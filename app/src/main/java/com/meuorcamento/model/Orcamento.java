package com.meuorcamento.model;

import com.meuorcamento.data.ModelBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Orcamento extends ModelBase {

    public Orcamento() {

    }

    private Habilidade habilidade;
    private String uid;
    private String titulo;
    private Date dataPrevista;
    private Date dataLancamento;
    private String detalhes;

    private OrcamentoAvaliacao orcamentoAvaliacao;
    private OrcamentoProposta propostaAprovada;
    private UsuarioPerfil usuarioPerfil;

    private String cep;
    private String cidade;
    private String uf;
    private String bairro;
    private String endereco;
    private int numeroEstabelecimento;
    private String enderecoPontoReferencia;
    private Map<String, OrcamentoProposta> orcamentoPropostaList;
    private List<String> fotosList;


    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public Date getDataPrevista() {
        return dataPrevista;
    }
    public void setDataPrevista(Date dataPrevista) {
        this.dataPrevista = dataPrevista;
    }
    public String getDetalhes() {
        return detalhes;
    }
    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }
    public String getCep() {
        return cep;
    }
    public void setCep(String cep) {
        this.cep = cep;
    }
    public String getCidade() {
        return cidade;
    }
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    public String getUf() {
        return uf;
    }
    public void setUf(String uf) {
        this.uf = uf;
    }
    public String getBairro() {
        return bairro;
    }
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }
    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    public int getNumeroEstabelecimento() {
        return numeroEstabelecimento;
    }
    public void setNumeroEstabelecimento(int numeroEstabelecimento) {
        this.numeroEstabelecimento = numeroEstabelecimento;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getEnderecoPontoReferencia() {
        return enderecoPontoReferencia;
    }

    public void setEnderecoPontoReferencia(String enderecoPontoReferencia) {
        this.enderecoPontoReferencia = enderecoPontoReferencia;
    }

    public Map<String, OrcamentoProposta> getOrcamentoPropostaList() {
        return orcamentoPropostaList;
    }

    public void setOrcamentoPropostaList(Map<String, OrcamentoProposta> orcamentoPropostaList) {
        this.orcamentoPropostaList = orcamentoPropostaList;
    }

    public void setFotosList(List<String> fotosList) {
        this.fotosList = fotosList;
    }

    public List<String> getFotosList() {
        if(fotosList == null)
            return new ArrayList<>();

        return fotosList;
    }

    public int getQuantidadePropostas() {
        if(orcamentoPropostaList == null)
            return 0;

        return orcamentoPropostaList.size();
    }

    public Habilidade getHabilidade() {
        if(habilidade == null)
            return new Habilidade("-LEMk733Zt37jPP_ZAV0","Assistência Técnica - Geral", "Serviços não especificados", "Assistência Técnica");

        return habilidade;
    }

    public OrcamentoAvaliacao getOrcamentoAvaliacao() {
        return orcamentoAvaliacao;
    }

    public void setOrcamentoAvaliacao(OrcamentoAvaliacao orcamentoAvaliacao) {
        this.orcamentoAvaliacao = orcamentoAvaliacao;
    }

    public OrcamentoProposta getPropostaAprovada() {
        return propostaAprovada;
    }

    public void setPropostaAprovada(OrcamentoProposta propostaAprovada) {
        this.propostaAprovada = propostaAprovada;
    }

    public UsuarioPerfil getUsuarioPerfil() {
        return usuarioPerfil;
    }

    public void setUsuarioPerfil(UsuarioPerfil usuarioPerfil) {
        this.usuarioPerfil = usuarioPerfil;
    }

    public void setHabilidade(Habilidade habilidade) {
        this.habilidade = habilidade;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof  Orcamento)) {
            return false;
        }

        if(obj == null)
            return false;

        Orcamento orcamento = (Orcamento)obj;

        if(orcamento.getKey() == null)
            return false;

        if(orcamento.getKey().equals(this.getKey()))
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
