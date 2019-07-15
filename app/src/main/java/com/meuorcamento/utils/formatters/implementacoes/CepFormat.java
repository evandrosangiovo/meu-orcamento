package com.meuorcamento.utils.formatters.implementacoes;
import com.meuorcamento.utils.formatters.Formatter;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CepFormat extends Formatter {


    public CepFormat(Object value) {
        super(value);
    }

    @Override
    public String format() {

        if (value == null)
            return "";

        if (!String.class.isInstance(value)) {
            throw new ClassCastException("CepFormat requer um tipo String");
        }

        String cep = value.toString().replaceAll("\\D", "");

        Pattern pattern = Pattern.compile("(\\d{5})(\\d{3})");
        Matcher matcher = pattern.matcher(cep);
        if (matcher.matches())
            cep = matcher.replaceAll("$1-$2");
        return cep;
    }

    @Override
    public Object convert() throws ParseException {
        if (value == null)
            return "";
        return value.toString().replaceAll("\\D+", "");
    }
}
