package com.ramon.code.scoutsapp.models;

import java.io.Serializable;

/**
 * Created by Ramon on 17-Nov-17.
 */

public class TimeInfo implements Serializable{

    private String equipe;
    private String campeonato;
    private int ano;
    private String data;

    public TimeInfo(String equipe, String campeonato, int ano, String data) {
        this.equipe = equipe;
        this.campeonato = campeonato;
        this.ano = ano;
        this.data = data;
    }

    public String getEquipe() {
        return equipe;
    }

    public void setEquipe(String equipe) {
        this.equipe = equipe;
    }

    public String getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(String campeonato) {
        this.campeonato = campeonato;
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
        return this.getEquipe();
    }
}
