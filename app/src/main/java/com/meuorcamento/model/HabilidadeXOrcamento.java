package com.meuorcamento.model;

import com.meuorcamento.data.ModelBase;

public class HabilidadeXOrcamento extends ModelBase {


    private String keyHabilidade;
    private String keyOrcamento;
    private Orcamento orcamento;

    public HabilidadeXOrcamento(){
    }


    public HabilidadeXOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;

        keyHabilidade = orcamento.getHabilidade().getKey();
        keyOrcamento = orcamento.getKey();
    }


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof HabilidadeXOrcamento)) {
            return false;
        }

        if(obj == null)
            return false;

        HabilidadeXOrcamento habilidade = (HabilidadeXOrcamento)obj;

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
