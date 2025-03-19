package com.example.myapplication.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.myapplication.login.DBHelper;
import com.example.myapplication.login.ModeloScore;

import java.util.ArrayList;

public class sqlScore extends DBHelper {
    Context context;
    DBHelper helper;
    SQLiteDatabase db;

    public sqlScore(@Nullable Context context) {
        super(context);
        this.context = context;
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }
    public long insertarPuntaje(ModeloScore puntaje) {
        long resultado = -1;
        try {
            ContentValues valores = new ContentValues();
            valores.put("fkUsuario", puntaje.getFkUsuario());
            valores.put("dificultad", puntaje.getDificultad());
            valores.put("score", puntaje.getScore());
            valores.put("tipo",puntaje.getTipo());
            valores.put("fecha", puntaje.getFecha());

            resultado = db.insert("puntajes", null, valores);
        } catch (Exception e) {
            Log.i("base", "Error al insertar puntaje: " + e.getMessage());
        }
        return resultado;
    }

    public boolean eliminarPuntaje(int pkPuntaje) {
        boolean res = false;
        try {
            db.execSQL("DELETE FROM puntajes WHERE pkPuntaje = '" + pkPuntaje + "'");
            res = true;
        } catch (Exception e) {
            Log.i("base", "Error al eliminar puntaje: " + e.getMessage());
            res = false;
        }
        return res;
    }

    public boolean actualizarPuntaje(int pkPuntaje, String nuevaDificultad, int nuevoScore, String nuevoTipo,String nuevaFecha) {
        boolean resultado = false;
        try {
            ContentValues valores = new ContentValues();
            valores.put("dificultad", nuevaDificultad);
            valores.put("score", nuevoScore);
            valores.put("tipo",nuevoTipo);
            valores.put("fecha", nuevaFecha);
            db.update("puntajes", valores, "pkPuntaje = ?", new String[]{String.valueOf(pkPuntaje)});
            resultado = true;
        } catch (Exception e) {
            Log.i("base", "Error al actualizar puntaje: " + e.getMessage());
        }
        return resultado;
    }

    public ArrayList<ModeloScore> listarPuntajes() {
        ArrayList<ModeloScore> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM puntajes", null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    int fkUsuario = cursor.getInt(1);
                    String dificultad = cursor.getString(2);
                    int score = cursor.getInt(3);
                    String tipo = cursor.getString(4);
                    String fecha = cursor.getString(5);

                    lista.add(new ModeloScore(id, fkUsuario, dificultad, score, tipo,fecha));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("base", "Error al listar puntajes: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lista;
    }

    public ArrayList<ModeloScore> listarPuntajesConFiltros(String orden, String dificultad, String puntaje, String tipo) {
        ArrayList<ModeloScore> lista = new ArrayList<>();
        Cursor cursor = null;
        String ordenSQL = "ASC";

        if (puntaje.equals("Mayor")) {
            ordenSQL = "DESC";
        } else if (puntaje.equals("Menor")) {
            ordenSQL = "ASC";
        }

        String query = "SELECT * FROM puntajes";
        ArrayList<String> args = new ArrayList<>();
        boolean whereAdded = false;

        if (!dificultad.equals("Dificultad")) {
            query += " WHERE dificultad = ?";
            args.add(dificultad);
            whereAdded = true;
        }

        if (!tipo.equals("Tipo")) {
            query += (whereAdded ? " AND" : " WHERE") + " tipo = ?";
            args.add(tipo);
        }

        query += " ORDER BY score " + ordenSQL;

        if (orden.equals("A-Z") || orden.equals("Z-A")) {
            query += ", fkUsuario " + (orden.equals("A-Z") ? "ASC" : "DESC");
        }

        try {
            cursor = db.rawQuery(query, args.toArray(new String[0]));

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    int fkUsuario = cursor.getInt(1);
                    String dif = cursor.getString(2);
                    int score = cursor.getInt(3);
                    String tipoResult = cursor.getString(4);
                    String fecha = cursor.getString(5);

                    lista.add(new ModeloScore(id, fkUsuario, dif, score, tipoResult, fecha));
                } while (cursor.moveToNext());
            } else {
                Log.i("base", "No se encontraron puntajes con los filtros especificados.");
            }
        } catch (Exception e) {
            Log.i("base", "Error al listar puntajes con filtros: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lista;
    }
}
