package com.meuorcamento.data.DBVersion;

import com.meuorcamento.data.DataAccessException;
public abstract class DBVersionFactory {

    public static IUpdateDBVersion getInstanceByVersionCode(int version) throws DataAccessException {

        IUpdateDBVersion dbVersion;
        switch (version) {
            case 1: {
                dbVersion = new DBVersion1();
                break;
            }
            default: {
                throw new DataAccessException("Não foi possível localizar a implementacao da versao do banco de dados!");
            }
        }
        return dbVersion;
    }
}
