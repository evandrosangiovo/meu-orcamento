package com.meuorcamento.utils.formatters.implementacoes;
import com.meuorcamento.utils.formatters.Formatter;
import java.text.ParseException;

public class NoFormat extends Formatter {

    public NoFormat(Object value) {
        super(value);
    }

    @Override
    public String format() {

        if (value == null)
            return "";

        return value.toString();
    }

    @Override
    public Object convert() throws ParseException {
        if(value == null)
            return value;

        return value.toString();
    }


}
