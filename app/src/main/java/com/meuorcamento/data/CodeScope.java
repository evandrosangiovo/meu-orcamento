package com.meuorcamento.data;

/**
 * Created by Administrador on 15/12/2015.
 */
public interface CodeScope {

    void execute() throws Exception;
    void onSuccess();
    //void onRollback(Throwable exception);

}
