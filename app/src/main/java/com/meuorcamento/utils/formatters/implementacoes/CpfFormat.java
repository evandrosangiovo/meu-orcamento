package com.meuorcamento.utils.formatters.implementacoes;
import com.meuorcamento.utils.formatters.Formatter;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CpfFormat extends Formatter {


    public CpfFormat(Object value) {
        super(value);
    }

    @Override
    public String format() {

        if (value == null)
            return "";

        if (!String.class.isInstance(value)) {
            throw new ClassCastException("CpfFormat requer um tipo String");
        }

        String cpf = value.toString().replaceAll("\\D", "");

        Pattern pattern = Pattern.compile("(\\d{3})(\\d{3})(\\d{3})(\\d{2})");
        Matcher matcher = pattern.matcher(cpf);
        if (matcher.matches())
            cpf = matcher.replaceAll("$1.$2.$3-$4");
        return cpf;
    }

    @Override
    public Object convert() throws ParseException {
        if (value == null)
            return "";
        return value.toString().replaceAll("\\D+", "");
    }

}
