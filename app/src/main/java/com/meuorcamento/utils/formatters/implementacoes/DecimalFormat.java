package com.meuorcamento.utils.formatters.implementacoes;
import com.meuorcamento.utils.formatters.Formatter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

public class DecimalFormat extends Formatter {

    private static String formato = "#,###,###,##0.00";

    private Locale defaultLocale;
    private DecimalFormatSymbols decimalFormatSymbols;

    public DecimalFormat(Object value, String mask) {
        super(value);

        if(mask != null) {
            formato = mask;
        }

        defaultLocale = new Locale("pt-BR");
        decimalFormatSymbols = new DecimalFormatSymbols(defaultLocale);

        decimalFormatSymbols.setDecimalSeparator(',');
        decimalFormatSymbols.setGroupingSeparator('.');
    }

    @Override
    public String format() {
        java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat(formato, decimalFormatSymbols);
        decimalFormat.setRoundingMode(RoundingMode.UP);

        if (value == null)
            return decimalFormat.format(0);

        if (!int.class.isInstance(value) &&
                !double.class.isInstance(value) &&
                !Double.class.isInstance(value) &&
                !Integer.class.isInstance(value) &&
                !float.class.isInstance(value)&&
                !Float.class.isInstance(value) &&
                !BigDecimal.class.isInstance(value)) {
            throw new ClassCastException("DecimalFormat requer um tipo double");
        }

        String result = decimalFormat.format(value);

        return result;
    }

    @Override
    public Object convert() throws ParseException {
        if(value == null)
            return 0.0;

        java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat(formato, decimalFormatSymbols);
        decimalFormat.setRoundingMode(RoundingMode.UP);

        return decimalFormat.parse(value.toString()).doubleValue();
    }
}
