package com.meuorcamento.repositorios;

import android.content.Context;
import com.meuorcamento.model.Usuario;

public class RepoUsuario extends Repositorio<Usuario> {


    public RepoUsuario(Context context) {
        super(context, Usuario.class);
    }
}
