package com.meuorcamento.utils.formatters.implementacoes;
import com.meuorcamento.utils.formatters.Formatter;
import java.text.ParseException;


public class TitleCaseFormat extends Formatter {


    public TitleCaseFormat(Object value) {
        super(value);
    }

    @Override
    public String format() {

        if (value == null)
            return "";

        if (!String.class.isInstance(value)) {
            throw new ClassCastException("TitleCaseFormat requer um tipo String");
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(value.toString());
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }

    @Override
    public Object convert() throws ParseException {
        if (value == null)
            return "";
        return value.toString();
    }

}
