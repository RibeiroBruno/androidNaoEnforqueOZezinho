package com.ems.naoenforqueozezinho.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ems.naoenforqueozezinho.ui.Tables.WordTable;
import com.ems.naoenforqueozezinho.ui.Tables.TemaTable;

import java.util.UUID;

public class Database extends SQLiteOpenHelper {
    private static final String NOME_BANCO = "db_forca";
    private static final TemaTable themeTable = new TemaTable();
    private static final WordTable wordTable = new WordTable();
    private static final int VERSAO = 1;
    private static final String TAG = "MyActivity";

    public Database(Context context){
        super(context, NOME_BANCO,null,VERSAO);
    }

    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String commandCreateWords = "CREATE TABLE IF NOT EXISTS " + wordTable.getTableName() + " ( " +
                TextUtils.join(", ", wordTable.getColumns()) + " );";
        String commandCreateThemes = "CREATE TABLE IF NOT EXISTS " + themeTable.getTableName() + " ( " +
                TextUtils.join(", ", themeTable.getColumns()) + " );";
        db.execSQL("DROP TABLE " + themeTable.getTableName());
        db.execSQL("DROP TABLE " + wordTable.getTableName());
        Log.i(TAG,"Banco : " + commandCreateWords);
        Log.i(TAG,"Banco : " + commandCreateThemes);
        db.execSQL(commandCreateWords);
        db.execSQL(commandCreateThemes);
        System.out.println("Banco Iniciado");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}