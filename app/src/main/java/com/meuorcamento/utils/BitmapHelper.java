
package com.meuorcamento.utils;
import java.util.Stack;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class BitmapHelper {

	//private HashMap<String, Bitmap> cache = new HashMap<String, Bitmap>();
    //private static int MAX_CACHE = 15;
    //private final int tamanhoImagem;
    private final FilaDownload fila = new FilaDownload();
    private final ThreadDownload threadDownload = new ThreadDownload();

    public BitmapHelper() {
        threadDownload.setPriority(Thread.NORM_PRIORITY - 1);
    }

	// Faz o download da imagem para a URL Fornecida
    // O ProgressBar é utilizado para a animação, e o ImageView será atualizado
    // automaticamente depois do download
    public void processBitmap(String url, ImageView imageView, ProgressBar progress) {

        try {
            imageView.setTag(url);
            preparaDownloadImagem(url, imageView, progress);
            imageView.setVisibility(View.INVISIBLE);
            if (progress != null) {
                progress.setVisibility(View.VISIBLE);
            }
        } catch (Exception ex) {
        }
    }

    public void processBitmap(String url, ImageView imageView) {

        try {
            imageView.setTag(url);
            preparaDownloadImagem(url, imageView, null);
            imageView.setVisibility(View.INVISIBLE);
        } catch (Exception ex) {
        }
    }

    // Insere a URL de download na fila para ser processada pela thread
    private void preparaDownloadImagem(String url, ImageView imageView, ProgressBar progress) {
        fila.zerar(imageView);
        ImagemDownload p = new ImagemDownload(url, imageView, progress);
        synchronized (fila.imgs) {
            // Insere uma nova imagem para ser processada
            fila.imgs.push(p);
            fila.imgs.notifyAll();
        }
        if (threadDownload.getState() == Thread.State.NEW) {
            threadDownload.start();
        }
    }

    class FilaDownload {

        private final Stack<ImagemDownload> imgs = new Stack<ImagemDownload>();

        public void zerar(ImageView image) {
            for (int j = 0; j < imgs.size();) {
                if (imgs.get(j).imageView == image) {
                    imgs.remove(j);
                } else {
                    ++j;
                }
            }
        }
    }

    class ImagemDownload {

        ImageView imageView;
        ProgressBar progressBar;
        String URL;

        ImagemDownload(String URL, ImageView imageView, ProgressBar progressBar) {
            this.URL = URL;
            this.imageView = imageView;
            this.progressBar = progressBar;
        }
    }

    // Para a thread
    public void stopThread() {
        threadDownload.interrupt();
    }

    class ThreadDownload extends Thread {

        @Override
        public void run() {
            try {
                while (true) {
                    // Aguarda e Sincroniza...
                    if (fila.imgs.size() == 0) {
                        synchronized (fila.imgs) {
                            fila.imgs.wait();
                        }
                    }
                    if (fila.imgs.size() != 0) {
                        final ImagemDownload img;
                        synchronized (fila.imgs) {
                            // Recebe imagem para fazer download
                            img = fila.imgs.pop();
                        }
                        final Bitmap bitmap = IOHelper.decodeBitmapFromUrl(img.URL, 200, 400);

                        Object tag = img.imageView.getTag();
                        if (tag != null && tag.toString().equals(img.URL)) {
                            Activity a = (Activity) img.imageView.getContext();
                            // Atualiza a imagem na Thread principal
                            a.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (bitmap != null) {
                                        img.imageView.setImageBitmap(bitmap);
                                        img.imageView.setVisibility(View.VISIBLE);
                                        if (img.progressBar != null) {
                                            img.progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                }
                            });
                        }
                    }
                    if (Thread.interrupted()) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
            }
        }
    }

}
