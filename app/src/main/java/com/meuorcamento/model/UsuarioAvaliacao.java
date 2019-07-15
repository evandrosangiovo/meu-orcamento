package com.meuorcamento.model;

import com.meuorcamento.data.ModelBase;

import java.util.Date;

public class UsuarioAvaliacao extends ModelBase {

    private Date dataAvaliacao;
    private String mensagem;
    private UsuarioPerfil usuarioAvaliador;
    private UsuarioPerfil usuarioAvaliado;
    private String uidUsuarioAvaliador;
    private String uidUsuarioAvaliado;
    private int avaliacao;
    private String keyOrcamento;


    public Date getDataAvaliacao() {
        return dataAvaliacao;
    }

    public void setDataAvaliacao(Date dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public UsuarioPerfil getUsuarioAvaliador() {
        return usuarioAvaliador;
    }

    public void setUsuarioAvaliador(UsuarioPerfil usuarioAvaliador) {
        this.usuarioAvaliador = usuarioAvaliador;
    }

    public UsuarioPerfil getUsuarioAvaliado() {
        return usuarioAvaliado;
    }

    public void setUsuarioAvaliado(UsuarioPerfil usuarioAvaliado) {
        this.usuarioAvaliado = usuarioAvaliado;
    }

    public String getUidUsuarioAvaliador() {
        return uidUsuarioAvaliador;
    }

    public void setUidUsuarioAvaliador(String uidUsuarioAvaliador) {
        this.uidUsuarioAvaliador = uidUsuarioAvaliador;
    }

    public String getUidUsuarioAvaliado() {
        return uidUsuarioAvaliado;
    }

    public void setUidUsuarioAvaliado(String uidUsuarioAvaliado) {
        this.uidUsuarioAvaliado = uidUsuarioAvaliado;
    }

    public int getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(int avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getKeyOrcamento() {
        return keyOrcamento;
    }

    public void setKeyOrcamento(String keyOrcamento) {
        this.keyOrcamento = keyOrcamento;
    }



    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof  UsuarioAvaliacao)) {
            return false;
        }

        if(obj == null)
            return false;

        UsuarioAvaliacao usuarioAvaliacao = (UsuarioAvaliacao)obj;

        if(usuarioAvaliacao.getKey() == this.getKey())
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
