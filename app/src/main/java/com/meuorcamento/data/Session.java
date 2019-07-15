package com.meuorcamento.data;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Administrador on 30/06/2015.
 */
public class Session {

    private static final String KEY = "SESSION";

    private static Session instance;
    private DatabaseManager databaseManager;
    private SQLiteDatabase sqLiteDatabase;


    private Session() {
        databaseManager = DatabaseManager.getInstance();
        //sqLiteDatabase = databaseManager.openDatabase();
    }

    public static synchronized Session getCurrentSession() {
        if(instance == null)
            instance = new Session();

        return instance;
    }

    private synchronized Session openDatabase() throws DataAccessException {
        try {
            sqLiteDatabase = databaseManager.openDatabase();
        }catch (Exception ex) {
            throw new DataAccessException("Erro ao abrir a conexão!", ex);
        }

        return this;
    }

    private synchronized Session closeDatabase() throws DataAccessException {
        try {
            databaseManager.closeDatabase();
        }catch (Exception ex) {
            throw new DataAccessException("Erro ao abrir a conexão!", ex);
        }

        return this;
    }

    public synchronized void executeWithScopeTransaction(CodeScope codeScope) throws DataAccessException{
        try {
            Log.i(KEY, "Scope started...");
            sqLiteDatabase = databaseManager.openDatabase();
            Log.i(KEY, "DB opened...");
            sqLiteDatabase.beginTransaction();
            Log.i(KEY, "Transaction started...");
            Log.i(KEY, "Executing scope...");
            codeScope.execute();
            Log.i(KEY, "Scope executed successful...");
            sqLiteDatabase.setTransactionSuccessful();
            codeScope.onSuccess();
        }catch (Exception ex) {
            Log.e(KEY, "Failed to execute scope...", ex);
            throw new DataAccessException(String.format("Erro na execução do escopo em transação! %s", ex.getMessage(), ex));
        }finally {
            sqLiteDatabase.endTransaction();
            Log.i(KEY, "Transaction finished...");
            databaseManager.closeDatabase();
            Log.i(KEY, "DB closed...");
        }
    }


    public synchronized void executeWithScopeTransactionNonExclusive(CodeScope codeScope) throws DataAccessException{
        try {
            Log.i(KEY, "Scope started...");
            sqLiteDatabase = databaseManager.openDatabase();
            Log.i(KEY, "DB opened...");
            sqLiteDatabase.beginTransactionNonExclusive();;
            Log.i(KEY, "Transaction started...");
            Log.i(KEY, "Executing scope...");
            codeScope.execute();
            Log.i(KEY, "Scope executed successful...");
            sqLiteDatabase.setTransactionSuccessful();
            codeScope.onSuccess();
        }catch (Exception ex) {
            Log.e(KEY, "Failed to execute scope...", ex);
            throw new DataAccessException(String.format("Erro na execução do escopo em transação! %s", ex.getMessage(), ex));
        }finally {
            sqLiteDatabase.endTransaction();
            Log.i(KEY, "Transaction finished...");
            databaseManager.closeDatabase();
            Log.i(KEY, "DB closed...");
        }
    }

    public long queryNumEntries (String tableName, String columnsWhere, String[] selectionArgs) {
        long count;

        if(sqLiteDatabase == null || !sqLiteDatabase.isOpen()) {
            openDatabase();
            count = DatabaseUtils.queryNumEntries(sqLiteDatabase, tableName, columnsWhere, selectionArgs);
            closeDatabase();
        } else {
            count = DatabaseUtils.queryNumEntries(sqLiteDatabase, tableName, columnsWhere, selectionArgs);
        }
        return count;
    }

    public Cursor rawQuery(String query, String[] selectionArgs ) {
        Cursor cursor;
        openDatabase();
        cursor = sqLiteDatabase.rawQuery(query, selectionArgs);

        return cursor;
    }

    //.update(tableName, values, getSqlFormatWhere(primaryKeyColumns), valuesArgs);
    public int update(String tableName, ContentValues values, String columnsWhere, String[] valuesArgs) {
        int rowsUpdated;
        if(sqLiteDatabase == null || !sqLiteDatabase.isOpen()) {
            openDatabase();
            rowsUpdated = sqLiteDatabase.update(tableName, values, columnsWhere, valuesArgs);
            closeDatabase();
        } else {
            rowsUpdated = sqLiteDatabase.update(tableName, values, columnsWhere, valuesArgs);;
        }

        return rowsUpdated;
    }

    public void execSQL(String sql) {
        if(sqLiteDatabase == null || !sqLiteDatabase.isOpen()) {
            openDatabase();
            sqLiteDatabase.execSQL(sql);
            closeDatabase();
        } else {
            sqLiteDatabase.execSQL(sql);
        }
    }

    //insertOrThrow(tableName, null, values);
    public long insertOrThrow(String tableName, String nullColumnHack, ContentValues values) {
        long id;
        if(sqLiteDatabase == null || !sqLiteDatabase.isOpen()) {
            openDatabase();
            id = sqLiteDatabase.insertOrThrow(tableName, nullColumnHack, values);
            closeDatabase();
        }else {
            id = sqLiteDatabase.insertOrThrow(tableName, nullColumnHack, values);
        }
        return id;
    }

    public long insertWithOnConflict(String tableName, String nullColumnHack, ContentValues values, int conflictAlgorithm) {
        long id;
        if(sqLiteDatabase == null || !sqLiteDatabase.isOpen()) {
            openDatabase();
            id = sqLiteDatabase.insertWithOnConflict(tableName, nullColumnHack, values, conflictAlgorithm);
            closeDatabase();
        }else {
            id = sqLiteDatabase.insertWithOnConflict(tableName, nullColumnHack, values, conflictAlgorithm);
        }
        return id;
    }

    //return sqLiteDatabase.delete(dbManager.openDatabase().delete(tableName, columnsWhere, buildValuesToString(valuesWhere))
    public int delete(String tableName, String columnsWhere, String[] valuesWhere) {
        int numRowsDeleted;
        if(sqLiteDatabase == null || !sqLiteDatabase.isOpen()) {
            openDatabase();
            numRowsDeleted = sqLiteDatabase.delete(tableName, columnsWhere, valuesWhere);
            closeDatabase();
        }else {
            numRowsDeleted = sqLiteDatabase.delete(tableName, columnsWhere, valuesWhere);
        }
        return numRowsDeleted;
    }

    //dbManager.openDatabase().query(tableName, allColumnNames, columnsWhere, buildValuesToString(valuesWhere), groupBy, having, orderBy, buildLimit(limitStart, limitTo));
    public Cursor query(String tableName, String[] allColumnNames, String columnsWhere, String[] valuesWhere, String groupBy, String having, String orderBy, String limits) {
        Cursor cursor;
        openDatabase();
        cursor = sqLiteDatabase.query(tableName, allColumnNames, columnsWhere, valuesWhere, groupBy, having, orderBy, limits);

        return cursor;
    }

    @Override
    protected void finalize() throws Throwable {
        //if(sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
        //    sqLiteDatabase.close();
        //}
        super.finalize();
    }


    public void executeTestBulkInsert() {
        /*
        // Create a single InsertHelper to handle this set of insertions.
        DatabaseUtils.InsertHelper ih = new DatabaseUtils.InsertHelper(sqLiteDatabase, "teste");

        // Get the numeric indexes for each of the columns that we're updating
        final int greekColumn = ih.getColumnIndex("Greek");
        final int ionicColumn = ih.getColumnIndex("Ionic");
        final int romanColumn = ih.getColumnIndex("Roman");

        try {
            while (moreRowsToInsert) {
                // ... Create the data for this row (not shown) ...

                // Get the InsertHelper ready to insert a single row
                ih.prepareForInsert();

                // Add the data for each column
                ih.bind(greekColumn, greekData);
                ih.bind(ionicColumn, ionicData);
                //...
                ih.bind(romanColumn, romanData);

                // Insert the row into the database.
                ih.execute();
            }
        }
        finally {
            ih.close();  // See comment below from Stefan Anca
        }
        */
    }
}
