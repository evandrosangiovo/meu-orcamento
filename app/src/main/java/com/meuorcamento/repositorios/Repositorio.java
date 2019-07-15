package com.meuorcamento.repositorios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.meuorcamento.SystemConfiguration;
import com.meuorcamento.data.CodeScope;
import com.meuorcamento.data.Criteria;
import com.meuorcamento.data.DataAccessException;
import com.meuorcamento.data.ModelBase;
import com.meuorcamento.data.Session;
import com.meuorcamento.data.mapping.Column;
import com.meuorcamento.data.mapping.Table;
import com.meuorcamento.data.mapping.TableBuilder;
import com.meuorcamento.utils.NumberHelper;


import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Repositorio<T extends ModelBase> {

    private static String KEY = "Repositorio";
    private Table table;

    private String tableName;
    private String[] allColumnNames;
    private Class<T> tableTypeClass;
    private List<Column> allColumns;
    private List<Column> singleColumns;
    private List<Column> primaryKeyColumns;
    private List<Column> allColumnsLessAutoIncrement;
    private Field[] columnFields;
    private final Session session;

    public Repositorio(Context context, Class<T> persistentClazz) {
        try {
            SystemConfiguration.buildInitMapping(context);
        }catch (Exception ex) {
            Log.e("Repositorio", ex.getMessage());
        }

        session = Session.getCurrentSession();
        setInit(persistentClazz, context);
    }

    public Repositorio(Class<T> persistentClazz, Context context) {


        session = Session.getCurrentSession();
        try {
            SystemConfiguration.buildInitMapping(context);

        }catch (Exception ex) {
            Log.e("Repositorio", ex.getMessage());
        }

        setInit(persistentClazz, context);
    }

    private void setInit(Class<T> persistentClass, Context context) {

        //DatabaseManager.initializeInstance(new DataBaseHelper(context));


        //dbManager = DatabaseManager.getInstance();
        tableTypeClass = persistentClass;
        table = TableBuilder.getTable(tableTypeClass.getName());
        tableName = table.getTableName();
        columnFields = table.getFields();
        Field.setAccessible(columnFields, true);
        allColumnNames = table.getAllColumnNames();
        allColumns = table.getAllColumns();
        primaryKeyColumns = table.getPrimaryKeyColumns();
        singleColumns = table.getSingleColumns();
        allColumnsLessAutoIncrement = table.getAllColumnsLessAutoIncrement();

    }

    /**
     * Efetua a exclusão dos registros conforme os parâmetros passados <br>
     * Se não for informado nenhuma coluna para o where todos os registros serão
     * excluidos <br>
     *
     * @param columnsWhere Colunas no formato "coluna1 = ? and coluna2 = ? and
     * coluna3 = ? and ... = ?"
     * @param valuesWhere argumentos do parâmetro where new String[]{"1", "2",
     * "texto asim", "..."}
     * @return quantidade de registros afetados
     * @throws DataAccessException
     */

    public int delete(String columnsWhere, Object valuesWhere) throws DataAccessException {
        return executeDelete(columnsWhere, valuesWhere);
    }

    public int deleteByPrimaryKey(Object key) throws DataAccessException {
        return executeDeleteByPrimaryKey(key);
    }

    /**
     * <p>
     * Efetua a exclusão de todos os registros</p>
     * Igual a delete from tabela
     *
     * @return Quantidade de registros excluídos
     * @throws DataAccessException
     */
    public int delete() throws DataAccessException {
        return executeDelete(null, null);
    }

    public int delete(Criteria criteria) throws DataAccessException {
        return executeDelete(criteria.toString(), criteria.getArgsArray());
    }


    public int delete(List<T> entidades) throws DataAccessException {
        if(entidades == null || entidades.size() == 0)
            return 0;

        Criteria criteria = new Criteria();
        for (T item : entidades) {
            criteria.expr("id", Criteria.Op.EQ, item.getId()).or();
        }

        return delete(criteria);
    }


    public int delete(T entidade) throws DataAccessException {
        if(entidade == null)
            return 0;

        Criteria criteria = new Criteria();
        criteria.expr("id", Criteria.Op.EQ, entidade.getId());

        return delete(criteria);

    }

    public long count(String columnsWhere, Object valuesWhere) throws DataAccessException {
        return executeCount(columnsWhere, valuesWhere);
    }

    public long count(Criteria criteria) throws DataAccessException {
        return executeCount(criteria.toString(), criteria.getArgsArray());
    }

    /**
     * Retorna a quantidade de registros da tabela <br>
     *
     * @return Quantidade de registros na tabela
     * @throws DataAccessException
     */
    public int count() throws DataAccessException {
        return (int) executeCount(null, null);
    }

    public Object getEscalar(String query, Object valuesWhere) throws DataAccessException {
        return executeEscalar(query, valuesWhere);
    }

    public Object getEscalar(String query) throws DataAccessException {
        return executeEscalar(query, null);
    }

    public int update(T entidade) throws DataAccessException {
        int effecteds;

        effecteds = executeUpdate(entidade);

        return effecteds;
    }

    public void update(String sqlUpdate) throws DataAccessException {

        executeUpdate(sqlUpdate);
    }

    /**
     * Efetua a atualização da lista passada por parâmetro <br> a atualização
     * será efetuada pela chave primária
     *
     * @param entidades Todas as entidades que devem serem alteradas
     * @return quantidade de registros modificados
     * @throws DataAccessException
     */
    public int update(List<T> entidades) throws DataAccessException {
        int effecteds;

        effecteds = executeUpdate(entidades);

        return effecteds;
    }

    public void insertOrUpdate(T entidades) throws DataAccessException {
        executeInsertOrUpdate(entidades);
    }

    public void insertOrUpdate(List<T> entidades) throws DataAccessException {
        executeInsertOrUpdate(entidades);
    }

    public void insertOrUpdate(T[] entidades) throws DataAccessException {
        executeInsertOrUpdate(Arrays.asList(entidades));
    }

    /**
     *
     * @param columnsWhere Colunas no formato "coluna1 = ? and coluna2 = ? and
     * coluna3 = ? and ... = ?"
     * @param valuesWhere argumentos do parâmetro where new String[]{"1", "2",
     * "texto", "..."}
     * @param groupBy somente o nome das colunas "coluna1, coluna2"
     * @param having
     * @param orderBy
     * @return Lista com todos os registros encontrados
     * @throws DataAccessException
     */
    public List<T> find(String columnsWhere, Object valuesWhere, String groupBy, String having, String orderBy) throws DataAccessException {
        List<T> result;

        result = executeFind(columnsWhere, valuesWhere, groupBy, having, orderBy, -1, -1);

        return result;
    }

    public List<T> find(Criteria criteria) throws DataAccessException {
        List<T> result;

        String strCriteria = criteria.toString();

        result = executeFind(strCriteria, criteria.getArgsArray(), criteria.getGroupBy(), null, criteria.getOrderBy(), criteria.getLimitStart(), criteria.getLimitEnd());

        return result;
    }


    /**
     *
     * @param columnsWhere Colunas no formato"coluna1 = ? and coluna2 = ? and
     * coluna3 = ? and ... = ?"
     * @param valuesWhere argumentos do parâmetro where new String[]{"1", "2",
     * "texto asim", "..."}
     * @param groupBy somente o nome das colunas "coluna1, coluna2"
     * @param having
     * @param orderBy
     * @param limitStart retorna os registros a partir da linha estipulada
     * @param limitTo
     * @return
     * @throws DataAccessException
     */
    public List<T> find(String columnsWhere, Object valuesWhere, String groupBy, String having, String orderBy, int limitStart, int limitTo) throws DataAccessException {

        List<T> result;

        result = executeFind(columnsWhere, valuesWhere, groupBy, having, orderBy, limitStart, limitTo);

        return result;
    }

    private T executeFindFirst(String columnsWhere, Object valuesWhere, String groupBy, String having, String orderBy) throws DataAccessException {
        T result = null;
        List<T> allResult;

        allResult = executeFind(columnsWhere, valuesWhere, groupBy, having, orderBy, 0, 1);
        if (allResult != null) {
            result = allResult.get(0);
        }

        return result;
    }

    public T findFirst(Criteria criterio) throws DataAccessException {
        return executeFindFirst(criterio.toString(), criterio.getArgsArray(), criterio.getGroupBy(), null, criterio.getOrderBy());
    }

    public T findFirst(String columnsWhere, Object valuesWhere) throws DataAccessException {
        return executeFindFirst(columnsWhere, valuesWhere, null, null, null);
    }

    public T findFirst() throws DataAccessException {
        return executeFindFirst(null, null, null, null, null);
    }

    public T findFirst(Criteria criterio, String orderBy) throws DataAccessException {
        return executeFindFirst(criterio.toString(), criterio.getArgsArray(), criterio.getGroupBy(), null, orderBy);
    }

    public T findFirst(String columnsWhere, Object valuesWhere, String orderBy) throws DataAccessException {
        return executeFindFirst(columnsWhere, valuesWhere, null, null, orderBy);
    }

    public T findFirst(String orderBy) throws DataAccessException {
        return executeFindFirst(null, null, null, null, orderBy);
    }


    public List<T> find(String columnsWhere, Object valueWhere) throws DataAccessException {
        List<T> result;

        result = executeFind(columnsWhere, valueWhere, null, null, null);

        return result;
    }

    public List<T> find(String columnsWhere, Object valueWhere, String orderBy) throws DataAccessException {
        List<T> result;

        result = executeFind(columnsWhere, valueWhere, null, null, orderBy);

        return result;
    }


    public List<T> find(String columnsWhere) throws DataAccessException {
        List<T> result;

        result = executeFind(columnsWhere, null, null, null, null);

        return result;
    }

    public List<T> find(String columnsWhere, String orderBy) throws DataAccessException {
        List<T> result;

        result = executeFind(columnsWhere, null, null, null, orderBy);

        return result;
    }





    /**
     *
     * @return @throws DataAccessException
     */
    public List<T> findAll() throws DataAccessException {
        List<T> result;

        result = executeFind(null, null, null);

        return result;
    }

    public List<T> findAll(String orderBy) throws DataAccessException {
        List<T> result;

        result = executeFind(null, null, null, null, orderBy);

        return result;
    }

    /**
     * Efetua a busca pela chave primaria defenida no mapeamento A ordem dos
     * parâmetros deve seguir a mesma ordem do mapeamento Somente enviar por
     * parametro int, String, int[] ou String[]
     *
     * @param key
     * @return T entidade selecionada
     * @throws DataAccessException
     */
    public T findByPrimaryKey(Object key) throws DataAccessException {
        T result;

        result = executeFindByPrimaryKey(key);

        return result;
    }

    public List<T> findBySql(String sql, Object valuesWhere) throws DataAccessException {
        List<T> result;

        result = executeFindBySql(sql, valuesWhere);

        return result;
    }



    public List<T> findBySql(String sql, Criteria criteria) throws DataAccessException {
        List<T> result;


        result = executeFindBySql(sql, criteria.getArgsArray());

        return result;
    }


    public List<T> findBySql(String sql, Criteria criteria, boolean addCriteria) throws DataAccessException {
        List<T> result;

        if(addCriteria) {
            sql = String.format("%s %s", sql, criteria.getSQLString());
        }

        result = executeFindBySql(sql, criteria.getArgsArray());

        return result;
    }

    private int executeDeleteByPrimaryKey(Object key) throws DataAccessException {
        int result;
        String[] values;

        values = buildValuesToString(key);

        if (values.length != primaryKeyColumns.size()) {
            throw new DataAccessException("Quantidade de valores difere da quantidade de colunas da chave primária");
        }

        result = executeDelete(getSqlFormatWhere(primaryKeyColumns), values);

        return result;

    }

    private T executeFindByPrimaryKey(Object key) throws DataAccessException {
        List<T> result;
        String[] values;

        values = buildValuesToString(key);

        if (values.length != primaryKeyColumns.size()) {
            throw new DataAccessException("Quantidade de valores difere da quantidade de colunas da chave primária");
        }


        result = executeFind(getSqlFormatWhere(primaryKeyColumns), values, null);

        if (result == null)
            return null;


        if (result.size() > 1)
            throw new DataAccessException("A consulta retornou mais de uma entidade");

        return result.get(0);
    }

    private String[] buildValuesToString(Object values) {
        //String[] result = null;

        if(values == null)
            return new String[]{};

        if(values.getClass().isPrimitive() || values instanceof String || values instanceof Integer ||
                                              values instanceof  Long  || values instanceof Double) {

            return new String[] { String.valueOf(values) };
        }

        if(values.getClass().isArray()) {
            Object[] allValues = (Object[])values;

            String[] result = new String[allValues.length];
            for (int i = 0; i<allValues.length; i++) {
                result[i] = String.valueOf(allValues[i]);
            }
            return result;
        }

        return new String[]{};
    }

    public void insert(T entidade) throws DataAccessException {
        executeInsert(entidade);
    }



    public void insert(List<T> entidades) throws DataAccessException {
        executeInsert(entidades);
    }


    public void insert(T[] entidades) throws DataAccessException {
        executeInsert(Arrays.asList(entidades));
    }


    private long executeCount(String columnsWhere, Object valuesWhere) throws DataAccessException {
        try {

            long count = session.queryNumEntries(tableName, columnsWhere, buildValuesToString(valuesWhere));
            return count;
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

    public Object executeEscalar(String query, Object valuesWhere) throws DataAccessException {

        Cursor cursor = null;

        try {
            cursor = session.rawQuery(query, buildValuesToString(valuesWhere));

            Object value = null;
            if (cursor.moveToFirst()) {
                switch (cursor.getType(0)) {
                    case Cursor.FIELD_TYPE_NULL:
                        break;
                    case Cursor.FIELD_TYPE_BLOB:
                        value = cursor.getBlob(0);
                        break;
                    case Cursor.FIELD_TYPE_FLOAT:
                        value = cursor.getFloat(0);
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        value = cursor.getInt(0);
                        break;
                    case Cursor.FIELD_TYPE_STRING:
                        value = cursor.getString(0);
                        break;
                }
            }
            return value;
        } catch (SQLException e) {
            throw new DataAccessException("Erro na execução do comando SQL!\n" + e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private int executeDelete(String columnsWhere, Object valuesWhere) throws DataAccessException {
        try {

            int affected = session.delete(tableName, columnsWhere, buildValuesToString(valuesWhere));
            return affected;
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao executeTask a exclusão dos registros!\n\n" + e.getMessage(), e);
        }
    }

    private String buildLimit(int limitStart, int limitTo) {
        String sqlFormat = null;
        if (limitStart > -1) {
            sqlFormat = String.valueOf(limitStart);
            if (limitTo > -1) {
                sqlFormat += "," + limitTo;
            }
        }

        return sqlFormat;

    }

    private List<T> executeFind(String columnsWhere, Object valuesWhere, String groupBy, String having, String orderBy, int limitStart, int limitTo) throws DataAccessException {
        try {
            Cursor cursor = session.query(tableName, allColumnNames, columnsWhere, buildValuesToString(valuesWhere), groupBy, having, orderBy, buildLimit(limitStart, limitTo));
            return populateClass(cursor);
        } catch (DataAccessException ex) {
            throw ex;
        } catch (SQLException ex) {
            throw new DataAccessException("Erro ao executeTask a consulta!\n" + ex.getMessage(), ex);
        }
    }

    private List<T> executeFind(String columnsWhere, Object valuesWhere, String orderBy) throws DataAccessException {
        try {
            Cursor cursor = session.query(tableName, allColumnNames, columnsWhere, buildValuesToString(valuesWhere), null, null, orderBy, null);
            return populateClass(cursor);
        } catch (DataAccessException ex) {
            throw ex;
        } catch (SQLException ex) {
            throw new DataAccessException("Erro ao executeTask a consulta!\n" + ex.getMessage(), ex);
        }
    }


    private List<T> executeFind(String columnsWhere, Object valuesWhere, String groupBy, String having, String orderBy) throws DataAccessException {
        try {
            Cursor cursor = session.query(tableName, allColumnNames, columnsWhere, buildValuesToString(valuesWhere), groupBy, having, orderBy, null);
            return populateClass(cursor);
        } catch (DataAccessException ex) {
            throw ex;
        } catch (SQLException ex) {
            throw new DataAccessException("Erro ao executeTask a consulta!\n" + ex.getMessage(), ex);
        }
    }

    private List<T> executeFindBySql(String sql, Object valuesWhere) throws DataAccessException {

        try {
            Cursor cursor = session.rawQuery(sql, buildValuesToString(valuesWhere));
            return populateClass(cursor);
        } catch (DataAccessException ex) {
            throw ex;
        } catch (SQLException ex) {
            throw new DataAccessException("Erro ao executeTask a consulta!\n" + ex.getMessage(), ex);
        }
    }

    private List<T> populateClass(Cursor cursor) throws DataAccessException {
        List<T> registers = null;
        if (cursor != null && cursor.moveToFirst()) {
            T register;
            registers = new ArrayList<>(cursor.getCount());

            do {

                try {
                    register = tableTypeClass.newInstance();

                } catch (IllegalAccessException ex) {
                    throw new DataAccessException(String.format("Instância do tipo %s não pode ser inicializada pois não existe um construtor válido!", tableTypeClass), ex);
                } catch (InstantiationException ex) {
                    throw new DataAccessException(String.format("Instância do tipo %s não pode ser inicializada!", tableTypeClass), ex);
                }

                for (Column column : allColumns) {

                    int columnIndex = cursor.getColumnIndexOrThrow(column.getColumnName());

                    int columnCursorType = cursor.getType(columnIndex);

                    String columnPropRef = column.getColumnPropRef();

                    /*
                     0 -> null
                     1 -> integer
                     2 -> Float
                     3 -> String
                     4 -> Blob
                     */
                    switch (columnCursorType) {
                        case 0:
                            break;
                        case 1: {
                            int value = cursor.getInt(columnIndex);
                            setValueInt(register, value, columnPropRef);
                            break;
                        }
                        case 2: {
                            double value = cursor.getDouble(columnIndex);

                            if(Double.isInfinite(value) || Double.isNaN(value))
                                value = 0;

                            setValueDouble(register, value, columnPropRef);
                            break;
                        }
                        case 3: {
                            String value = cursor.getString(columnIndex);
                            if (column.getColumnJavaType().equals(Date.class)) {
                                setValueDate(register, value, columnPropRef);
                            } else {
                                setValueString(register, value, columnPropRef);
                            }
                            break;
                        }
                        case 4: {
                            byte[] value = cursor.getBlob(columnIndex);
                            setValueByte(register, value, columnPropRef);
                            break;
                        }
                    }
                }

                registers.add(register);

            } while (cursor.moveToNext());

            cursor.close();

        }
        return registers;
    }

    private int executeUpdate(T entidade) throws DataAccessException {
        try {

            ContentValues values = buildContentValues(entidade, false, true, false);
            ContentValues valuesWhereArgs = buildContentValues(entidade, true, false, true);

            int affecteds;

            String[] valuesArgs = new String[valuesWhereArgs.size()];

            for (int i = 0, j = this.primaryKeyColumns.size(); i < j; i++) {
                valuesArgs[i] = valuesWhereArgs.get(primaryKeyColumns.get(i).getColumnName()).toString();
            }

            affecteds = session.update(tableName, values, getSqlFormatWhere(primaryKeyColumns), valuesArgs);

            return affecteds;
        } catch (DataAccessException ex) {
            throw ex;
        } catch (SQLException ex) {
            throw new DataAccessException("Erro ao executeTask a alteração do registro!\n" + ex.getMessage(), ex);
        }
    }

    private void executeUpdate(String sqlUpdate) throws DataAccessException {
        try {
            session.execSQL(sqlUpdate);
        } catch (DataAccessException ex) {
            throw ex;
        } catch (SQLException ex) {
            throw new DataAccessException("Erro ao executeTask o update nos registros!\n" + ex.getMessage(), ex);
        }
    }




    private int executeUpdate(List<T> entidades) throws DataAccessException {

        try {
            int affecteds = 0;

            for (T entidade : entidades) {
                ContentValues values = buildContentValues(entidade, false, true, false);
                ContentValues valuesWhereArgs = buildContentValues(entidade, true, false, true);

                String[] valuesArgs = new String[valuesWhereArgs.size()];

                for (int i = 0, j = this.primaryKeyColumns.size(); i < j; i++) {
                    valuesArgs[i] = valuesWhereArgs.get(primaryKeyColumns.get(i).getColumnName()).toString();
                }

                affecteds += session.update(tableName, values, getSqlFormatWhere(primaryKeyColumns), valuesArgs);
            }

            return affecteds;

        } catch (DataAccessException ex) {
            throw ex;
        } catch (SQLException ex) {
            throw new DataAccessException("Erro ao executeTask a alteração dos registros!\n" + ex.getMessage(), ex);
        }
    }




    private void executeInsert(T entidade) throws DataAccessException {
        ContentValues values;

        try {
            values = buildContentValues(entidade);
            long id = session.insertOrThrow(tableName, null, values);
            if(id > 0)
                entidade.setId(id);

            if(entidade.getId() == 0)
                entidade.setId(getLastId());

        } catch (DataAccessException ex) {
            throw ex;
        } catch (SQLException ex) {
            throw new DataAccessException("Erro ao executeTask a inclusão do registros!\n" + ex.getMessage(), ex);
        }
    }


    private void executeInsert(final List<T> entidades) throws DataAccessException {

        session.executeWithScopeTransactionNonExclusive(new CodeScope() {
            @Override
            public void execute() throws Exception {
                ContentValues values;

                try {
                    long id;
                    for (T entidade : entidades) {
                        values = buildContentValues(entidade, true, true, false);
                        id = session.insertOrThrow(tableName, null, values);
                        if(id > 0)
                            entidade.setId(id);

                        if(entidade.getId() == 0)
                            entidade.setId(getLastId());

                        values = null;
                    }
                } catch (DataAccessException ex) {
                    throw ex;
                } catch (SQLException ex) {
                    throw new DataAccessException("Erro ao executeTask a inclusão dos registros!\n" + ex.getMessage(), ex);
                }
            }

            @Override
            public void onSuccess() {

            }
        });

    }


    private void executeInsertOrUpdate(T entidade) throws DataAccessException {
        ContentValues values;

        try {
            values = buildContentValues(entidade);
            long id = session.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);

            if(id > 0)
                entidade.setId(id);

            if(entidade.getId() == 0)
                entidade.setId(getLastId());

        } catch (DataAccessException ex) {
            throw ex;
        } catch (SQLException ex) {
            throw new DataAccessException("Erro ao executeTask a inclusão do registros!\n" + ex.getMessage(), ex);
        }
    }


    private long getLastId() {
        return (int)executeEscalar(String.format("select (case when min(id) < 0 then min(id) else 0 end) from %s", tableName), null);
    }

    private String getInsertCommand() {

        String columns = "";
        String values = "";
        for (int i = 0; i < allColumnNames.length; i++) {
            columns += allColumnNames[i];
            if (i + 1 < allColumnNames.length) {
                columns += ",";
            }
        }
        for (int i = 0; i < allColumnNames.length; i++) {
            values += "?";
            if (i + 1 < allColumnNames.length) {
                values += ",";
            }
        }

        String sql = String.format("insert into %s (%s) values (%s)", tableName, columns, values);

        return sql;
    }

    private String[] getValuesOfContentValues(ContentValues cv) {
        String[] values = new String[allColumnNames.length];
        for (int i = 0; i < allColumnNames.length; i++) {
            values[i] = cv.getAsString(allColumnNames[i]);
        }

        return values;
    }

    private void executeInsertOrUpdate(final List<T> entidades) throws DataAccessException {

        final List<ContentValues> contentValues = new ArrayList<>(entidades.size());

        for (T entidade : entidades) {
            contentValues.add(buildContentValues(entidade));
        }

        session.executeWithScopeTransactionNonExclusive(new CodeScope() {
            @Override
            public void execute() throws Exception {
                for (int i = 0, size = contentValues.size(); i < size; i++) {
                    try {
                        long id;
                        id = session.insertWithOnConflict(tableName, null, contentValues.get(i), SQLiteDatabase.CONFLICT_REPLACE);
                        if (id > 0)
                            entidades.get(i).setId(id);

                        if (entidades.get(i).getId() == 0)
                            entidades.get(i).setId(getLastId());


                    } catch (DataAccessException ex) {
                        throw ex;
                    } catch (SQLException ex) {
                        throw new DataAccessException("Erro ao executeTask a inclusão dos registros!\n" + ex.getMessage(), ex);
                    }
                }
            }

            @Override
            public void onSuccess() {

            }
        });
    }


    private ContentValues buildContentValues(T entidade, boolean includePKs, boolean includeSingleColumns, boolean includeAutoIncrement) throws DataAccessException {
        if (includePKs && includeSingleColumns && includeAutoIncrement)
            return createContentValues(entidade, this.allColumns);

        if (includePKs && includeSingleColumns)
            return createContentValues(entidade, this.allColumnsLessAutoIncrement);

        if (includeSingleColumns)
            return createContentValues(entidade, this.singleColumns);

        if (includePKs)
            return createContentValues(entidade, this.primaryKeyColumns);

        return null;
    }

    private ContentValues buildContentValues(T entidade) throws DataAccessException {
        if(entidade != null && entidade.getId() > 0)
            return buildContentValues(entidade, true, true, true);

        return buildContentValues(entidade, true, true, false);
    }

    private ContentValues createContentValues(T entidade, List<Column> columns) throws DataAccessException {
        ContentValues content = new ContentValues();
        for (Column column : columns) {
            Type type = column.getColumnJavaType();
            String propertyRef = column.getColumnPropRef();
            String columnName = column.getColumnName();
            if (type.equals(int.class))  {
                    content.put(columnName, getValueInt(entidade, propertyRef));
            } else if (column.getColumnJavaType().equals(String.class)) {
                content.put(columnName, getValueString(entidade, propertyRef));
            } else if (column.getColumnJavaType().equals(Date.class)) {
                content.put(columnName, getValueDate(entidade, propertyRef));
            } else if (column.getColumnJavaType().equals(BigDecimal.class)) {
                content.put(columnName, getValueBigDecimal(entidade, propertyRef).toString());
            } else if (column.getColumnJavaType().equals(Double.class) || column.getColumnJavaType().equals(double.class)) {
                Double value = getValueDouble(entidade, propertyRef);
                if(value != null && (Double.isInfinite(value) || Double.isNaN(value)))
                    value = .0;
                content.put(columnName, value);
            } else if (column.getColumnJavaType().equals(Float.class) || column.getColumnJavaType().equals(float.class)) {
                Float value = getValueFloat(entidade, propertyRef);

                if(value != null && (Float.isInfinite(value) || Float.isNaN(value)))
                    value = .0f;

                content.put(columnName, value);

            } else if (column.getColumnJavaType().equals(Boolean.class) || column.getColumnJavaType().equals(boolean.class)) {
                content.put(columnName, getValueBoolean(entidade, propertyRef));
            } else if (column.getColumnJavaType().equals(Byte.class) || column.getColumnJavaType().equals(byte.class)) {
                content.put(columnName, getValueByte(entidade, propertyRef));
            } else if (column.getColumnJavaType().equals(Byte[].class) || column.getColumnJavaType().equals(byte[].class)) {
                content.put(columnName, getValuebyte(entidade, propertyRef));
            } else if (column.getColumnJavaType().equals(Long.class) || column.getColumnJavaType().equals(long.class)) {
                content.put(columnName, getValueLong(entidade, propertyRef));
            } else if (column.getColumnJavaType().equals(Short.class) || column.getColumnJavaType().equals(short.class)) {
                content.put(columnName, getValueShort(entidade, propertyRef));
            }
        }

        return content;
    }

    private Field getField(String columnPropRef) throws DataAccessException {
        Field field = null;
        for (Field search : columnFields) {
            if (search.getName().equalsIgnoreCase(columnPropRef)) {
                field = search;
                break;
            }
        }
        if (field == null) {
            throw new DataAccessException(String.format("Coluna %s da tabela %s não foi localizada!\nVerifique o mapeamento.", columnPropRef, tableName));
        }

        return field;
    }

    private Object getValue(T entidade, String columnPropRef) throws DataAccessException {

        if (entidade == null) {
            throw new NullPointerException(String.format("A entidade %s não pode ser nulla!", tableTypeClass));
        }

        Object value = null;
        Field field;

        field = getField(columnPropRef);

        try {
            value = field.get(entidade);
        } catch (IllegalArgumentException e) {
            throw new DataAccessException(String.format("O tipo %s não contém a propriedade %s!", entidade.getClass(), field.getName()), e);
        } catch (IllegalAccessException e) {
            throw new DataAccessException(String.format("Tentativa de acesso a propriedade %s da entidade %s negada!", field.getName(), entidade.getClass()), e);
        }

        return value;
    }

    private void setValue(T entidade, Object value, String columnPropRef) throws DataAccessException {
        Field field = getField(columnPropRef);
        if (field.getType() == Boolean.class || field.getType() == boolean.class) {
            try {
                field.set(entidade, NumberHelper.convertToBoolean(value.toString()));
            } catch (IllegalArgumentException e) {
                throw new DataAccessException(String.format("O tipo %s não contém a propriedade %s!", entidade.getClass(), field.getName()), e);
            } catch (IllegalAccessException e) {
                throw new DataAccessException(String.format("Tentativa de acesso a propriedade %s da entidade %s negada!", field.getName(), entidade.getClass()), e);
            }
        } else {
            try {
                field.set(entidade, value);
            } catch (IllegalArgumentException e) {
                throw new DataAccessException(String.format("O tipo %s não contém a propriedade %s!", entidade.getClass(), field.getName()), e);
            } catch (IllegalAccessException e) {
                throw new DataAccessException(String.format("Tentativa de acesso a propriedade %s da entidade %s negada!", field.getName(), entidade.getClass()), e);
            }
        }
    }

    private String getSqlFormatWhere(List<Column> columns) {
        String sql = "";

        for (int i = 0, j = columns.size(); i < j; i++) {
            sql += columns.get(i).GetSqlFormat();
            if (i + 1 != j) {
                sql += " and ";
            }
        }

        return sql;
    }

    private int getValueInt(T entidade, String columnPropRef) throws DataAccessException {
        return ((Integer) getValue(entidade, columnPropRef));
    }

    private String getValueString(T entidade, String columnPropRef) throws DataAccessException {
        Object value = getValue(entidade, columnPropRef);

        if (value == null) {
            return "";
        }
        return value.toString();
    }

    private Double getValueDouble(T entidade, String columnPropRef) throws DataAccessException {
        return ((Double) getValue(entidade, columnPropRef));
    }

    private Float getValueFloat(T entidade, String columnPropRef) throws DataAccessException {
        return ((Float) getValue(entidade, columnPropRef));
    }

    private Boolean getValueBoolean(T entidade, String columnPropRef) throws DataAccessException {
        return (Boolean) getValue(entidade, columnPropRef);
    }

    private Byte getValueByte(T entidade, String columnPropRef) throws DataAccessException {
        return (Byte) getValue(entidade, columnPropRef);
    }

    private byte[] getValuebyte(T entidade, String columnPropRef) throws DataAccessException {
        return (byte[]) getValue(entidade, columnPropRef);
    }

    private Long getValueLong(T entidade, String columnPropRef) throws DataAccessException {
        return ((Long) getValue(entidade, columnPropRef));
    }

    private BigDecimal getValueBigDecimal(T entidade, String columnPropRef) throws DataAccessException {
        return ((BigDecimal) getValue(entidade, columnPropRef));
    }

    private Short getValueShort(T entidade, String columnPropRef) throws DataAccessException {
        return ((Short) getValue(entidade, columnPropRef));
    }


    private String getValueDate(T entidade, String columnPropRef) throws DataAccessException {
        String result = null;
        Object value = getValue(entidade, columnPropRef);
        if (value != null) {
            Date date;

            Column column = table.findColumn(columnPropRef);
            SimpleDateFormat toFullDate = null;
            if(column != null && column.isDateTime()) {
                toFullDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            } else {
                toFullDate = new SimpleDateFormat("yyyy-MM-dd");
            }

            try {
                date = (Date) value;
                result = toFullDate.format(date);
            } catch (Exception ex) {
                Log.e(KEY, ex.getMessage());
            }
        }

        return result;
    }

    private void setValueInt(T entidade, int value, String columnPropRef) throws DataAccessException {
        setValue(entidade, value, columnPropRef);
    }

    private void setValueDouble(T entidade, double value, String columnPropRef) throws DataAccessException {
        setValue(entidade, value, columnPropRef);
    }

    private void setValueString(T entidade, String value, String columnPropRef) throws DataAccessException {
        setValue(entidade, value, columnPropRef);
    }

    private void setValueByte(T entidade, byte[] value, String columnPropRef) throws DataAccessException {
        setValue(entidade, value, columnPropRef);
    }

    private void setValueBigDecimal(T entidade, BigDecimal value, String columnPropRef) throws DataAccessException {
        setValue(entidade, value, columnPropRef);
    }

    private void setValueDate(T entidade, String value, String columnPropRef) throws DataAccessException {
        Date date = null;
        SimpleDateFormat sdf;

        Column column = table.findColumn(columnPropRef);

        //value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);

        if (column != null && column.isDateTime()) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }


        if (value != null && !value.isEmpty()) {
            try {
                date = sdf.parse(value);
            } catch (ParseException ex) {
                Log.e(KEY, ex.getMessage());
            }
        }
        setValue(entidade, date, columnPropRef);
    }



    public void executeBulkInsert(List<T> entidades) throws DataAccessException {
        ContentValues values;

        for (T entidade : entidades) {
            values = buildContentValues(entidade);
            getValuesOfContentValues(values);
        }
    }
}