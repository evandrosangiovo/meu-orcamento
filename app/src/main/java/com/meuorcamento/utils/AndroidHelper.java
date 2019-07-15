package com.meuorcamento.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.meuorcamento.R;

public class AndroidHelper {

    protected static final String TAG = "AndroidUtils";

    public static final int SUCCESS = R.drawable.ok_small;
    public static final int ERROR = R.drawable.error_small;
    public static final int QUESTION = R.drawable.question_small;
    public static final int INFORMATION = R.drawable.information_small;
    public static final int WARNING = R.drawable.warning_small;


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo info1 : info) {
                    if (info1.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void ativarGPS(Context context) {
        final Intent poke = new Intent();
        poke.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
        poke.setData(Uri.parse("3"));
        context.sendBroadcast(poke);
    }

    public static void alertDialog(final Context context, final int mensagem) {
        // try {
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle(
                context.getString(R.string.app_name)).setMessage(mensagem).create();
        dialog.setButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
        //} catch (Exception e) {
        //    Log.e(TAG, e.getMessage(), e);
        //}
    }

    public static void alertDialog(final Context context, final String mensagem, final int icon) {

        //try {
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle(
                context.getString(R.string.app_name)).setMessage(mensagem).setIcon(icon)
                .create();
        dialog.setButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }

    public static void alertDialog(final Context context, final String titulo, final String mensagem, final int icon) {

        //try {
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle(titulo).setMessage(mensagem).setIcon(icon)
                .create();
        dialog.setButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }

    public static void alertDialogYesNo(final Context context, final String mensagem, final int icon, final OnClickListener listener) {
        //try {
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle(
                context.getString(R.string.app_name)).
                setIcon(icon).
                setMessage(mensagem).
                setPositiveButton("Sim", listener).
                setNegativeButton("Não", listener).
                create();
        dialog.show();
    }

    public static void alertDialogTwoButtons(final Context context, final String mensagem, final int icon, final OnClickListener listener, final String labelPositive, final String labelNegative) {
        // try {
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle(
                context.getString(R.string.app_name)).
                setIcon(icon).
                setMessage(mensagem).
                setPositiveButton(labelPositive, listener).
                setNegativeButton(labelNegative, listener).
                create();
        dialog.show();


    }

    public static void alertDialogTwoButtons(final Context context, final String title, final String mensagem, final int icon, final OnClickListener listener, final String labelPositive, final String labelNegative) {
        // try {
        AlertDialog dialog = new AlertDialog.Builder(context).
                setTitle(title).
                setIcon(icon).
                setMessage(mensagem).
                setPositiveButton(labelPositive, listener).
                setNegativeButton(labelNegative, listener).
                create();
        dialog.show();
    }

    public static void alertDialogThreeButtons(final Context context, final String title, final String mensagem, final int icon, final OnClickListener listener, final String labelPositive, final String labelNeutral, final String labelNegative) {
        //try {
        AlertDialog dialog = new AlertDialog.Builder(context).
                setTitle(title).
                setIcon(icon).
                setMessage(mensagem).
                setPositiveButton(labelPositive, listener).
                setNeutralButton(labelNeutral, listener).
                setNegativeButton(labelNegative, listener).
                create();
        dialog.show();
    }

    public static void alertDialogTwoButtons(final Context context, final String titulo, final int icon, final OnClickListener listener, final String labelPositive, final String labelNegative, final ListView layout, final OnItemClickListener listenerItemSelected) {
        //try {

        layout.setOnItemClickListener(listenerItemSelected);
        AlertDialog dialog = new AlertDialog.Builder(context).
                setTitle(titulo).
                setIcon(icon).
                setView(layout).
                setPositiveButton(labelPositive, listener).
                setNegativeButton(labelNegative, listener).
                create();
        dialog.show();
        //} catch (Exception e) {
        //    Log.e(TAG, e.getMessage(), e);
        //}
    }

    public static void alertDialog(final Context context, final String mensagem, final int icon, final OnClickListener listener) {
        //try {
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle(
                context.getString(R.string.app_name)).
                setIcon(icon).
                setMessage(mensagem).
                setPositiveButton("OK", listener).
                create();
        dialog.show();
        //} catch (Exception e) {
        //    Log.e(TAG, e.getMessage(), e);
        //}
    }

    public static void alertDialog(final Context context, final String titulo, final String mensagem, final int icon, final OnClickListener listener) {
        //try {
        AlertDialog dialog = new AlertDialog.Builder(context).setTitle(
                context.getString(R.string.app_name)).
                setTitle(titulo).
                setIcon(icon).
                setMessage(mensagem).
                setPositiveButton("OK", listener).
                create();
        dialog.show();
        //} catch (Exception e) {
        //    Log.e(TAG, e.getMessage(), e);
        //}
    }

    // Retorna se é Android 3.x "honeycomb" ou superior (API Level 11)
    public static boolean isAndroid_3() {
        int apiLevel = Build.VERSION.SDK_INT;
        if (apiLevel >= 11) {
            return true;
        }
        return false;
    }

    // Retorna se a tela é large ou xlarge
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    // Retona se é um tablet com Android 3.x
    public static boolean isAndroid_3_Tablet(Context context) {
        return isAndroid_3() && isTablet(context);
    }

    // Fecha o teclado virtual se aberto (view com foque)
    public static boolean closeVirtualKeyboard(Context context, View view) {
        // Fecha o teclado virtual
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            boolean b = imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return b;
        }
        return false;
    }

    public static void killApp(boolean killSafely) {
        if (killSafely) {
            /*
             * Notify the system to finalize and collect all objects of the app
             * on exit so that the virtual machine running the app can be killed
             * by the system without causing issues. NOTE: If this is set to
             * true then the virtual machine will not be killed until all of its
             * threads have closed.
             */
            System.runFinalizersOnExit(true);

            /*
             * Force the system to close the app down completely instead of
             * retaining it in the background. The virtual machine that runs the
             * app will be killed. The app will be completely created as a new
             * app in a new virtual machine running in a new process if the user
             * starts the app again.
             */
            System.exit(0);
        } else {
            /*
             * Alternatively the process that runs the virtual machine could be
             * abruptly killed. This is the quickest way to remove the app from
             * the device but it could cause problems since resources will not
             * be finalized first. For example, all threads running under the
             * process will be abruptly killed when the process is abruptly
             * killed. If one of those threads was making multiple related
             * changes to the database, then it may have committed some of those
             * changes but not all of those changes when it was abruptly killed.
             */
            android.os.Process.killProcess(android.os.Process.myPid());
        }

    }

    public static boolean isTimeAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return Settings.System.getInt(c.getContentResolver(), Settings.System.AUTO_TIME, 0) == 1;
        }
    }

}
