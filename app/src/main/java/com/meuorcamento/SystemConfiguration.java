package com.meuorcamento;

import java.io.File;


import android.content.Context;
import android.os.Environment;

import com.google.firebase.database.FirebaseDatabase;
import com.meuorcamento.data.DataBaseHelper;
import com.meuorcamento.data.DatabaseManager;
import com.meuorcamento.data.mapping.Mapping;

public class SystemConfiguration {


    public static final boolean DEBUG = true;
    private static boolean mepeado = false;


    private static final String FOLDER_BASE = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String FOLDER_APLICATIVO = String.format("%s%s", FOLDER_BASE, "/MeuOcamento.com/");
    public static final String FOLDER_IMAGENS = String.format("%s%s", FOLDER_APLICATIVO, "/Imagens/");
    public static String FOLDER_DB;

    private SystemConfiguration() {

    }

    public static synchronized void buildInitMapping(Context context) {

        if(context == null)
            return;

        if (!mepeado) {
            FOLDER_DB = context.getDatabasePath("ps").getAbsolutePath();
            Mapping.createMapping();
            DatabaseManager.initializeInstance(new DataBaseHelper(context));
            mepeado = true;
        }
    }

    public static void criarDiretoriosDoSistema() {
        File folder;

        folder = new File(FOLDER_IMAGENS);
        folder.mkdirs();
        folder.setExecutable(true, false);
        folder.setReadable(true, false);
        folder.setWritable(true, false);

    }

    public static final String[] sqls = new String[]{



    };

}
