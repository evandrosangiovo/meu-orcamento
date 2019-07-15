package com.meuorcamento.utils.formatters.implementacoes;
import com.meuorcamento.utils.formatters.Formatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateTimeFormat extends Formatter {

    SimpleDateFormat simpleDateFormat;
    public DateTimeFormat(Object value) {
        super(value);

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        simpleDateFormat.setLenient(false);
    }

    @Override
    public String format() throws ClassCastException {

        if (value == null)
            return "";

        if (!Date.class.isInstance(value) && !Calendar.class.isInstance(value)) {
            throw new ClassCastException("DateTimeFormat requer um tipo Date ou Calendar");
        }

        Date dateValue = null;
        if (Date.class.isInstance(value)) {
            dateValue = (Date) value;
        }

        if (Calendar.class.isInstance(value)) {
            dateValue = ((Calendar) value).getTime();
        }

        return simpleDateFormat.format(dateValue);
    }

    @Override
    public Object convert() throws ParseException {
        if (value == null)
            return null;

        return simpleDateFormat.parse(value.toString());
    }
}
