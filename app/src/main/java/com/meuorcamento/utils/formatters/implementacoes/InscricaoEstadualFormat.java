package com.meuorcamento.utils.formatters.implementacoes;
import com.meuorcamento.utils.formatters.Formatter;
import java.text.ParseException;

public class InscricaoEstadualFormat extends Formatter {


    public InscricaoEstadualFormat(Object value) {
        super(value);
    }

    @Override
    public String format() {

        if (value == null || value.toString().isEmpty())
            return "Isento";


        if (!String.class.isInstance(value)) {
            throw new ClassCastException("CnpjFormat requer um tipo String");
        }

        return value.toString();
    }

    @Override
    public Object convert() throws ParseException {
        if (value == null)
            return "";
        return value.toString().replaceAll("\\D+", "");
    }
}
