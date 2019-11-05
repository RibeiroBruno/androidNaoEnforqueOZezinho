package com.ems.naoenforqueozezinho.ui.Tables;

import java.util.ArrayList;

public class WordTable {
    private String tableName;
    private String idPalavra = "idPalavra";
    private String palavra = "palavra";
    private String dica = "dica";
    private String idTema = "idTema";

    public WordTable() {
        this.tableName = "palavras";
    }

    public ArrayList<String> getColumns () {
        ArrayList<String> columns = new ArrayList<String>();
        columns.add(getIdPalavra() + " VARCHAR");
        columns.add(getPalavra() + " VARCHAR");
        columns.add(getDica() + " VARCHAR");
        columns.add(getTema() + " VARCHAR");
        return columns;
    }

    public String getTableName() {
        return tableName;
    }

    public String getIdPalavra() {
        return idPalavra;
    }

    public String getIdPalavraAs() {
        return tableName + "." + idPalavra;
    }

    public String getPalavra() {
        return palavra;
    }

    public String getPalavraAs() {
        return tableName + "." + palavra;
    }

    public String getDica() {
        return dica;
    }

    public String getDicaAs() {
        return tableName + "." + dica;
    }

    public String getTema() {
        return idTema;
    }

    public String getTemaAs() {
        return tableName + "." + idTema;
    }
}
