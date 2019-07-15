package com.meuorcamento.app.dialog;


import android.app.FragmentManager;

/**
 * Created by Administrador on 03/09/2016.
 */
public interface IDialogFragmentCallback {
    void onPositiveClick();
    void onNegativeClick();
    FragmentManager getFragmentManager();
}
