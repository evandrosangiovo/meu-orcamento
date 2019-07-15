package com.meuorcamento.utils.formatters.implementacoes;
import com.meuorcamento.utils.formatters.Formatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormat extends Formatter {

    SimpleDateFormat simpleDateFormat;
    public TimeFormat(Object value) {
        super(value);

        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        simpleDateFormat.setLenient(false);
    }

    @Override
    public String format() throws ClassCastException {

        if (value == null)
            return "";

        if (!Date.class.isInstance(value)) {
            throw new ClassCastException("DateTimeFormat requer um tipo Date");
        }

        return simpleDateFormat.format(value);
    }

    @Override
    public Object convert() throws ParseException {
        if (value == null)
            return null;

        return simpleDateFormat.parse(value.toString());
    }
}
