package com.ems.naoenforqueozezinho.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ems.naoenforqueozezinho.ui.Tables.TemaTable;

import java.util.ArrayList;

public class DatabaseController {
    private Database banco;
    private SQLiteDatabase db;

    public DatabaseController (Context context){
        this.banco = new Database(context);
        this.db = this.banco.getWritableDatabase();
    }

    public String createTema (Tema tema) {
        ContentValues values = new ContentValues();
        values.put("tema", tema.getTema());
        values.put("idTema", tema.getTemaId());
        this.db.insert("temas", null, values);
        return tema.getTemaId();
    }

    public ArrayList<String> getTemasList () {
        ArrayList temas = new ArrayList<String>();
        TemaTable themeTable = new TemaTable();
        String[] campos =  {themeTable.getIdTema(),themeTable.getTema()};
        //ORDER BY tema ASC
        Cursor c = db.query(themeTable.getTableName(), campos, null, null, null, null, null, null);
        while (c.moveToNext()) {
            Tema tema = new Tema(c.getString(0), c.getString(1));
            temas.add(tema.getTema());
        }
        return temas;
    }
}
