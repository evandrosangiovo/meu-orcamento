package com.meuorcamento.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.meuorcamento.data.DBVersion.DBVersionFactory;
import com.meuorcamento.data.DBVersion.IUpdateDBVersion;

public final class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "meuorcamento";

    private static final int DB_VERSION = 1;

    private IUpdateDBVersion dataBaseVersion;

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        dataBaseVersion = DBVersionFactory.getInstanceByVersionCode(DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        initDb(db);
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            //db.execSQL("PRAGMA foreign_keys = OFF;");
        }
    }


    private void initDb(SQLiteDatabase db) {

        try {
            db.beginTransaction();

            for (String script : dataBaseVersion.getFullCreateDBScript()) {
                db.execSQL(script);
            }

            db.setTransactionSuccessful();

        } catch (SQLException ex) {
            Log.e("CREATE_BD", "Erro ao tentar executeTask a criação do BD.\n " + ex.getMessage());
            throw ex;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        try {
            db.beginTransaction();
            for (int currentVersion = oldVersion + 1; currentVersion <= newVersion; currentVersion++) {
                dataBaseVersion = DBVersionFactory.getInstanceByVersionCode(currentVersion);

                for (String script : dataBaseVersion.getAlterTables()) {
                    db.execSQL(script);
                }
                for (String script : dataBaseVersion.getCreateTables()) {
                    db.execSQL(script);
                }

                for (String script : dataBaseVersion.getInsertRows()) {
                    db.execSQL(script);
                }
                for (String script : dataBaseVersion.getUpdateRows()) {
                    db.execSQL(script);
                }
            }
            db.setTransactionSuccessful();
        } catch (SQLException ex) {
            Log.e("CREATE_BD", "Erro ao tentar executeTask a criação do BD. \n Script inválido:\n " + ex.getMessage());
            throw ex;
        } finally {
            db.endTransaction();

        }

    }


    public static int getDBVersion() {
        return DB_VERSION;
    }

    public static String getDBName() {
        return DB_NAME;
    }

}
