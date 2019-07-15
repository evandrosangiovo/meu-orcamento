package com.meuorcamento.model;

import com.meuorcamento.data.ModelBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class UsuarioPerfil extends ModelBase{

    public UsuarioPerfil() {

    }

    private Usuario usuario;
    private String uid;
    private String email;
    private String nome;
    private String sobreNome;
    private String cpf;
    private String identidade;
    private String cep;
    private String cidade;
    private String uf;
    private String bairro;
    private String endereco;
    private int numeroEstabelecimento;
    private String celular;
    private String skype;
    private Date dataNascimento;
    private String urlAvatar;
    private Map<String, OrcamentoAvaliacao> usuarioAvaliacaoList;
    private int orcamentosExecutados;
    private List<String> imagensPerfil;
    private int tipoUsuario;
    private List<String> habilidadesList;


    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobreNome() {
        return sobreNome;
    }

    public void setSobreNome(String sobreNome) {
        this.sobreNome = sobreNome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getIdentidade() {
        return identidade;
    }

    public void setIdentidade(String identidade) {
        this.identidade = identidade;
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

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }


    public Map<String, OrcamentoAvaliacao> getUsuarioAvaliacaoList() {
        return usuarioAvaliacaoList;
    }

    public void setUsuarioAvaliacaoList(Map<String, OrcamentoAvaliacao> usuarioAvaliacaoList) {
        this.usuarioAvaliacaoList = usuarioAvaliacaoList;
    }

    public int getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(int tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public List<String> getImagensPerfil() {
        return imagensPerfil;
    }

    public void setImagensPerfil(List<String> imagensPerfil) {
        this.imagensPerfil = imagensPerfil;
    }

    public List<String> getHabilidadesList() {
        /*
        if(habilidadesList == null) {
            habilidadesList = new ArrayList<>();
            habilidadesList.add("-LEMk7354JFClG48CsBX");
            habilidadesList.add("-LEMk732DhpLv6z8yD0F");
            habilidadesList.add("-LEMk733Zt37jPP_ZAUz");
            habilidadesList.add("-LEMk733Zt37jPP_ZAV-");
            habilidadesList.add("-LEMk733Zt37jPP_ZAV0");
            habilidadesList.add("-LEMk733Zt37jPP_ZAV1");
            habilidadesList.add("-LEMk734PoiHdWYoOPgy");
        }
        */
        return habilidadesList;
    }

    public void setHabilidadesList(List<String> habilidadesList) {
        this.habilidadesList = habilidadesList;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof  UsuarioPerfil)) {
            return false;
        }

        if(obj == null)
            return false;

        UsuarioPerfil usuarioPerfil = (UsuarioPerfil)obj;

        if(usuarioPerfil.getKey() == this.getKey())
            return true;

        return false;

    }

    public float getAvaliacaoGeral() {
        float avaliacaoGeral = 0f;
        if(usuarioAvaliacaoList == null || usuarioAvaliacaoList.size() == 0) {
            return avaliacaoGeral;
        }


        for (OrcamentoAvaliacao item : usuarioAvaliacaoList.values()) {
            avaliacaoGeral += item.getNota();
        }


        return avaliacaoGeral / usuarioAvaliacaoList.size();
    }

    public int getOrcamentosExecutados() {
        return orcamentosExecutados;
    }

    public void setOrcamentosExecutados(int orcamentosExecutados) {
        this.orcamentosExecutados = orcamentosExecutados;
    }

    @Override
    public int hashCode() {
        if(getKey() == null)
            return super.hashCode();

        return getKey().hashCode();
    }
}
