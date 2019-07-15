package com.meuorcamento.utils;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

/**
 * Created by Administrador on 25/01/2015.
 */
public class NotificationHelper {


    public static void gerarNotificacao(Context context, int id, int number, String title, String text, String info, String ticker) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;

        Notification.Builder builder = new Notification.Builder(context)
                .setNumber(number)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(text)
                .setContentInfo(info)
                .setTicker(ticker)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setVibrate(new long[] {1000, 1000, 1000, 1000, 1000});

        notification = builder.getNotification();
        notificationManager.notify(id, notification);
    }

    public static void gerarNotificacao(Context context, int id, int number, String title, String text) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;

        Notification.Builder builder = new Notification.Builder(context)
                .setAutoCancel(true)
                .setNumber(number)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(text)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        notification = builder.getNotification();

        notificationManager.notify(id, notification);
    }

    public static void gerarNotificacao(Context context, int id, int number, String title, String text, String info) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;

        Notification.Builder builder = new Notification.Builder(context)
                .setAutoCancel(true)
                .setNumber(number)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(text)
                .setContentInfo(info)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        notification = builder.getNotification();
        notificationManager.notify(id, notification);
    }

    private void execute(Context context, int id, int number, String title, String text, String info, String ticker) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;

        Notification.Builder builder = new Notification.Builder(context)
                .setNumber(number)
                .setAutoCancel(true)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(text)
                .setContentInfo(info)
                .setTicker(ticker)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setVibrate(new long[] {1000, 1000, 1000, 1000, 1000});

        notification = builder.getNotification();
        notificationManager.notify(id, notification);
    }
}
