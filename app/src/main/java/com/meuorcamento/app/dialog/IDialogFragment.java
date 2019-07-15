package com.meuorcamento.app.dialog;

public interface IDialogFragment {

    void showDialog();
    void resetView();
    void onPositiveClick();
    void onNegativeClick();
    String getViewTag();


}