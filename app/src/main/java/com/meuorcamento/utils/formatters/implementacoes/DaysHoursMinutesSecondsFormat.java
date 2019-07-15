package com.meuorcamento.utils.formatters.implementacoes;
import com.meuorcamento.utils.formatters.Formatter;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class DaysHoursMinutesSecondsFormat extends Formatter {


    public DaysHoursMinutesSecondsFormat(Object value) {
        super(value);
    }

    @Override
    public String format() throws ClassCastException {


        if (value == null)
            return "";


        if (!Integer.class.isInstance(value) && !int.class.isInstance(value) && !Date.class.isInstance(value) && !Calendar.class.isInstance(value)) {
            throw new ClassCastException("DaysHoursMinutesSecondsFormat requer um tipo int, Date ou Calendar");
        }

        int segundosTotais = 0;
        if(Integer.class.isInstance(value) || int.class.isInstance(value)) {
            segundosTotais = (int) value;
        }

        if(Date.class.isInstance(value)) {
            Calendar calendarData = Calendar.getInstance();
            calendarData.setTime((Date) value);
            long millisData = calendarData.getTimeInMillis();

            long millisHoje = Calendar.getInstance().getTimeInMillis();

            segundosTotais = (int)((millisHoje - millisData) / 1000);
            if(segundosTotais < 0)
                segundosTotais = segundosTotais * -1;
        }

        if(Calendar.class.isInstance(value)) {
            Calendar data = (Calendar) value;
            Calendar dataAtual = Calendar.getInstance();
            segundosTotais = (int)((data.getTimeInMillis() - dataAtual.getTimeInMillis()) / 1000);
            if(segundosTotais < 0)
                segundosTotais = segundosTotais * -1;
        }

        int dias = segundosTotais / 86400;
        int horas = (segundosTotais - (dias * 86400)) / 3600;
        int minutos = (segundosTotais - ((dias * 86400) + (horas * 3600))) / 60;
        int segundosRestantes = segundosTotais - ((dias * 86400) + (horas * 3600) + (minutos * 60));

        if (segundosTotais < 60) {
            return pluralizeSegundos(segundosTotais);
        }

        if (dias > 0 && horas > 0 && minutos > 0 && segundosRestantes > 0)
            return String.format("%s, %s, %s e %s", pluralizeDias(dias), pluralizeHoras(horas), pluralizeMinutos(minutos), pluralizeSegundos(segundosRestantes));

        if (dias > 0 && horas > 0 && minutos > 0 && segundosRestantes == 0)
            return String.format("%s, %s e %s ", pluralizeDias(dias), pluralizeHoras(horas),pluralizeMinutos(minutos));

        if (dias > 0 && horas > 0 && minutos == 0 && segundosRestantes > 0)
            return String.format("%s, %s e %s", pluralizeDias(dias), pluralizeHoras(horas), pluralizeSegundos(segundosRestantes));

        if (dias > 0 && horas == 0 && minutos > 0 && segundosRestantes > 0)
            return String.format("%s, %s e %s ", pluralizeDias(dias), pluralizeMinutos(minutos), pluralizeSegundos(segundosRestantes));

        if (dias > 0 && horas > 0 && minutos == 0 && segundosRestantes == 0)
            return String.format("%s e %s ", pluralizeDias(dias), pluralizeHoras(horas));

        if (dias > 0 && horas == 0 && minutos == 0 && segundosRestantes == 0)
            return String.format("%s", pluralizeDias(dias));

        if (dias == 0 && horas > 0 && minutos > 0 && segundosRestantes > 0)
            return String.format("%s, %s e %s", pluralizeHoras(horas), pluralizeMinutos(minutos), pluralizeSegundos(segundosRestantes));

        if (dias == 0 && horas > 0 && minutos > 0 && segundosRestantes == 0)
            return String.format("%s e %s", pluralizeHoras(horas), pluralizeMinutos(minutos));

        if (dias == 0 && horas > 0 && minutos == 0 && segundosRestantes > 0)
            return String.format("%s e %s", pluralizeHoras(horas), pluralizeSegundos(segundosRestantes));

        if (dias == 0 && horas == 0 && minutos > 0 && segundosRestantes > 0)
            return String.format("%s e %s", pluralizeMinutos(minutos), pluralizeSegundos(segundosRestantes));

        if (dias == 0 && horas > 0 && minutos == 0 && segundosRestantes == 0)
            return String.format("%s", pluralizeHoras(horas));

        if (dias == 0 && horas == 0 && minutos > 0 && segundosRestantes == 0)
            return String.format("%s", pluralizeMinutos(minutos));


        return "";

    }

    private String pluralizeDias(int valor) {
        if(valor > 1)
            return String.format("%s dias", valor);

        return "1 dia";
    }
    private String pluralizeHoras(int valor) {
        if(valor > 1)
            return String.format("%s horas", valor);

        return "1 hora";
    }

    private String pluralizeMinutos(int valor) {
        if(valor > 1)
            return String.format("%s minutos", valor);

        return "1 minuto";
    }

    private String pluralizeSegundos(int valor) {
        if(valor > 1)
            return String.format("%s segundos", valor);

        return "1 segundo";
    }

    @Override
    public Object convert() throws ParseException {
        if(value == null)
            return null;

        return value;
    }
}
