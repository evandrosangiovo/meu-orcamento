package com.meuorcamento.model;

import com.meuorcamento.data.ModelBase;

import java.util.Date;

public class OrcamentoAvaliacao extends ModelBase {

    private String uid;
    private String keyOrcamento;
    private UsuarioPerfil usuarioPerfil;
    private String observacoes;
    private float nota;
    private Date dataAvaliacao;

    public OrcamentoAvaliacao() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UsuarioPerfil getUsuarioPerfil() {
        if(usuarioPerfil == null)
            return new UsuarioPerfil();
        return usuarioPerfil;
    }

    public void setUsuarioPerfil(UsuarioPerfil usuarioPerfil) {
        this.usuarioPerfil = usuarioPerfil;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public String getKeyOrcamento() {
        return keyOrcamento;
    }

    public void setKeyOrcamento(String keyOrcamento) {
        this.keyOrcamento = keyOrcamento;
    }

    public Date getDataAvaliacao() {
        return dataAvaliacao;
    }

    public void setDataAvaliacao(Date dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }
}
