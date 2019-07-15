package com.meuorcamento.data.mapping;

import android.location.Criteria;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public final class Table {

    private String tableName;
    private Class<?> refClass;
    private List<Column> singleColumns;
    private List<Column> primaryKeysColumns;
    private List<Column> allColumns;
    private List<Column> allColumnsLessAutoIncrement;
    private Field[] fields;

    protected Table(String tableName, Class<?> classRef) {
        this.tableName = tableName;
        //this.refClass = classRef;
        this.refClass = classRef;
        this.singleColumns = new ArrayList<>();
        this.primaryKeysColumns = new ArrayList<>();
        this.allColumns = new ArrayList<>();
        this.allColumnsLessAutoIncrement = new ArrayList<>();
        //this.fields = classRef.getDeclaredFields();
        List<Field> listFields = getAllFields(classRef);
        this.fields = listFields.toArray(new Field[listFields.size()]);
        Field.setAccessible(fields, true);
    }

    private List<Field> getAllFields(Class clazz) {
        List<Field> fields = new ArrayList<Field>();

        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

        Class superClazz = clazz.getSuperclass();
        if(superClazz != null){
            fields.addAll( getAllFields(superClazz) );
        }

        return fields;
    }

    public Field[] getFields() {
        return fields;
    }

    public String getTableName() {
        return tableName;
    }

    public Class<?> getTableRefClass() {
        return refClass;
    }

    public List<Column> getAllColumns() {
        return allColumns;
    }

    public List<Column> getAllColumnsLessAutoIncrement() {
        return allColumnsLessAutoIncrement;
    }

    public List<Column> getPrimaryKeyColumns() {
        return primaryKeysColumns;
    }

    public List<Column> getSingleColumns() {
        return singleColumns;
    }

    public String[] getAllColumnNames() {
        return getColumnNames(true, true);
    }

    public String[] getSingleColumnNames() {
        return getColumnNames(false, true);
    }

    public String[] getPrimaryKeyColumnNames() {
        return getColumnNames(true, false);
    }

    private String[] getColumnNames(boolean primaryKeyColumns, boolean singleColumns) {

        List<String> columns = new ArrayList<String>();
        if (primaryKeyColumns) {
            for (Column column : this.primaryKeysColumns) {
                columns.add(column.getColumnName());
            }
        }
        if (singleColumns) {
            for (Column column : this.singleColumns) {
                columns.add(column.getColumnName());
            }
        }

        return columns.toArray(new String[columns.size()]);
    }


    public Column findColumn(String name) {
        for (Column column : allColumns ) {
            if(column.getColumnName().equalsIgnoreCase(name)) {
                return column;
            }
        }
        return  null;
    }

    /**
     * Adiciona uma coluna do tipo Integer <br>
     * Assume o tipo Integer no Banco de dados e int no Java<br>
     *
     * @param columnName Nome da coluna no banco de dados
     * @param propertyRef Nome da propriedade da classe java
     * @param isPrimaryKey É chave primária?
     */
    protected void addColumnInteger(String columnName, String propertyRef, boolean isPrimaryKey) {
        this.addColumn(columnName, Types.INTEGER, int.class, propertyRef, isPrimaryKey);
    }

    /**
     * Adiciona uma coluna do tipo Integer <br>
     * Assume o tipo Integer no Banco de dados e int no Java<br>
     *
     * @param columnName Nome da coluna no banco de dados
     * @param propertyRef Nome da propriedade da classe java
     * @param isPrimaryKey É chave primária?
     * @param isAutoIncrement Coluna é auto incremento?
     */
    protected void addColumnInteger(String columnName, String propertyRef, boolean isPrimaryKey, boolean isAutoIncrement) {
        this.addColumn(columnName, Types.INTEGER, int.class, propertyRef, isPrimaryKey, isAutoIncrement);
    }

    /**
     * Adiciona uma coluna do tipo Integer <br>
     * Assume o tipo Integer no Banco de dados e int no Java<br>
     * Assume que o nome da propriedade java é o mesmo nome da coluna no banco
     * de dados<br>
     *
     * @param columnName Nome da coluna no banco de dados
     * @param isPrimaryKey É chave primária?
     */
    protected void addColumnInteger(String columnName, boolean isPrimaryKey) {
        this.addColumn(columnName, Types.INTEGER, int.class, columnName, isPrimaryKey);
    }

    protected void addColumnLong(String columnName, boolean isPrimaryKey) {
        this.addColumn(columnName, Types.INTEGER, long.class, columnName, isPrimaryKey);
    }

    protected void addColumnLong(String columnName, String propertyRef) {
        this.addColumn(columnName, Types.INTEGER, long.class, propertyRef, false);
    }

    protected void addColumnLong(String columnName) {
        this.addColumn(columnName, Types.INTEGER, long.class, columnName, false);
    }

    protected void addColumnLong(String columnName, boolean isPrimaryKey, boolean isAutoIncrement) {
        this.addColumn(columnName, Types.INTEGER, long.class, columnName, isPrimaryKey, isAutoIncrement);
    }

    /**
     * Adiciona uma coluna do tipo Integer <br>
     * Assume o tipo Integer no Banco de dados e int no Java<br>
     *
     * @param columnName Nome da coluna no banco de dados
     * @param isPrimaryKey É chave primária?
     * @param isAutoIncrement Coluna é auto incremento?
     */
    protected void addColumnInteger(String columnName, boolean isPrimaryKey, boolean isAutoIncrement) {
        this.addColumn(columnName, Types.INTEGER, int.class, columnName, isPrimaryKey, isAutoIncrement);
    }

    /**
     * Adiciona uma coluna do tipo Integer <br>
     * Assume o tipo Integer no Banco de dados e int no Java<br>
     * Assume que o nome da propriedade java é o mesmo nome da coluna no banco
     * de dados<br>
     * Assume que a coluna NÃO é uma chave primária<br>
     *
     * @param columnName Nome da coluna no banco de dados
     */
    protected void addColumnInteger(String columnName) {
        this.addColumn(columnName, Types.INTEGER, int.class, columnName, false);
    }
    
    protected void addColumnEnum(String columnName) {
        this.addColumn(columnName, Types.INTEGER, Enum.class, columnName, false);
    }

    protected void addColumnBoolean(String columnName) {
        this.addColumn(columnName, Types.INTEGER, boolean.class, columnName, false);
    }

    /**
     * Adiciona uma coluna do tipo byete[] <br>
     * Assume o tipo blob no Banco de dados e byte[] no Java<br>
     * Assume que o nome da propriedade java é o mesmo nome da coluna no banco
     * de dados<br>
     * Assume que a coluna NÃO é uma chave primária<br>
     *
     * @param columnName Nome da coluna no banco de dados
     */
    protected void addColumnByte(String columnName) {
        this.addColumn(columnName, Types.BLOB, byte[].class, columnName, false);
    }

    /**
     * Adiciona uma coluna do tipo Varchar <br>
     * Assume o tipo Varchar no Banco de dados e String no Java<br>
     *
     * @param columnName Nome da coluna no banco de dados
     * @param propertyRef Nome da propriedade da classe java
     * @param isPrimaryKey É chave primária?
     */
    protected void addColumnVarchar(String columnName, String propertyRef, boolean isPrimaryKey) {
        this.addColumn(columnName, Types.VARCHAR, String.class, propertyRef, isPrimaryKey);
    }


    protected void addColumnVarchar(String columnName, String propertyRef) {
        this.addColumn(columnName, Types.VARCHAR, String.class, propertyRef, false);
    }

    /**
     * Adiciona uma coluna do tipo Varchar <br>
     * Assume o tipo Varcha no Banco de dados e String no Java<br>
     * Assume que o nome da propriedade java é o mesmo nome da coluna no banco
     * de dados<br>
     *
     * @param columnName Nome da coluna no banco de dados
     * @param isPrimaryKey É chave primária?
     */
    protected void addColumnVarchar(String columnName, boolean isPrimaryKey) {
        this.addColumn(columnName, Types.VARCHAR, String.class, columnName, isPrimaryKey);
    }

    /**
     * Adiciona uma coluna do tipo Varchar <br>
     * Assume o tipo Varchar no Banco de dados e String no Java<br>
     * Assume que o nome da propriedade java é o mesmo nome da coluna no banco
     * de dados<br>
     * Assume que a coluna NÃO é uma chave primária<br>
     *
     * @param columnName Nome da coluna no banco de dados
     */
    protected void addColumnVarchar(String columnName) {
        this.addColumn(columnName, Types.VARCHAR, String.class, columnName, false);
    }

    protected void addColumnDate(String columnName, String propertyRef, boolean isPrimaryKey) {
        this.addColumn(columnName, Types.VARCHAR, Date.class, propertyRef, isPrimaryKey);
    }

    protected void addColumnDate(String columnName, String propertyRef) {
        this.addColumn(columnName, Types.VARCHAR, Date.class, propertyRef, false);
    }

    protected void addColumnDate(String columnName) {
        this.addColumn(columnName, Types.VARCHAR, Date.class, columnName, false, false);
    }




    protected void addColumnDateTime(String columnName, String propertyRef, boolean isPrimaryKey) {
        this.addColumn(columnName, Types.VARCHAR, Date.class, propertyRef, isPrimaryKey, false, true);
    }

    protected void addColumnDateTime(String columnName, String propertyRef) {
        this.addColumn(columnName, Types.VARCHAR, Date.class, propertyRef, false, false, true);
    }

    protected void addColumnDateTime(String columnName) {
        this.addColumn(columnName, Types.VARCHAR, Date.class, columnName, false, false, true);
    }

    /**
     * Adiciona uma coluna do tipo Decimal <br>
     * Assume o tipo Decimal no Banco de dados e double no Java<br>
     *
     * @param columnName Nome da coluna no banco de dados
     * @param propertyRef Nome da propriedade da classe java
     * @param isPrimaryKey É chave primária?
     */
    protected void addColumnDecimal(String columnName, String propertyRef, boolean isPrimaryKey) {
        this.addColumn(columnName, Types.DECIMAL, double.class, propertyRef, isPrimaryKey);
    }

    /**
     * Adiciona uma coluna do tipo Decimal <br>
     * Assume o tipo Decimal no Banco de dados e double no Java<br>
     * Assume que o nome da propriedade java é o mesmo nome da coluna no banco
     * de dados<br>
     *
     * @param columnName Nome da coluna no banco de dados
     * @param isPrimaryKey É chave primária?
     */
    protected void addColumnDecimal(String columnName, boolean isPrimaryKey) {
        this.addColumn(columnName, Types.DECIMAL, double.class, columnName, isPrimaryKey);
    }

    /**
     * Adiciona uma coluna do tipo Decimal <br>
     * Assume o tipo Decimal no Banco de dados e double no Java<br>
     * Assume que o nome da propriedade java é o mesmo nome da coluna no banco
     * de dados<br>
     * Assume que a coluna NÃO é uma chave primária<br>
     *
     * @param columnName Nome da coluna no banco de dados
     */
    protected void addColumnDecimal(String columnName) {
        this.addColumn(columnName, Types.DECIMAL, double.class, columnName, false);
    }

    protected void addColumnDecimal(String columnName, String propertyRef) {
        this.addColumn(columnName, Types.DECIMAL, double.class, propertyRef, false);
    }

    private void executeAddColumn(String columnName, int sqlType, Type javaType, String propertyRef, boolean isPrimaryKey, boolean isAutoIncrement, boolean isDateTime) {
        if (columnName.isEmpty()) {
            throw new IllegalArgumentException("columnName não pode ser empty");
        }
        if (sqlType == 0) {
            throw new IllegalArgumentException("sqlType não pode ser 0");
        }
        if (javaType == null) {
            throw new IllegalArgumentException("javaType não pode ser null");
        }
        if (propertyRef.isEmpty()) {
            throw new IllegalArgumentException("propertyRef não pode ser empty");
        }

        Column column = new Column(this, columnName, sqlType, javaType, propertyRef, isPrimaryKey, isAutoIncrement, isDateTime);

        if (singleColumnExists(column)) {
            throw new IllegalArgumentException("Coluna já existente na coleção\nColumn: " + column.getColumnName());
        }

        if (isPrimaryKey) {
            addPrimaryKeyColumns(column);
        } else {
            addSingleColumns(column);
        }

    }



    private void addColumn(String columnName, int sqlType, Type javaType, String propertyRef, boolean isPrimaryKey) {

        executeAddColumn(columnName, sqlType, javaType, propertyRef, isPrimaryKey, false, false);
    }



    private void addColumn(String columnName, int sqlType, Type javaType, String propertyRef, boolean isPrimaryKey, boolean isAutoIncrement) {
        executeAddColumn(columnName, sqlType, javaType, propertyRef, isPrimaryKey, isAutoIncrement, false);
    }

    private void addColumn(String columnName, int sqlType, Type javaType, String propertyRef, boolean isPrimaryKey, boolean isAutoIncrement, boolean isDateTime) {
        executeAddColumn(columnName, sqlType, javaType, propertyRef, isPrimaryKey, isAutoIncrement, isDateTime);
    }

    /*
     public void setForeignKey()
     {
     this.Nova = new ForeignKey("FK_TESTE");
     }
	
	
     public void setForeignKeyReference(Column columnKey, Column columnForeignKey)
     {
     this.Nova.setForeignKeyMap(columnKey, columnForeignKey);
     }
     */
    private boolean singleColumnExists(Column column) {
        return this.allColumns.contains(column);
    }

    private void addSingleColumns(Column column) {
        this.allColumns.add(column);
        this.singleColumns.add(column);
        if(!column.isAutoIncrement())
            this.allColumnsLessAutoIncrement.add(column);
    }

    private void addPrimaryKeyColumns(Column column) {
        this.allColumns.add(column);
        this.primaryKeysColumns.add(column);
        if(!column.isAutoIncrement())
            this.allColumnsLessAutoIncrement.add(column);
    }

    @Override
    public int hashCode() {
        return this.tableName.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return this.hashCode() == ((Table) o).hashCode();
    }

}
