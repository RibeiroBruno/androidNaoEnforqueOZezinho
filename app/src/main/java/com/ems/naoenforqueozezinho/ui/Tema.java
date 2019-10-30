package com.ems.naoenforqueozezinho.ui;

import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Tema implements Serializable {
    private String temaId;
    private String tema;
    SQLiteDatabase db;

    /**
     * Método construtor da classe
     *
     * @param tema
     */
    public Tema(String temaId, String tema) {
        this.setTema(tema);
        this.temaId = temaId;
    }

    public Tema(String tema) {
        this.setTema(tema);
        this.temaId = this.generateId();
    }

    @Override
    public String toString() {
        return this.getTema();
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getTemaId() {
        return temaId;
    }
}
