package com.example.myapplication.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.myapplication.login.DBHelper;
import com.example.myapplication.login.ModeloUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class sqlUser extends DBHelper {
    Context context;
    DBHelper helper;
    SQLiteDatabase db;

    public sqlUser(@Nullable Context context) {
        super(context);
        this.context = context;
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public String obtenerFechaActual() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    public long insertarUsuario(ModeloUser usuario) {
        long resultado = -1;
        try {
            ContentValues valores = new ContentValues();
            valores.put("user", usuario.getUser());
            valores.put("password", usuario.getPassword());

            resultado = db.insert("usuario", null, valores);
        } catch (Exception e) {
            Log.i("base", "Error al insertar usuario: " + e.getMessage());
        }
        return resultado;
    }

    public boolean eliminarUsuario(int pkUsuario) {
        boolean res = false;
        try {
            db.execSQL("DELETE FROM usuario WHERE pkUsuario = '" + pkUsuario + "'");
            res = true;
        } catch (Exception e) {
            Log.i("base", "Error al eliminar usuario: " + e.getMessage());
        }
        return res;
    }

    public boolean actualizarUsuario(int pkUsuario, String nuevoUser, String nuevaPassword) {
        boolean resultado = false;
        try {
            ContentValues valores = new ContentValues();
            valores.put("user", nuevoUser);
            valores.put("password", nuevaPassword);

            // Realizamos la actualización y obtenemos el número de filas afectadas
            int filasAfectadas = db.update("usuario", valores, "pkUsuario = ?", new String[]{String.valueOf(pkUsuario)});

            // Si se actualizó al menos una fila, el resultado será verdadero
            if (filasAfectadas > 0) {
                resultado = true;
                Log.d("ActualizarUsuario", "Se actualizó el usuario con pkUsuario: " + pkUsuario);
            } else {
                Log.d("ActualizarUsuario", "No se encontraron usuarios para actualizar con pkUsuario: " + pkUsuario);
            }
        } catch (Exception e) {
            Log.e("ActualizarUsuario", "Error al actualizar usuario: " + e.getMessage());
        }
        return resultado;
    }

    public ArrayList<ModeloUser> listarUsuarios() {
        ArrayList<ModeloUser> lista = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM usuario", null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String user = cursor.getString(1);
                    String password = cursor.getString(2);

                    lista.add(new ModeloUser(id, user, password));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i("base", "Error al listar usuarios: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lista;
    }

    public ModeloUser obtenerUsuarioPorId(int pkUsuario) {
        ModeloUser usuario = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM usuario WHERE pkUsuario = ?", new String[]{String.valueOf(pkUsuario)});

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(0);
                String user = cursor.getString(1);
                String password = cursor.getString(2);
                usuario = new ModeloUser(id, user, password);
            }
        } catch (Exception e) {
            Log.i("base", "Error al obtener usuario por id: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return usuario;
    }
}
