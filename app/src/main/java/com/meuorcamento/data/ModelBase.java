package com.meuorcamento.data;
import java.io.Serializable;

/**
 * Created by Administrador on 19/11/2014.
 */
public class ModelBase implements Serializable {

    protected long id;
    protected boolean excluido;
    protected String key;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isExcluido() {
        return excluido;
    }

    public void setExcluido(boolean excluido) {
        this.excluido = excluido;
    }
}
