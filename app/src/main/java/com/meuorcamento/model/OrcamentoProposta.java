package com.meuorcamento.model;

import com.meuorcamento.data.ModelBase;

public class OrcamentoProposta extends ModelBase {

    private String uid;
    private OrcamentoPropostaInformacao orcamentoPropostaInformacao;


    public OrcamentoProposta() {
        orcamentoPropostaInformacao = new OrcamentoPropostaInformacao();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public OrcamentoPropostaInformacao getOrcamentoPropostaInformacao() {
        return orcamentoPropostaInformacao;
    }

    public void setOrcamentoPropostaInformacao(OrcamentoPropostaInformacao orcamentoPropostaInformacao) {
        this.orcamentoPropostaInformacao = orcamentoPropostaInformacao;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof  OrcamentoProposta)) {
            return false;
        }

        if(obj == null)
            return false;

        OrcamentoProposta orcamentoProposta = (OrcamentoProposta)obj;

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
