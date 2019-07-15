package com.meuorcamento.data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseManager {

    private AtomicInteger mOpenCounter = new AtomicInteger();

    private static DatabaseManager instance;
    private static SQLiteOpenHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;
    private static final String TAG = "DatabaseManager";


    private DatabaseManager() { }

    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (instance == null) {
            instance = new DatabaseManager();
            mDatabaseHelper = helper;
        }
    }

    protected static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(String.format("%s %s",
                    DatabaseManager.class.getSimpleName(),
                    "is not initialized, call initializeInstance(..) method first."));
        }

        return instance;
    }

    protected synchronized SQLiteDatabase openDatabase() throws InterruptedException {

        if (mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    protected synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            if(mDatabase == null)
                return;
            mDatabase.close();
        }
    }
}
