package com.ems.naoenforqueozezinho.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;
import com.ems.naoenforqueozezinho.ui.Tables.*;

public class Word implements Serializable {
    private String idPalavra;
    private String temaId;
    private String palavra;
    private String dica;
    private String temaString;

    /**
     * Método construtor da classe
     *
     * @param tema
     * @param palavra
     * @param dica
     */
    public Word(String palavra, String dica, Tema tema) {
        this.setPalavra(palavra);
        this.setDica(dica);
        this.setTemaId(tema.getTemaId());
        this.idPalavra = this.generateId();
        this.setTemaString(tema.getTemaId());
    }

    public Word(String idPalavra, String palavra, String dica, Tema tema) {
        this.idPalavra = idPalavra;
        this.setPalavra(palavra);
        this.setDica(dica);
        this.setTemaId(tema.getTemaId());
        this.setTemaString(tema.getTema());
    }

    @Override
    public String toString() {
        return " - Palavra: " + this.palavra + "\n - Tema: " + this.getTemaString() + "\n - Dica: " + this.dica;
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    public String getIdPalavra() {
        return idPalavra;
    }

    public String getTemaId() {
        return temaId;
    }

    public String getPalavra() {
        return palavra;
    }

    public String getDica() {
        return dica;
    }

    public void setTemaId(String temaId) {
        this.temaId = temaId;
    }

    public void setPalavra(String palavra) {
        this.palavra = palavra;
    }

    public void setDica(String dica) {
        this.dica = dica;
    }

    public String getTemaString() {
        return temaString;
    }

    public void setTemaString(String temaString) {
        this.temaString = temaString;
    }
}
