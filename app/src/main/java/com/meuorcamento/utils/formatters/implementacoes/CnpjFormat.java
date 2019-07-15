package com.meuorcamento.utils.formatters.implementacoes;
import com.meuorcamento.utils.formatters.Formatter;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CnpjFormat extends Formatter {


    public CnpjFormat(Object value) {
        super(value);
    }

    @Override
    public String format() {

        if (value == null)
            return "";

        if (!String.class.isInstance(value)) {
            throw new ClassCastException("CnpjFormat requer um tipo String");
        }

        String cnpj = value.toString().replaceAll("\\D", "");

        Pattern pattern = Pattern.compile("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})");
        Matcher matcher = pattern.matcher(cnpj);
        if (matcher.matches())
            cnpj = matcher.replaceAll("$1.$2.$3/$4-$5");
        return cnpj;
    }

    @Override
    public Object convert() throws ParseException {
        if (value == null)
            return "";
        return value.toString().replaceAll("\\D+", "");
    }
}
