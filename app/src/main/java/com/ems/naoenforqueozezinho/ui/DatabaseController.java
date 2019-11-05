package com.ems.naoenforqueozezinho.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ems.naoenforqueozezinho.ui.Tables.TemaTable;
import com.ems.naoenforqueozezinho.ui.Tables.WordTable;

import java.util.ArrayList;

public class DatabaseController {
    private Database banco;
    private SQLiteDatabase db;
    TemaTable themeTable = new TemaTable();
    WordTable wordTable = new WordTable();

    public DatabaseController (Context context){
        this.banco = new Database(context);
        this.db = this.banco.getWritableDatabase();
    }

    public String createWord (Word word) {
        ContentValues values = new ContentValues();
        values.put(wordTable.getIdPalavra(), word.getIdPalavra());
        values.put(wordTable.getPalavra(), word.getPalavra());
        values.put(wordTable.getDica(), word.getDica());
        values.put(themeTable.getIdTema(), word.getTemaId());
        Log.i("log", word.getPalavra() + " "
                + word.getIdPalavra() + " "
                + word.getDica() + " "
                + word.getTemaId());
        this.db.insert("palavras", null, values);
        return word.getTemaId();
    }

    public String createTema (Tema tema) {
        ContentValues values = new ContentValues();

        values.put(themeTable.getTema(), tema.getTema());
        values.put(themeTable.getIdTema(), tema.getTemaId());
        this.db.insert("temas", null, values);
        return tema.getTemaId();
    }

    public ArrayList<Word> getWordsList () {
        ArrayList words = new ArrayList<Word>();

        Log.i("log", "Inicio consulta");
        Cursor c = this.db.rawQuery("SELECT " +
                wordTable.getIdPalavraAs() + ", " +
                wordTable.getPalavraAs() + ", " +
                wordTable.getDicaAs() + ", " +
                themeTable.getIdTemaAs() + ", " +
                themeTable.getTemaAs() + " " +
                "FROM " + themeTable.getTableName() + " " +
                "JOIN " + wordTable.getTableName() + " " +
                "ON " + wordTable.getTemaAs() + " = " + themeTable.getIdTemaAs() +  " ",
                null
                );

        Log.i("log", "Registros: " + c.getCount());
        if (c.getCount() <= 0) return words;
        c.moveToFirst();
        do {
            Log.i("log", c.getString(3) + " " + c.getString(4));
            Log.i("log", c.getString(0) + " " + c.getString(1) + " " + c.getString(2));
            Tema tema = new Tema(c.getString(3), c.getString(4));
            Word word = new Word(c.getString(0), c.getString(1), c.getString(2), tema);
            words.add(word);
        } while (c.moveToNext());
        Log.i("log", "Fim consulta");
        return words;
    }

    public void getWordsListLog () {
        ArrayList words = new ArrayList<Word>();
        Log.i("log", "Inicio consulta log");
        Cursor c = this.db.rawQuery("SELECT * " +
                "FROM " + wordTable.getTableName(), null
        );
        Log.i("log", "Registros: " + c.getCount());
        c.moveToFirst();
        do {
            Log.i("log", c.getString(0) + " " + c.getString(1) + " " + c.getString(2) + " " + c.getString(3));
        } while (c.moveToNext());
        Log.i("log", "Fim consulta");
    }

    public ArrayList<Tema> getTemasList () {
        ArrayList temas = new ArrayList<Tema>();
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

    public ArrayList<Tema> getValidTemasList () {
        ArrayList temas = new ArrayList<Tema>();

        Log.i("log", "Inicio consulta");
        Cursor c = this.db.rawQuery("SELECT " +
                        themeTable.getIdTemaAs() + ", " +
                        themeTable.getTemaAs() + " " +
                        "FROM " + wordTable.getTableName()+ " " +
                        "LEFT JOIN " + themeTable.getTableName() + " " +
                        "ON " + wordTable.getTemaAs() + " = " + themeTable.getIdTemaAs() + " ",
                null);
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
        ContentValues values = new ContentValues();
        values.put("tema", theme.getTema());

        String whereClause = themeTable.getIdTema() + " = ? ";
        String[] whereArgs = { theme.getTemaId() };

        Log.i("log", "Inicio consulta");
        int result = this.db.update(themeTable.getTableName(), values, whereClause, whereArgs);
        Log.i("log", "Registros atualizado");
    }

    public void setWordItem (Word word) {
        ContentValues values = new ContentValues();
        values.put(wordTable.getPalavra(), word.getPalavra());
        values.put(wordTable.getDica(), word.getDica());
        values.put(wordTable.getTema(), word.getTemaId());

        String whereClause = wordTable.getIdPalavra() + " = ? ";
        String[] whereArgs = { word.getIdPalavra() };

        Log.i("log", "Inicio consulta");
        int result = this.db.update(wordTable.getTableName(), values, whereClause, whereArgs);
        Log.i("log", "Registros atualizado");
    }

    public void removeThemeItem (String themeId) {
        String whereClause = themeTable.getIdTema() + " = ? ";
        String[] whereArgs = { themeId };

        Log.i("log", "Inicio consulta");
        int result = this.db.delete(themeTable.getTableName(), whereClause, whereArgs);
        Log.i("log", "Registros atualizado");
    }

    public void removeWordItem (String wordid) {
        String whereClause = wordTable.getIdPalavra() + " = ? ";
        String[] whereArgs = { wordid };

        Log.i("log", "Inicio consulta");
        int result = this.db.delete(wordTable.getTableName(), whereClause, whereArgs);
        Log.i("log", "Registros atualizado");
    }

    public ArrayList<Word> getWordsByTheme (String themeid) {
        ArrayList words = new ArrayList<Word>();
        String[] whereArgs = { themeid };

        Log.i("log", "Inicio consulta");
        Cursor c = this.db.rawQuery("SELECT " +
                        wordTable.getIdPalavraAs() + ", " +
                        wordTable.getPalavraAs() + ", " +
                        wordTable.getDicaAs() + ", " +
                        themeTable.getIdTemaAs() + ", " +
                        themeTable.getTemaAs() + " " +
                        "FROM " + themeTable.getTableName() + " " +
                        "JOIN " + wordTable.getTableName() + " " +
                        "ON " + wordTable.getTemaAs() + " = " + themeTable.getIdTemaAs() + " " +
                        "WHERE " + themeTable.getIdTemaAs() + " = ? " + ";",
                whereArgs
        );

        Log.i("log", "Registros: " + c.getCount());
        if (c.getCount() <= 0) return words;
        c.moveToFirst();
        do {
            Log.i("log", c.getString(3) + " " + c.getString(4));
            Log.i("log", c.getString(0) + " " + c.getString(1) + " " + c.getString(2));
            Tema tema = new Tema(c.getString(3), c.getString(4));
            Word word = new Word(c.getString(0), c.getString(1), c.getString(2), tema);
            words.add(word);
        } while (c.moveToNext());
        Log.i("log", "Fim consulta");
        return words;
    }

    public void cleartThemeList () {
        db.execSQL("DELETE FROM " + themeTable.getTableName());
    }

    public void cleartWordsList () {
        db.execSQL("DELETE FROM " + wordTable.getTableName());
    }
}
