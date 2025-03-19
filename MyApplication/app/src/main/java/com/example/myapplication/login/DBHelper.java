package com.example.myapplication.login;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static int version = 1;
    public static String nameDB = "dbExa";

    public DBHelper(@Nullable Context context) {
        super(context, nameDB, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usuario (" +
                "pkUsuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user TEXT NOT NULL, " +
                "password TEXT NOT NULL " +
                ");");

        db.execSQL("CREATE TABLE puntajes (" +
                "pkPuntaje INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fkUsuario INTEGER NOT NULL, " +
                "dificultad TEXT NOT NULL, " +
                "score INTEGER NOT NULL, " +
                "tipo TEXT NOT NULL, " +
                "fecha TEXT DEFAULT CURRENT_DATE, " +
                "FOREIGN KEY (fkUsuario) REFERENCES usuario(pkUsuario) ON DELETE CASCADE " +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS puntajes");
        db.execSQL("DROP TABLE IF EXISTS usuario");
        onCreate(db);
    }
}