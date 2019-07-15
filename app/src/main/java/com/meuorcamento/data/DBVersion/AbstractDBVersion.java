
package com.meuorcamento.data.DBVersion;

/**
 *
 * @author Administrador
 */
abstract class AbstractDBVersion implements IUpdateDBVersion {


    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public String[] dropAllIndexes() {
        return new String[] {
        };
    }

    @Override
    public String[] createAllIndexes() {
        return new String[] {
        };
    }

    @Override
    public String[] createAllTriggers() {
        return new String[] {
                     
                };
    }

    @Override
    public String[] dropAllTriggers() {
        return new String[] {
        };
    }

}
