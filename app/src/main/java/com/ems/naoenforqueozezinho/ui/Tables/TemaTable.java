package com.ems.naoenforqueozezinho.ui.Tables;

import java.util.ArrayList;

public class TemaTable {
    private String tableName;
    private String idTema = "idTema";
    private String tema = "tema";

    public TemaTable () {
        this.tableName = "temas";
    }

    public ArrayList<String> getColumns () {
        ArrayList<String> columns = new ArrayList<String>();
        columns.add(getIdTema() + " VARCHAR");
        columns.add(getTema() + " VARCHAR");
        return columns;
    }

    public String getTableName() {
        return tableName;
    }

    public String getIdTema() {
        return idTema;
    }

    public String getTema() {
        return tema;
    }
}
