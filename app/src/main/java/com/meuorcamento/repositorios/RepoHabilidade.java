package com.meuorcamento.repositorios;
import android.content.Context;
import com.meuorcamento.model.Habilidade;

public class RepoHabilidade extends Repositorio<Habilidade> {


    public RepoHabilidade(Context context) {
        super(context, Habilidade.class);
    }

}
