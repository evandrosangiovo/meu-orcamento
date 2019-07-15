package com.meuorcamento.utils.formatters.implementacoes;
import com.meuorcamento.utils.formatters.Formatter;
import java.text.ParseException;

public class FirstLetterFormat extends Formatter {


    public FirstLetterFormat(Object value) {
        super(value);
    }


    @Override
    public String format() {

        if (value == null || value.toString().isEmpty())
            return " ";

        if (!String.class.isInstance(value)) {
            throw new ClassCastException("FirstLetterFormat requer um tipo String");
        }

        return value.toString().substring(0, 1).toUpperCase();
    }

    @Override
    public Object convert() throws ParseException {
        if (value == null)
            return "";
        return value.toString();
    }

}
