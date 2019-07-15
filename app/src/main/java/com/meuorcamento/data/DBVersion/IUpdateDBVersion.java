package com.meuorcamento.data.DBVersion;

/**
 *
 * @author Administrador
 */
public interface IUpdateDBVersion {

        /**
         * Versão do banco de dados a ser atualizado
         *
         * @return Version Code
         */
        int getVersion();

        String[] fullCreateDBScript = new String[]{

                "create table usuarios (\n" +
                "       id integer not null primary key,       \n" +
                "       uid varchar not null,       \n" +
                "       email varchar not null,    \n" +
                "       excluido boolean default 0    \n" +
                ");",

                "create table usuariosPerfis (\n" +
                        "    id integer not null primary key,       \n" +
                        "    uid varchar,\n" +
                        "    email varchar,\n" +
                        "    nome varchar,\n" +
                        "    sobreNome varchar,\n" +
                        "    cpf varchar,\n" +
                        "    identidade varchar,\n" +
                        "    cep varchar,\n" +
                        "    cidade varchar,\n" +
                        "    uf varchar,\n" +
                        "    bairro varchar,\n" +
                        "    endereco varchar,\n" +
                        "    numeroEstabelecimento integer,\n" +
                        "    celular varchar,\n" +
                        "    skype varchar,\n" +
                        "    dataNascimento date,\n" +
                        "    urlAvatar varchar,    \n" +
                        "    excluido boolean default 0    \n" +
                 ");",

                "create table habilidades (\n" +
                        "    id integer not null primary key autoincrement,    \n" +
                        "    key varchar,    \n" +
                        "    titulo varchar,    \n" +
                        "    descricao varchar,    \n" +
                        "    grupo varchar,    \n" +
                        "    excluido boolean default 0    \n" +
                ");"
        };

        String[] getFullCreateDBScript();

        String[] getAlterTables();

        String[] getCreateTables();

        String[] getInsertRows();

        String[] getUpdateRows();




        String[] dropAllIndexes();

        String[] createAllIndexes();

        String[] createAllTriggers();

        String[] dropAllTriggers();

}
