package com.meuorcamento.utils.internet;

import android.util.Log;
import java.io.IOException;
import java.net.HttpURLConnection;

public class InternetAccess {

    public static void checkInternetAccess(String urlString, IInternetAccessResult callBack) {
        HttpURLConnection urlConnect = null;
        try {
            java.net.URL a = new java.net.URL(urlString);
            urlConnect = (HttpURLConnection) a.openConnection();
            urlConnect.setConnectTimeout(4000);
            urlConnect.setUseCaches(false);
            urlConnect.setInstanceFollowRedirects(false);
            int responseCode = urlConnect.getResponseCode();

            if (responseCode > 299) {
                callBack.onError(new IOException("Tentativa de acesso a URL falhou!\n\nCódigo de resposta: " + responseCode));
            } else {
                callBack.onSuccess();
            }

        } catch (IOException ex) {
            callBack.onError(ex);
        } finally {
            if (urlConnect != null) {
                urlConnect.disconnect();
            }
        }
    }

    public static boolean checkInternetAccess(String urlString) throws IOException {
        return runCheckInternetAccess(urlString, true);
    }


    public static boolean checkInternetAccessWithException(String urlString) throws IOException {
            return runCheckInternetAccess(urlString, true);
    }

    public static boolean checkInternetAccessWithoutException(String urlString) {
        try {
            return runCheckInternetAccess(urlString, false);
        }catch(IOException ex) {
            return false;
        }
    }

    private static boolean runCheckInternetAccess(String urlString, boolean throwExeption) throws IOException {
        HttpURLConnection urlConnect = null;
        try {
            java.net.URL a = new java.net.URL(urlString);

            urlConnect = (HttpURLConnection) a.openConnection();
            urlConnect.setUseCaches(false);
            urlConnect.setInstanceFollowRedirects(false);
            urlConnect.setConnectTimeout(1000);
            urlConnect.connect();
            int responseCode = urlConnect.getResponseCode();

            /*
            if (responseCode <= 299) {
                return true;
            }
            if (throwExeption) {
                throw new IOException("Tentativa de acesso a URL falhou!\n\nCódigo de resposta: " + responseCode);
            }
            */
            return true;

        } catch (IOException ex) {
            if (throwExeption) {
                throw new IOException("Erro na conexão com a internet.\nPor favor verifique sua conexão!\n\n" + ex.getMessage(), ex);
            }
            return false;
        } finally {
            if (urlConnect != null) {
                try {
                    urlConnect.disconnect();
                } catch (Exception ex) {
                    Log.e("InternetAccess", ex.getMessage());
                }
            }
        }
    }
}
