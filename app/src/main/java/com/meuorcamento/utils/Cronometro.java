package com.meuorcamento.utils;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by Administrador on 20/10/2015.
 */
public class Cronometro {

    private long timeInicial;
    private long timeFinal;
    private long timeTotal;
    private boolean finalizado = false;

    public Cronometro(boolean autoStart) {
        if(autoStart)
            start();
        //timeInicial = Calendar.getInstance().getTimeInMillis();
    }



    public Cronometro start() {
        timeInicial = Calendar.getInstance().getTimeInMillis();
        finalizado = false;
        return this;
    }
    public Cronometro stop()
    {
        if(finalizado)
            throw new IllegalStateException("Cronometro nao foi iniciado...");
        finalizado = true;
        timeFinal = Calendar.getInstance().getTimeInMillis();
        timeTotal = timeFinal - timeInicial;
        return this;
    }

    public long getTotalTime() {
        if(finalizado)
            return timeTotal;

        stop();
        return timeTotal;
    }

    public int getTotalSeconds() {
        if(finalizado)
            return (int)(timeTotal / 1000);

        stop();
        return (int)(timeTotal / 1000);
    }
}
