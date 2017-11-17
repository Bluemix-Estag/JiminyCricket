package com.ramon.code.scoutsapp.models;

import java.io.Serializable;

/**
 * Created by Ramon on 14-Nov-17.
 */

public class Partida implements Serializable{

    private String timeMandante;
    private String timeVisitante;
    private String campeonato;
    private int rodada;
    private int ano;
    private String data;

    public Partida(String timeMandante, String timeVisitante, String campeonato, int rodada, int ano, String data) {
        this.timeMandante = timeMandante;
        this.timeVisitante = timeVisitante;
        this.campeonato = campeonato;
        this.rodada = rodada;
        this.ano = ano;
        this.data = data;
    }

    public String getTimeMandante() {
        return timeMandante;
    }

    public void setTimeMandante(String timeMandante) {
        this.timeMandante = timeMandante;
    }

    public String getTimeVisitante() {
        return timeVisitante;
    }

    public void setTimeVisitante(String timeVisitante) {
        this.timeVisitante = timeVisitante;
    }

    public String getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(String campeonato) {
        this.campeonato = campeonato;
    }

    public int getRodada() {
        return rodada;
    }

    public void setRodada(int rodada) {
        this.rodada = rodada;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return this.getTimeMandante() + " x " + this.getTimeVisitante() + ": " + this.getData() + " - " + this.getCampeonato();
    }
}
