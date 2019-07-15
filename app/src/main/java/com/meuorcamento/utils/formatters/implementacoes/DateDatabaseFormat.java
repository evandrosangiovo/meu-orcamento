package com.meuorcamento.utils.formatters.implementacoes;
import com.meuorcamento.utils.formatters.Formatter;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateDatabaseFormat extends Formatter {

    SimpleDateFormat simpleDateFormat;

    public DateDatabaseFormat(Object value) {
            super(value);

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setLenient(false);
    }

    @Override
    public String format() throws ClassCastException {



        if (value == null)
            return "";

        if (!Date.class.isInstance(value) && !Calendar.class.isInstance(value)) {
            throw new ClassCastException("DateDatabaseFormat requer um tipo Date ou Calendar");
        }

        Date dateValue = null;
        if(Date.class.isInstance(value)) {
            dateValue = (Date) value;;
        }

        if(Calendar.class.isInstance(value)) {
            dateValue = ((Calendar) value).getTime();
        }

        DateFormatSymbols.getInstance(Locale.getDefault());

        return simpleDateFormat.format(dateValue);
    }

    @Override
    public Object convert() throws ParseException {
        if (value == null)
            return null;

        return simpleDateFormat.parse(value.toString());
    }
}
