package com.example.myapplication.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myapplication.R;
import com.example.myapplication.login.ModeloScore;
import com.example.myapplication.sql.sqlUser;
import com.example.myapplication.login.ModeloUser;

import java.util.ArrayList;

public class AdapterS extends ArrayAdapter<ModeloScore> {

    private final Context context;
    ArrayList<ModeloScore> listaPuntajes;
    sqlUser sql;
    TextView tvUser, tvDificultad, tvScore, tvtipo,tvFecha;

    public AdapterS(Context context, ArrayList<ModeloScore> listaPuntajes) {
        super(context, 0, listaPuntajes);
        this.context = context;
        this.listaPuntajes = listaPuntajes;
        this.sql = new sqlUser(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_puntaje, parent, false);
        }
        ModeloScore puntaje = getItem(position);
        tvUser = convertView.findViewById(R.id.tvUser);
        tvDificultad = convertView.findViewById(R.id.tvDificultad);
        tvScore = convertView.findViewById(R.id.tvScore);
        tvtipo = convertView.findViewById(R.id.tvTipo);
        tvFecha = convertView.findViewById(R.id.tvFecha);

        if (puntaje != null) {
            int totalSegundos = puntaje.getScore();
            int minutos = totalSegundos / 60;
            int segundos = totalSegundos % 60;
            String tiempoFormateado = String.format("%02d:%02d", minutos, segundos);
            ModeloUser usuario = sql.obtenerUsuarioPorId(puntaje.getFkUsuario());
            tvUser.setText(usuario != null ? usuario.getUser() : "Desconocido");
            tvDificultad.setText(puntaje.getDificultad());
            tvScore.setText(tiempoFormateado);
            tvtipo.setText(puntaje.getTipo());
            tvFecha.setText(puntaje.getFecha());
        }
        return convertView;
    }
}
