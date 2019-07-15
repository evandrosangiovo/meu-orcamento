package com.meuorcamento.model;

import com.meuorcamento.data.ModelBase;

public class OrcamentoPropostaInformacao extends ModelBase {

    private String keyOrcamento;
    private double valorProposta;
    private String uid;
    private UsuarioPerfil usuarioPerfil;
    private String observacoes;

    public OrcamentoPropostaInformacao() {

    }

    public double getValorProposta() {
        return valorProposta;
    }

    public void setValorProposta(double valorProposta) {
        this.valorProposta = valorProposta;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKeyOrcamento() {
        return keyOrcamento;
    }

    public void setKeyOrcamento(String keyOrcamento) {
        this.keyOrcamento = keyOrcamento;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public UsuarioPerfil getUsuarioPerfil() {
        return usuarioPerfil;
    }

    public void setUsuarioPerfil(UsuarioPerfil usuarioPerfil) {
        this.usuarioPerfil = usuarioPerfil;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof OrcamentoPropostaInformacao)) {
            return false;
        }

        if(obj == null)
            return false;

        OrcamentoPropostaInformacao orcamentoProposta = (OrcamentoPropostaInformacao)obj;

        if(orcamentoProposta.getKey() == this.getKey())
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
