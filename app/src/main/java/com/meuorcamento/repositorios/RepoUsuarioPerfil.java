package com.meuorcamento.repositorios;

import android.content.Context;
import com.meuorcamento.model.UsuarioPerfil;

public class RepoUsuarioPerfil extends Repositorio<UsuarioPerfil> {
    public RepoUsuarioPerfil(Context context) {
        super(context, UsuarioPerfil.class);
    }

}
