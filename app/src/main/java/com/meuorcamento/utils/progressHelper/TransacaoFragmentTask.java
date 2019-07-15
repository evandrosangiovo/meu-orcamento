package com.meuorcamento.utils.progressHelper;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.meuorcamento.utils.AndroidHelper;


public class TransacaoFragmentTask extends AsyncTask<Void, Void, Boolean> {

    private static final String TAG = "TransacaoTaskFragment";
    private final Context context;
    private final ITransacao transacao;
    private Throwable exceptionErro;


    private ProgressDialog progresso;


    public TransacaoFragmentTask(Context context, ITransacao transacao, int aguardeMsg) {
        this.context = context;
        this.transacao = transacao;
        progresso = new ProgressDialog(context);
        progresso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progresso.setIcon(AndroidHelper.INFORMATION);
        progresso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progresso.setIcon(AndroidHelper.INFORMATION);
        progresso.setMessage(context.getString(aguardeMsg));
        progresso.setCancelable(false);
        progresso.setCanceledOnTouchOutside(false);
    }

    public TransacaoFragmentTask(Context context, ITransacao transacao, final String title, final String mensagem) {
        this.context = context;
        this.transacao = transacao;

        progresso  = new ProgressDialog(context);
        progresso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progresso.setIcon(AndroidHelper.INFORMATION);
        progresso.setTitle(title);
        progresso.setMessage(mensagem);
        progresso.setCancelable(false);
        progresso.setCanceledOnTouchOutside(false);
    }

    public TransacaoFragmentTask(Context context, ITransacao transacao, final String title, final String mensagem, int maxProgress) {
        this.context = context;
        this.transacao = transacao;
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


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
            //this.progresso = null;
        }
    }



    private void openProgress() {
        if(progresso == null)
            return;

        try {
            progresso.show();
        } catch (Exception e) {
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
        }
    }


    public void disableDialog() {
        progresso = null;
    }
}
