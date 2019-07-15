package com.meuorcamento.data.DBVersion;

/**
 *
 * @author Administrador
 */
public class DBVersion1 extends AbstractDBVersion {

    public int getVersion() {
        return 1;
    }

    public String[] getFullCreateDBScript() {
        return fullCreateDBScript;
    }

    public String[] getAlterTables() {
        return new String[]{};
    }

    public String[] getCreateTables() {
        return new String[]{};
    }

    public String[] getInsertRows() {
        return new String[]{};
            
    }

    public String[] getUpdateRows() {
        return new String[]{};
    }

}
