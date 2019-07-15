package com.meuorcamento.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.meuorcamento.FirebaseUtil;
import com.meuorcamento.app.interfaces.IFirebaseResult;


public abstract class BaseActivity extends Activity implements IFirebaseResult {

    private static final String KEY = "BaseActivity";
    private ProgressDialog mProgressDialog;


    @Override
    public void openProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Aguarde...");
        }

        try {
            mProgressDialog.show();
        }catch (Exception ex) {

        }
    }

    @Override
    public void closeProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
            }catch (Exception ex) {

            }
        }
    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public String getUID() {
        return FirebaseUtil.getUid();
    }

    @Override
    public void onDataReceived(Object result) {
        if(result == null)
            return;

        Log.i(KEY, result.toString());
    }

    @Override
    public void onError(DatabaseError databaseError) {

    }

    /*
    public boolean userAuthenticated() {
        return FirebaseUtil.userAuthenticated();
    }
    public FirebaseUser getFirebaseUser() {
        return FirebaseUtil.getFirebaseUser();
    }

    public FirebaseAuth getFirebaseAuthInstance() {
        return FirebaseUtil.getFirebaseAuthInstance();
    }
    */

    public void signOut() {
        FirebaseUtil.signOut();
    }

    /*
    public void sendSMSVerification(String numeroTelefone, PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(numeroTelefone, 60, TimeUnit.SECONDS, this, callbacks);
    }
    
    public void confirmSMSCodeToVerifyPhoneNumber(String receivedVerificationId, String SMSCodeUser, OnCompleteListener<AuthResult> onCompleteListener) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(receivedVerificationId, SMSCodeUser);
        getFirebaseAuthInstance().signInWithCredential(credential).addOnCompleteListener(onCompleteListener);
    }

    public void sendEmailVerification() {
        getFirebaseUser().sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }
    */
}
