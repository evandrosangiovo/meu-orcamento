package com.meuorcamento.utils.progressHelper;
import android.content.Context;

public class ProgressDialog extends android.app.ProgressDialog {


    public ProgressDialog(Context context) {
        super(context);
    }

    public ProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
