package com.meuorcamento.utils.formatters;


import java.text.ParseException;

/**
 * Created by Administrador on 20/10/2015.
 */
public interface Formatteable {

    String format();

    Object getOriginalValue();

    Object convert() throws ParseException;

}
