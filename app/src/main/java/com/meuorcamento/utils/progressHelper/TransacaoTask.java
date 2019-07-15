package com.meuorcamento.utils.progressHelper;

import java.io.Closeable;
import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;

import com.meuorcamento.utils.AndroidHelper;

public class TransacaoTask extends AsyncTask<Void, Integer, Boolean> implements Closeable {

    private static final String TAG = "TrasacaoTask";
    private Context context;
    private OnPreExecute onPreExecute;
    private ITransacao transacao;
    private IOnProgressUpdate onProgressUpdate;

    private ProgressDialog progresso;
    private Throwable exceptionErro;

    public TransacaoTask(Context context, ITransacao transacao, int aguardeMsg) {
        this.context = context;
        this.transacao = transacao;
        progresso = new ProgressDialog(context);
        progresso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progresso.setIcon(AndroidHelper.INFORMATION);
        progresso.setMessage(context.getString(aguardeMsg));
        progresso.setCancelable(false);
        progresso.setCanceledOnTouchOutside(false);
    }

    public TransacaoTask(Context context, ITransacao transacao, final String title, final String mensagem) {
        this.context = context;
        this.transacao = transacao;
        progresso = new ProgressDialog(context);
        progresso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progresso.setIcon(AndroidHelper.INFORMATION);
        progresso.setTitle(title);
        progresso.setMessage(mensagem);
        progresso.setCancelable(false);
        progresso.setCanceledOnTouchOutside(false);
    }

    public TransacaoTask(Context context, ITransacao transacao, final String title, final String mensagem, final IOnClickAction action) {
        this.context = context;
        this.transacao = transacao;
        progresso = new ProgressDialog(context);
        progresso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progresso.setIcon(AndroidHelper.INFORMATION);
        progresso.setTitle(title);
        progresso.setMessage(mensagem);
        progresso.setCancelable(false);
        progresso.setCanceledOnTouchOutside(false);
        progresso.setButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (action != null)
                    action.onClick(dialog, which);
                cancel(true);
            }
        });
    }

    public TransacaoTask(Context context, ITransacao transacao, final String title, final String mensagem, final IOnProgressUpdate onProgressUpdate, int maxProgress, boolean onTop) {
        this.context = context;
        this.transacao = transacao;
        this.onProgressUpdate = onProgressUpdate;

        progresso = new ProgressDialog(context);
        progresso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progresso.setMax(maxProgress);
        progresso.setProgress(0);
        progresso.setIcon(AndroidHelper.INFORMATION);
        progresso.setTitle(title);
        
        progresso.setMessage(mensagem);
        progresso.setCancelable(false);
        progresso.setCanceledOnTouchOutside(false);
        if(onTop) {
            progresso.getWindow().setGravity(Gravity.BOTTOM);

        }
    }



    public TransacaoTask(Context context, ITransacao transacao, final String title, final String mensagem, final IOnProgressUpdate onProgressUpdate, int maxProgress) {
        this.context = context;
        this.transacao = transacao;
        this.onProgressUpdate = onProgressUpdate;

        progresso = new ProgressDialog(context);
        //progresso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progresso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progresso.setMax(maxProgress);
        progresso.setProgress(0);
        progresso.setIcon(AndroidHelper.INFORMATION);
        progresso.setTitle(title);
        progresso.setMessage(mensagem);
        progresso.setCancelable(false);
        progresso.setCanceledOnTouchOutside(false);
    }

    public TransacaoTask(Context context, ITransacao transacao, final String title, final String mensagem, final IOnClickAction action, final IOnProgressUpdate onProgressUpdate) {
        this.context = context;
        this.transacao = transacao;
        this.onProgressUpdate = onProgressUpdate;


        progresso = new ProgressDialog(context);
        progresso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progresso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progresso.setIcon(AndroidHelper.INFORMATION);
        progresso.setTitle(title);
        progresso.setMessage(mensagem);
        progresso.setCancelable(false);
        progresso.setCanceledOnTouchOutside(false);
        progresso.setButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(action != null)
                    action.onClick(dialog, which);
                cancel(true);
            }
        });
    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onCancelled(Boolean aBoolean) {
        super.onCancelled(aBoolean);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(onPreExecute != null)
            onPreExecute.execute();

        openProgress();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            transacao.executeTask();
        } catch (Throwable e) {
            this.exceptionErro = e;
            Log.e("TransacaoTask", e.getMessage(), e);
            return false;
        } finally {
            try {
                closeProgress();
            } catch (Exception e) {
                Log.e("TransacaoTask", e.getMessage(), e);
            }
        }
        return true;
    }

    /***
     * Usar somente no método executeTask
     * @param i Progresso atual
     */
    public void publicarProgresso(int i) {
        publishProgress(i);
        progresso.setProgress(i);
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if(onProgressUpdate != null)
            onProgressUpdate.onProgressUpdate(values[0]);
    }

    @Override
    protected void onPostExecute(Boolean ok) {

        super.onPostExecute(ok);
        try {
            if ((this.progresso != null) && this.progresso.isShowing()) {
                this.progresso.dismiss();
            }

            if (ok) {
                transacao.executeOnSuccess();
            } else {
                transacao.executeOnError(exceptionErro);
            }
        } catch (final IllegalArgumentException e) {
            Log.e("TransacaoTask", e.getMessage(), e);
        } catch (final Exception e) {
            Log.e("TransacaoTask", e.getMessage(), e);
        } finally {
            this.progresso = null;
        }
    }


    private void openProgress() {
        if(progresso == null)
            return;
        try {
            progresso.show();
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void closeProgress() {

        if(progresso == null)
            return;

        try {
            if ((this.progresso != null) && this.progresso.isShowing()) {
                this.progresso.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            Log.e("TransacaoTask", e.getMessage(), e);
        } catch (final Exception e) {
            Log.e("TransacaoTask", e.getMessage(), e);
        } finally {
            //this.progresso = null;
        }
    }

    @Override
    public void close() throws IOException {
        this.cancel(true);
    }

    public void disableDialog() {
        progresso = null;
    }

    public void setOnPreExecute(OnPreExecute OnPreExecute) {
        this.onPreExecute = OnPreExecute;

    }
}
