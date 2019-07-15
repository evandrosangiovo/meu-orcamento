package com.meuorcamento.utils.formatters.implementacoes;
import com.meuorcamento.utils.formatters.Formatter;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TelefoneFormat extends Formatter {


    public TelefoneFormat(Object value) {
        super(value);
    }

    @Override
    public String format() {

        if (value == null)
            return "";

        if (!String.class.isInstance(value)) {
            throw new ClassCastException("TelefoneFormat requer um tipo String");
        }

        String telefone = value.toString().replaceAll("\\D", "");

        Matcher matcher = null;

        if(telefone.length() == 8) {
            Pattern pattern = Pattern.compile("(\\d{4})(\\d{4})");
            matcher = pattern.matcher(telefone);
            telefone = matcher.replaceAll("(00) $1-$2");
        }

        if(telefone.length() == 9) {
            Pattern pattern = Pattern.compile("(\\d{5})(\\d{4})");
            matcher = pattern.matcher(telefone);
            telefone = matcher.replaceAll("(00) $1-$2");
        }

        if(telefone.length() == 10) {
            Pattern pattern = Pattern.compile("(\\d{2})(\\d{4})(\\d{4})");
            matcher = pattern.matcher(telefone);
            telefone = matcher.replaceAll("($1) $2-$3");
        }

        if(telefone.length() == 11) {
            Pattern pattern = Pattern.compile("(\\d{2})(\\d{5})(\\d{4})");
            matcher = pattern.matcher(telefone);
            telefone = matcher.replaceAll("($1) $2-$3");
        }

        return telefone;
    }

    @Override
    public Object convert() throws ParseException {
        if (value == null)
            return "";
        return value.toString().replaceAll("\\D+", "");
    }

}
