package com.meuorcamento.model;

import com.meuorcamento.data.ModelBase;

public class Usuario extends ModelBase {

    private String email;


    @Override
    public int hashCode() {
        if(getKey() == null)
            return super.hashCode();

        return getKey().hashCode();
    }
}
