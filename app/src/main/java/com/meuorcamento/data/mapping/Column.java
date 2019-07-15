package com.meuorcamento.data.mapping;

import java.lang.reflect.Type;

public final class Column {

    private Table tableRef;
    private String columnName;
    private int columnSqlType;
    private Type columnJavaType;
    private String columnPropRef;
    private boolean isPrimaryKey;
    private boolean isAutoIncrement;
    private boolean isDateTime = false;


    /*
    protected Column(Table tableRef, String columnName, int columnSqlType,
            Type columnJavaType, String columnPropRef, boolean isPrimaryKey) {
        this.tableRef = tableRef;
        this.columnName = columnName;
        this.columnSqlType = columnSqlType;
        this.columnJavaType = columnJavaType;
        this.columnPropRef = columnPropRef;
        this.isPrimaryKey = isPrimaryKey;
        this.isAutoIncrement = false;
    }
    */

    public boolean isDateTime() {
        return isDateTime;
    }

    protected Column(Table tableRef, String columnName, int columnSqlType,
            Type columnJavaType, String columnPropRef, boolean isPrimaryKey, boolean isAutoIncrement, boolean isDateTime) {
        this.tableRef = tableRef;
        this.columnName = columnName;
        this.columnSqlType = columnSqlType;
        this.columnJavaType = columnJavaType;
        this.columnPropRef = columnPropRef;
        this.isPrimaryKey = isPrimaryKey;
        this.isAutoIncrement = isAutoIncrement;
        this.isDateTime = isDateTime;
    }


    protected Column(Table tableRef, String columnName, int columnSqlType,
                     Type columnJavaType, String columnPropRef, boolean isPrimaryKey, boolean isAutoIncrement) {
        this.tableRef = tableRef;
        this.columnName = columnName;
        this.columnSqlType = columnSqlType;
        this.columnJavaType = columnJavaType;
        this.columnPropRef = columnPropRef;
        this.isPrimaryKey = isPrimaryKey;
        this.isAutoIncrement = isAutoIncrement;
    }

    public String GetSqlFormat() {
        if (this.columnJavaType.equals(String.class)) {
            return this.columnName + " like ?";
        }
        return this.columnName + " = ?";
    }

    /*
    protected Table getTableRef() {
        return tableRef;
    }
    */

    public String getColumnName() {
        return columnName;
    }

    /*
    protected int getColumnSqlType() {
        return columnSqlType;
    }
    */

    public Type getColumnJavaType() {
        return columnJavaType;
    }

    public String getColumnPropRef() {
        return columnPropRef;
    }

    //protected boolean isPrimaryKey() {
    //    return isPrimaryKey;
    //}

    @Override
    public int hashCode() {
        return this.columnName.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Column && this.hashCode() == o.hashCode();
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

}
