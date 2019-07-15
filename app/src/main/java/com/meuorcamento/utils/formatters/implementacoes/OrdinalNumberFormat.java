package com.meuorcamento.utils.formatters.implementacoes;
import com.meuorcamento.utils.formatters.Formatter;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class OrdinalNumberFormat extends Formatter {


    public OrdinalNumberFormat(Object value) {
        super(value);
    }

    @Override
    public String format() {

        Locale defaultLocale = new Locale("pt-BR");

        NumberFormat numberFormat = NumberFormat.getNumberInstance(defaultLocale);;
        numberFormat.setRoundingMode(RoundingMode.FLOOR);
        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(0);

        if(value == null)
            return numberFormat.format(0);

        if (!int.class.isInstance(value) &&
                !double.class.isInstance(value) &&
                !Double.class.isInstance(value) &&
                !Integer.class.isInstance(value) &&
                !long.class.isInstance(value) &&
                !Float.class.isInstance(value) &&
                !float.class.isInstance(value) &&
                !Long.class.isInstance(value)) {
            throw new ClassCastException("Inteiroformat requer um tipo int ou double");
        }

        return String.format("%s", numberFormat.format(value));

    }

    @Override
    public Object convert() throws ParseException {
        if(value == null)
            return 0;

        Locale defaultLocale = Locale.getDefault();
        NumberFormat numberFormat = NumberFormat.getNumberInstance(defaultLocale);;
        numberFormat.setRoundingMode(RoundingMode.FLOOR);
        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(0);

        return numberFormat.parse(value.toString()).intValue();
    }
}
