package com.meuorcamento.utils.formatters.implementacoes;
import com.meuorcamento.utils.formatters.Formatter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListToStringFormat extends Formatter {


    private final String separator;

    public ListToStringFormat(Object value, String separator) {
        super(value);
        this.separator = separator;
    }

    @Override
    public String format() {

        if (value == null)
            return "";

        if (!List.class.isInstance(value)) {
            throw new ClassCastException("ListToStringFormat requer um tipo List");
        }

        List<String> list = (List<String>)value;


        String stringList = "";
        if(list == null)
            return stringList;

        if(list.isEmpty())
            return stringList;

        for(int i = 0; i< list.size(); i++) {
            stringList += String.format("%s", list.get(i));

            if(i < list.size()-1)
                stringList += separator;
        }

        return stringList;
    }

    @Override
    public Object convert() throws ParseException {
        if(value == null)
            return new ArrayList<String>();

        return Arrays.asList(value.toString().split(separator));
    }

}
