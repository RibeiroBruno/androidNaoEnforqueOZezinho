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

    public ArrayList<Tema> getTemasList () {
        ArrayList temas = new ArrayList<Tema>();
        TemaTable themeTable = new TemaTable();
        String[] campos =  {themeTable.getIdTema(), themeTable.getTema()};

        Log.i("log", "Inicio consulta");
        Cursor c = this.db.query(themeTable.getTableName(), campos, null, null, null, null, null, null);
        Log.i("log", "Registros: " + c.getCount());

        if (c.getCount() <= 0) return temas;
        c.moveToFirst();
        do {
            Log.i("log", c.getString(1));
            Tema tema = new Tema(c.getString(0), c.getString(1));
            temas.add(tema);
        } while (c.moveToNext());
        Log.i("log", "Fim consulta");
        return temas;
    }

    public void setThemeItem (Tema theme) {
        TemaTable themeTable = new TemaTable();
        ContentValues values = new ContentValues();
        values.put("tema", theme.getTema());

        String whereClause = themeTable.getIdTema() + " = ? ";
        String[] whereArgs = { theme.getTemaId() };

        Log.i("log", "Inicio consulta");
        int result = this.db.update(themeTable.getTableName(), values, whereClause, whereArgs);
        Log.i("log", "Registros atualizado");
    }

    public void removeThemeItem (String themeId) {
        TemaTable themeTable = new TemaTable();
        String whereClause = themeTable.getIdTema() + " = ? ";
        String[] whereArgs = { themeId };

        Log.i("log", "Inicio consulta");
        int result = this.db.delete(themeTable.getTableName(), whereClause, whereArgs);
        Log.i("log", "Registros atualizado");
    }

    public void cleartThemeList () {
        TemaTable themeTable = new TemaTable();
        db.execSQL("DELETE FROM " + themeTable.getTableName());
    }
}
