package com.meuorcamento.app.mainmenu;

import com.meuorcamento.utils.IEnum;

public enum EnumView implements IEnum {

    VIEW_MEUS_ORCAMENTOS(0),
    VIEW_VISUALIZAR_PERFIL(1),
    VIEW_ORCAMENTOS_SOLICITADOS(2),
    VIEW_ORCAMENTOS_SOLICITADOS_APROVADOS(3);

    private final int value;
    EnumView(int value){
        this.value = value;
    }

    @Override
    public int getValue(){
        return this.value;
    }

}
