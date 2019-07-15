package com.meuorcamento.app.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import com.meuorcamento.app.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class InitActivity extends BaseActivity {


    private static final String TAG = "InitActivity";
    private static final int MY_PERMISSIONS_REQUEST = 1;

    public static void startActivity(Context context) {
        Intent it = new Intent(context, InitActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        verificarPermissoes();

    }

    private void verificarPermissoes() {

        List<String> manifestPermission = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            manifestPermission.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            manifestPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            manifestPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            manifestPermission.add(Manifest.permission.INTERNET);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            manifestPermission.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            manifestPermission.add(Manifest.permission.CHANGE_NETWORK_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            manifestPermission.add(Manifest.permission.ACCESS_WIFI_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            manifestPermission.add(Manifest.permission.CHANGE_WIFI_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            manifestPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            manifestPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            manifestPermission.add(Manifest.permission.VIBRATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED) {
            manifestPermission.add(Manifest.permission.RECEIVE_BOOT_COMPLETED);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
            manifestPermission.add(Manifest.permission.WAKE_LOCK);
        }

        if (manifestPermission.isEmpty()) {
            LoginActivity.startActivity(this);
            finish();
            return;
        }


        ActivityCompat.requestPermissions(this, manifestPermission.toArray(new String[0]), MY_PERMISSIONS_REQUEST);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        finish();
                        return;
                    }
                }
                LoginActivity.startActivity(this);
                finish();
                break;
            }
        }
    }
}

