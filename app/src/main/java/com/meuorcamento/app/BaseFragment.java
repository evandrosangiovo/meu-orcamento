package com.meuorcamento.app;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseError;
import com.meuorcamento.FirebaseUtil;
import com.meuorcamento.R;
import com.meuorcamento.app.interfaces.IFirebaseResult;
import com.meuorcamento.app.interfaces.IReloadView;

public abstract class BaseFragment extends Fragment implements IFirebaseResult, IReloadView {


    private static final String KEY = "BaseFragment";
    private ProgressDialog mProgressDialog;

    @Override
    public void openProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Aguarde...");
        }

        mProgressDialog.show();
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

    @Override
    public void closeProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public String getUID() {
        return FirebaseUtil.getUid();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item == null)
            return true;

        switch (item.getItemId()) {
            case R.id.btnReload: {
                reloadView();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
