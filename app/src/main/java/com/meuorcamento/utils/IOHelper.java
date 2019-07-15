package com.meuorcamento.utils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.util.Log;

public class IOHelper {

    private IOHelper() {
    }

    //private static final String TAG = "IOUtils";
    //private static final int IMAGE_MAX_SIZE = 1024;
    //private static int MAX_CACHE = 25;
    /**
     * Cria um BufferedReader para percorrer o arquivo<br>
     * Chamar o método close() apos utilizar o mesmo<br>
     * Somente utilizar para leitura e nunca para escrita <br>
     * Se o arquivo nao existir o retorno sera null<br>
     *
     * @param url Caminho do arquivo e nome
     * "/mnt/sdcard/DCIM/nomedoarquivo.jpeg"
     * @return BufferedReader
     * @throws FileNotFoundException
     */
    public static BufferedReader getBufferedReader(String url) throws FileNotFoundException {
        BufferedReader br;
        FileReader fr;

        File file;

        file = new File(url);

        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException ex) {
            throw ex;
        }

        br = new BufferedReader(fr);

        return br;
    }



    public static String byteToString(byte[] bytes) throws IOException {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * Pega o arquivo e "quebra" em byte[]
     *
     * @param url Caminho do arquivo e nome
     * "/mnt/sdcard/DCIM/nomedoarquivo.jpeg"
     * @return byte[] do arquivo
     * @throws IOException
     */
    public static byte[] archiveToByte(String url) throws IOException {

        return toByte(new File(url), false);
    }

    /**
     * Pega o arquivo e "quebra" em byte[]
     *
     * @param url
     * @return byte[] do arquivo
     * @throws IOException
     */
    public static byte[] archiveToByte(File url) throws IOException {
        return toByte(url, false);
    }

    public static byte[] archiveToByte(String url, boolean deleteFile) throws IOException {
        return toByte(new File(url), deleteFile);
    }

    public static byte[] archiveToByte(File url, boolean deleteFile) throws IOException {
        return toByte(url, deleteFile);
    }



    private static byte[] toByte(File file, boolean deleteFile) throws IOException {
        byte[] binario = null;
        InputStream stream;

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        byte[] buffer = new byte[8192];

        try {
            stream = new BufferedInputStream(new FileInputStream(file));

            while (stream.read(buffer) != -1) {
                output.write(buffer);
            }
            binario = output.toByteArray();

            stream.close();
            output.flush();
            output.close();

        } catch (IOException ex) {
            throw ex;
        }

        if (deleteFile) {
            file.delete();
        }

        return binario;
    }

    public static ArrayList<File> listFilesForFolder(final File folder, final boolean recursivity, final String patternFileFilter) {

        // Inputs
        boolean filteredFile = false;

        // Ouput
        final ArrayList<File> output = new ArrayList<File> ();

        // Foreach elements
        for (final File fileEntry : folder.listFiles()) {

            //File g = new File("");
            //g.
            // If this element is a directory, do it recursivly
            if (fileEntry.isDirectory()) {
                if (recursivity) {
                    output.addAll(listFilesForFolder(fileEntry, recursivity, patternFileFilter));
                }
            }
            else {
                // If there is no pattern, the file is correct
                if (patternFileFilter.length() == 0) {
                    filteredFile = true;
                }
                // Otherwise we need to filter by pattern
                else {
                    filteredFile = Pattern.matches(patternFileFilter, fileEntry.getName());
                }

                // If the file has a name which match with the pattern, then add it to the list
                if (filteredFile) {
                    output.add(fileEntry);
                }
            }
        }

        return output;
    }


    public static List<String> listFilesNamesForFolder(final File folder, final boolean recursivity, final String patternFileFilter) {

        // Inputs
        boolean filteredFile = false;

        // Ouput
        final ArrayList<String> output = new ArrayList<String> ();

        // Foreach elements
        for (final File fileEntry : folder.listFiles()) {

            if (fileEntry.isDirectory()) {
                if (recursivity) {
                    output.addAll(listFilesNamesForFolder(fileEntry, recursivity, patternFileFilter));
                }
            }
            else {
                // If there is no pattern, the file is correct
                if (patternFileFilter.length() == 0) {
                    filteredFile = true;
                }
                else {
                    filteredFile = Pattern.matches(patternFileFilter, fileEntry.getName());
                }

                // If the file has a name which match with the pattern, then add it to the list
                if (filteredFile) {
                    output.add(fileEntry.getName());
                }
            }
        }

        return output;
    }

    /**
     *
     * @param url local onde deve ser salvo o arquivo [incluir caminho e nome]
     * @param bytes binario do arquivo
     * @throws IOException
     */
    public static boolean byteToArchive(String url, byte[] bytes) throws IOException {
        File file;
        file = new File(url);

        file.delete();
        file.createNewFile();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bos.write(bytes);
        bos.flush();
        bos.close();

        return file.exists();

    }

    public static boolean bitmapToArchive(String url, Bitmap bitmap) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bytes = bos.toByteArray();

        return byteToArchive(url, bytes);

    }


    public static byte[] bitmapToByteArray(Bitmap bitmap) {

        if(bitmap == null)
            return null;

        return executeBitmapToByteArray(bitmap, 100);

    }


    public static byte[] bitmapToByteArray(Bitmap bitmap, int quality) {

        if(bitmap == null)
            return null;

        return executeBitmapToByteArray(bitmap, quality);

    }

    private static byte[] executeBitmapToByteArray(Bitmap bitmap, int quality ) {

        if(bitmap == null)
            return null;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, bos);
        byte[] bytes = bos.toByteArray();

        return bytes;

    }


    public static Bitmap getBitMap(byte[] binario) {
        return BitmapFactory.decodeByteArray(binario, 0, binario.length);
    }

    public static Bitmap getBitMap(String url) {
        return BitmapFactory.decodeFile(url);

    }


    public static long getTamanhoDoArquivoEmBytes(String url) {
        File file = new File(url);
        long tam = file.length();
        return  tam;
    }

    public static boolean deletarArquivo(String url) {
        File file = new File(url);

        if (file.exists()) {
            return file.delete();
        }
        return true;
    }


    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        if(fileOrDirectory.delete()) {
            Log.i("IOUtils.clearDirectory", "Arquivo ou diretorio: " + fileOrDirectory.getName() + " excluido com sucesso!");
        } else {
            Log.w("IOUtils.clearDirectory", "Arquivo ou diretorio: " + fileOrDirectory.getName() + " nao foi excluido!");
        }
    }


    public static void clearDirectory(String url) {

        File file = new File(url);
        if (file != null && file.isDirectory()) {

            File[] files = file.listFiles();

            if (files != null) {
                for (File f : files) {
                    if(f.delete())
                        Log.i("IOUtils.clearDirectory", "Arquivo: " + f.getName() + " excluido com sucesso!");
                    else
                        Log.w("IOUtils.clearDirectory", "Arquivo: " + f.getName() + " nao foi excluido!");
                }
            }
        }
    }


    public static void clearDirectory(String url, List<String> listOfFilesToNotDelete) {

        File file = new File(url);
        if (file != null && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if(!listOfFilesToNotDelete.contains(f.getName()))
                        if(f.delete())
                            Log.i("IOUtils.clearDirectory", "Arquivo: " + f.getName() + " excluido com sucesso!");
                        else
                            Log.w("IOUtils.clearDirectory", "Arquivo: " + f.getName() + " nao foi excluido!");
                }
            }
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     *
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeBitmapFromUrl(String url, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //options.inPurgeable = true;
        //options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        BitmapFactory.decodeFile(url, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(url, options);
    }


    public static Bitmap resizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public static Boolean backupDataBase(String patchDB, String patchBackup) throws IOException {
        try {
            // Local database
            InputStream input = new FileInputStream(patchDB);

            // Path to the external backup
            OutputStream output = new FileOutputStream(patchBackup);

            // transfer bytes from the Input File to the Output File
            byte[] buffer = new byte[1024];
            int length;

            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            input.close();

            return true;
        } catch (IOException ex) {
            throw ex;
        }

    }
}
