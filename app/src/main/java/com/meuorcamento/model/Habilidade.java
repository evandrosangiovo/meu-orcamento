package com.meuorcamento.model;

import com.meuorcamento.data.ModelBase;

public class Habilidade extends ModelBase {

    private String titulo;
    private String descricao;
    private String grupo;

    public Habilidade(){
    }


    public Habilidade(String titulo, String descricao, String grupo){
        this.titulo = titulo;
        this.descricao = descricao;
        this.grupo = grupo;
    }

    public Habilidade(String key, String titulo, String descricao, String grupo){
        this.key = key;
        this.titulo = titulo;
        this.descricao = descricao;
        this.grupo = grupo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof  Habilidade)) {
            return false;
        }

        if(obj == null)
            return false;

        Habilidade habilidade = (Habilidade)obj;

        if(habilidade.getKey() == this.getKey())
            return true;

        return false;

    }

    @Override
    public int hashCode() {
        if(getKey() == null)
            return super.hashCode();

        return getKey().hashCode();
    }
}
