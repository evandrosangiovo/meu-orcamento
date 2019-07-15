package com.meuorcamento.utils.formatters.implementacoes;
import com.meuorcamento.utils.formatters.Formatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateExtensiveNoTimeFormat extends Formatter {

    SimpleDateFormat simpleDateFormat;
    public DateExtensiveNoTimeFormat(Object value) {
        super(value);
        simpleDateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy");
        simpleDateFormat.setLenient(false);
    }

    @Override
    public String format() throws ClassCastException {

        if (value == null)
            return "";

        if (!Date.class.isInstance(value) && !Calendar.class.isInstance(value)) {
            throw new ClassCastException("DateExtensiveFormat requer um tipo Date ou Calendar");
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
        if(value == null)
            return null;

        return simpleDateFormat.parse(value.toString());
    }
}
