package com.meuorcamento.utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidacoesHelper {

    private ValidacoesHelper() {
    }

    private static final int[] pesoCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] pesoCNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    public static boolean validarIE(String ie, String uF) {
        //	InscricaoEstadual instance = InscricaoEstadualFactory.getInstance(uF);

        //return instance.validar(ie);
        return true;
    }

    private static int calcularDigito(String str, int[] peso) {
        int soma = 0;
        for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
            digito = Integer.parseInt(str.substring(indice, indice + 1));
            soma += digito * peso[peso.length - str.length() + indice];
        }
        soma = 11 - soma % 11;
        return soma > 9 ? 0 : soma;
    }

    public static boolean isValidCPF(String cpf) {

        if(cpf == null || cpf.isEmpty())
            return  false;

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11)
            return false;


        if(cpf.equalsIgnoreCase("00000000000") || cpf.equalsIgnoreCase("11111111111") ||
                cpf.equalsIgnoreCase("22222222222") || cpf.equalsIgnoreCase("33333333333") ||
                cpf.equalsIgnoreCase("44444444444") || cpf.equalsIgnoreCase("55555555555") ||
                cpf.equalsIgnoreCase("66666666666") || cpf.equalsIgnoreCase("77777777777") ||
                cpf.equalsIgnoreCase("88888888888") || cpf.equalsIgnoreCase("99999999999")) {
            return false;
        }

        Integer digito1 = calcularDigito(cpf.substring(0, 9), pesoCPF);
        Integer digito2 = calcularDigito(cpf.substring(0, 9) + digito1, pesoCPF);
        return cpf.equals(cpf.substring(0, 9) + digito1.toString() + digito2.toString());
    }

    public static boolean isValidCNPJ(String cnpj) {
        if(cnpj == null)
            return  false;

        cnpj = cnpj.replaceAll("\\D", "");

        if (cnpj.length() != 14)
            return false;

        Integer digito1 = calcularDigito(cnpj.substring(0, 12), pesoCNPJ);
        Integer digito2 = calcularDigito(cnpj.substring(0, 12) + digito1, pesoCNPJ);
        return cnpj.equals(cnpj.substring(0, 12) + digito1.toString() + digito2.toString());
    }

    public static boolean isValidEmail(String email) {
        Pattern p = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$");
        Matcher m = p.matcher(email);
        return m.find();
    }

    public static boolean isValidEmail(String[] email) {
        for(int i = 0; i<email.length; i++) {
            if(!isValidEmail(email[i])) {
                return false;
            }
        }
        return true;
    }
}