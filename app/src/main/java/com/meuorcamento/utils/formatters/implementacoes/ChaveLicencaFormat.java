package com.meuorcamento.utils.formatters.implementacoes;

import com.meuorcamento.utils.formatters.Formatter;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChaveLicencaFormat extends Formatter {


    public ChaveLicencaFormat(Object value) {
        super(value);
    }

    @Override
    public String format() {

        if (value == null)
            return "";

        if (!String.class.isInstance(value)) {
            throw new ClassCastException("ChaveBssFormat requer um tipo String");
        }

        String chave = value.toString();

        Pattern pattern = Pattern.compile("(\\w{4})(\\w{4})(\\w{4})(\\w{4})(\\w{4})");
        Matcher matcher = pattern.matcher(chave);
        if (matcher.matches())
            chave = matcher.replaceAll("$1-$2-$3-$4-$5");
        return chave;
    }

    @Override
    public Object convert() throws ParseException {
        if (value == null)
            return "";
        return value.toString().replaceAll("\\D+", "");
    }

}
