package com.meuorcamento.data.mapping;

import java.util.ArrayList;
import java.util.List;

public final class TableBuilder {

    private TableBuilder() {

    }

    private static final List<Table> tables = new ArrayList<Table>();
    private static boolean finishedConfiguration = true;

    public static Table getTable(String className) {

        Table table = null;
        for (Table t : tables) {
            if (t.getTableRefClass().getName().equals(className)) {
                table = t;
                break;
            }
        }

        if (table == null) {
            throw new IllegalArgumentException("Classe " + className + " não encontrada no mapeamento");
        }

        return table;

    }

    private static void addNewTable(Table table) {
        if (table == null) {
            throw new IllegalArgumentException("table não pode ser null");
        }
        if (table.getTableName().isEmpty()) {
            throw new IllegalArgumentException("tableName não pode ser empty");
        }

        //if (tables.contains(table)) {
        //    throw new IllegalArgumentException("Tabela já existente na coleção\nTable: " + table.getTableName());
        //}

        tables.add(table);

    }

    public static Table newTable(String tableName, Class<?> classRef) {
        if (finishedConfiguration == false) {
            throw new IllegalStateException("Execute o método finishConfiguration antes de iniciar outro mapeamento");
        }

        finishedConfiguration = false;

        return new Table(tableName, classRef);
    }

    public static void finishConfiguration(Table table) {
        addNewTable(table);
        finishedConfiguration = true;
    }

}
