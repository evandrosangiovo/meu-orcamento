package com.meuorcamento.data.mapping;

import com.meuorcamento.model.Habilidade;
import com.meuorcamento.model.Usuario;
import com.meuorcamento.model.UsuarioPerfil;

public class Mapping {

    private static boolean executedMapping = false;


    private Mapping() {
    }

    /**
     * Constrói o mapeamento para o ORM Deve ser chamada no início da aplicação
     *
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     */
    public static void createMapping() throws IllegalArgumentException, IllegalStateException {
        if (!executedMapping) {
            buildMapping();
            executedMapping = true;
        }
    }

    private static void buildMapping() throws IllegalArgumentException, IllegalStateException {

        Table usuarios = TableBuilder.newTable("usuarios", Usuario.class);
        usuarios.addColumnLong("id", true);
        usuarios.addColumnVarchar("uid");
        usuarios.addColumnVarchar("email");
        usuarios.addColumnBoolean("excluido");
        TableBuilder.finishConfiguration(usuarios);

        Table usuarioPerfil = TableBuilder.newTable("usuariosPerfis", UsuarioPerfil.class);
        usuarioPerfil.addColumnLong("id", true);
        usuarioPerfil.addColumnVarchar("uid");
        usuarioPerfil.addColumnVarchar("nome");
        usuarioPerfil.addColumnVarchar("sobreNome");
        usuarioPerfil.addColumnVarchar("cpf");
        usuarioPerfil.addColumnVarchar("identidade");
        usuarioPerfil.addColumnVarchar("cep");
        usuarioPerfil.addColumnVarchar("cidade");
        usuarioPerfil.addColumnVarchar("uf");
        usuarioPerfil.addColumnVarchar("bairro");
        usuarioPerfil.addColumnVarchar("endereco");
        usuarioPerfil.addColumnInteger("numeroEstabelecimento");
        usuarioPerfil.addColumnVarchar("celular");
        usuarioPerfil.addColumnVarchar("skype");
        usuarioPerfil.addColumnDate("dataNascimento");
        usuarioPerfil.addColumnVarchar("urlAvatar");
        usuarioPerfil.addColumnBoolean("excluido");
        TableBuilder.finishConfiguration(usuarioPerfil);


        Table habilidades = TableBuilder.newTable("habilidades", Habilidade.class);
        habilidades.addColumnLong("id", true, true);
        habilidades.addColumnVarchar("key");
        habilidades.addColumnVarchar("titulo");
        habilidades.addColumnVarchar("descricao");
        habilidades.addColumnVarchar("grupo");
        habilidades.addColumnBoolean("excluido");
        TableBuilder.finishConfiguration(habilidades);

    }
}


