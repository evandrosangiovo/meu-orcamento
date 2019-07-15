package com.meuorcamento.app.interfaces;

import android.content.Context;

import com.google.firebase.database.DatabaseError;

public interface IFirebaseResult {
    void openProgressDialog();
    void closeProgressDialog();

    void onDataReceived(Object result);
    void onError(DatabaseError databaseError);

    String getUID();
    Context getContext();
}
