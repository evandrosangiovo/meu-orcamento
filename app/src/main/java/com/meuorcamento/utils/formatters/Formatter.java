package com.meuorcamento.utils.formatters;

import java.text.ParseException;

import com.meuorcamento.utils.formatters.implementacoes.CepFormat;
import com.meuorcamento.utils.formatters.implementacoes.ChaveLicencaFormat;
import com.meuorcamento.utils.formatters.implementacoes.CnpjFormat;
import com.meuorcamento.utils.formatters.implementacoes.CpfCnpjFormat;
import com.meuorcamento.utils.formatters.implementacoes.CpfFormat;
import com.meuorcamento.utils.formatters.implementacoes.DateDatabaseFormat;
import com.meuorcamento.utils.formatters.implementacoes.DateExtensiveFormat;
import com.meuorcamento.utils.formatters.implementacoes.DateExtensiveNoTimeFormat;
import com.meuorcamento.utils.formatters.implementacoes.DateFormat;
import com.meuorcamento.utils.formatters.implementacoes.DateNoYearFormat;
import com.meuorcamento.utils.formatters.implementacoes.DateTimeDatabaseFormat;
import com.meuorcamento.utils.formatters.implementacoes.DateTimeFormat;
import com.meuorcamento.utils.formatters.implementacoes.DaysFormat;
import com.meuorcamento.utils.formatters.implementacoes.DaysHoursFormat;
import com.meuorcamento.utils.formatters.implementacoes.DaysHoursMinutesFormat;
import com.meuorcamento.utils.formatters.implementacoes.DaysHoursMinutesSecondsFormat;
import com.meuorcamento.utils.formatters.implementacoes.DecimalFormat;
import com.meuorcamento.utils.formatters.implementacoes.FirstLetterFormat;
import com.meuorcamento.utils.formatters.implementacoes.InscricaoEstadualFormat;
import com.meuorcamento.utils.formatters.implementacoes.InteiroFormat;
import com.meuorcamento.utils.formatters.implementacoes.ListToStringFormat;
import com.meuorcamento.utils.formatters.implementacoes.TitleCaseFormat;
import com.meuorcamento.utils.formatters.implementacoes.MonthExtensiveFormat;
import com.meuorcamento.utils.formatters.implementacoes.NoFormat;
import com.meuorcamento.utils.formatters.implementacoes.OrdinalNumberFormat;
import com.meuorcamento.utils.formatters.implementacoes.TelefoneFormat;
import com.meuorcamento.utils.formatters.implementacoes.TimeFormat;



public abstract class Formatter implements Formatteable {


    protected Object value;

    public Formatter(Object value) {
        this.value = value;
    }

    public static Formatter getInstance(Object value, FormatTypeEnum tipoFormatacao) {

        switch (tipoFormatacao) {
            case CPF: {
                return new CpfFormat(value);
            }
            case CNPJ: {
                return new CnpjFormat(value);
            }
            case INTEIRO: {
                return new InteiroFormat(value);
            }
            case DECIMAL_UM: {
                return new DecimalFormat(value, "#,###,###,##0.0");
            }
            case DECIMAL_DOIS: {
                return new DecimalFormat(value, "#,###,###,##0.00");
            }
            case DECIMAL_TREZ:{
                return new DecimalFormat(value, "#,###,###,##0.000");
            }
            case DECIMAL_QUATRO: {
                return new DecimalFormat(value, "#,###,###,##0.0000");
            }
            case DECIMAL_CINCO: {
                return new DecimalFormat(value, "#,###,###,##0.00000");
            }
            case DECIMAL_UM_MOEDA: {
                return new DecimalFormat(value, "$#,###,###,##0.0");
            }
            case DECIMAL_DOIS_MOEDA: {
                return new DecimalFormat(value, "$#,###,###,##0.00");
            }
            case DECIMAL_TREZ_MOEDA:{
                return new DecimalFormat(value, "$#,###,###,##0.000");
            }
            case DECIMAL_QUATRO_MOEDA: {
                return new DecimalFormat(value, "$#,###,###,##0.0000");
            }
            case DECIMAL_CINCO_MOEDA: {
                return new DecimalFormat(value, "$#,###,###,##0.00000");
            }
            case LIST_TO_STRING_COMMA_SEPARATOR: {
                return new ListToStringFormat(value, ",");
            }
            case LIST_TO_STRING_SEMICOLON_SEPARATOR: {
                return new ListToStringFormat(value, ";");
            }
            case CEP: {
                return new CepFormat(value);
            }
            case DATE: {
                return new DateFormat(value);
            }
            case DATE_NO_YEAR: {
                return new DateNoYearFormat(value);
            }
            case TIME_NO_DATE: {
                return new TimeFormat(value);
            }
            case DATETIME: {
                return new DateTimeFormat(value);
            }
            case DATE_EXTENSIVE: {
                return new DateExtensiveFormat(value);
            }
            case DATE_EXTENSIVE_NO_TIME: {
                return new DateExtensiveNoTimeFormat(value);
            }
            case MONTH_EXTENSIVE: {
                return new MonthExtensiveFormat(value);
            }
            case DATE_DATABASE: {
                return new DateDatabaseFormat(value);
            }
            case DATETIME_DATABASE: {
                return new DateTimeDatabaseFormat(value);
            }
            case TELEFONE: {
                return new TelefoneFormat(value);
            }
            case INSCRICAO_ESTADUAL: {
                return new InscricaoEstadualFormat(value);
            }
            case CPF_CPNJ: {
                return new CpfCnpjFormat(value);
            }
            case CHAVE_LICENCA: {
                return new ChaveLicencaFormat(value);
            }
            case TITLE_CASE: {
                return new TitleCaseFormat(value);
            }
            case DAYSHOURSMINUTESSECONDS: {
                return new DaysHoursMinutesSecondsFormat(value);
            }
            case DAYSHOURSMINUTES: {
                return new DaysHoursMinutesFormat(value);
            }
            case DAYSHOURS: {
                return new DaysHoursFormat(value);
            }
            case DAYS: {
                return new DaysFormat(value);
            }
            case FIRST_LETTER: {
                return new FirstLetterFormat(value);
            }
            case ORDINAL_NUMBER: {
                return new OrdinalNumberFormat(value);
            }
            default:
                return new NoFormat(value);
        }
    }

    public enum FormatTypeEnum {
        NO_FORMAT,
        DECIMAL_UM,
        DECIMAL_DOIS,
        DECIMAL_TREZ,
        DECIMAL_QUATRO,
        DECIMAL_CINCO,
        DECIMAL_UM_MOEDA,
        DECIMAL_DOIS_MOEDA,
        DECIMAL_TREZ_MOEDA,
        DECIMAL_QUATRO_MOEDA,
        DECIMAL_CINCO_MOEDA,
        LIST_TO_STRING_COMMA_SEPARATOR,
        LIST_TO_STRING_SEMICOLON_SEPARATOR,
        CHAVE_LICENCA,
        INTEIRO,
        DDADECIMAL,
        CPF,
        CNPJ,
        CPF_CPNJ,
        TELEFONE,
        CEP,
        DATE,
        DATE_NO_YEAR,
        FIRST_LETTER,
        TIME_NO_DATE,
        DATE_DATABASE,
        DATETIME,
        DATETIME_DATABASE,
        DAYS,
        DAYSHOURS,
        DAYSHOURSMINUTES,
        DAYSHOURSMINUTESSECONDS,
        DATE_EXTENSIVE,
        TITLE_CASE,
        ORDINAL_NUMBER,
        MONTH_EXTENSIVE,
        DATE_EXTENSIVE_NO_TIME,
        INSCRICAO_ESTADUAL
    }

    @Override
    public Object getOriginalValue() {
        return value;
    }

    @Override
    public Object convert() throws ParseException {
        return value.toString();
    }
}

