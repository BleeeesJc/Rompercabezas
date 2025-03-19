package com.example.myapplication.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.login.ModeloScore;
import com.example.myapplication.login.ModeloUser;
import com.example.myapplication.sql.sqlScore;
import com.example.myapplication.sql.sqlUser;

import java.util.ArrayList;

public class AgregarS_Fragment extends Fragment {

    Spinner spusuario, spdificultad, sptipo;
    EditText  etscore, etfecha;
    TextView btnagregar;
    sqlUser sqlUser;
    sqlScore sqlScore;
    private ArrayList<ModeloUser> listaUsuarios;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_s, container, false);
        spusuario = view.findViewById(R.id.spUsuario);
        spdificultad = view.findViewById(R.id.spDificultad);
        sptipo = view.findViewById(R.id.spTipo);
        etscore = view.findViewById(R.id.etScore);
        etfecha = view.findViewById(R.id.etFecha);
        btnagregar = view.findViewById(R.id.btnAgregarPuntaje);

        sqlUser = new sqlUser(getContext());
        sqlScore = new sqlScore(getContext());
        cargarUsuarios();
        cargarsp();
        btnagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarPuntaje();
            }
        });

        return view;
    }

    private void cargarUsuarios() {
        listaUsuarios = sqlUser.listarUsuarios();
        if (!listaUsuarios.isEmpty()) {
            ArrayList<String> nombresUsuarios = new ArrayList<>();
            for (ModeloUser user : listaUsuarios) {
                nombresUsuarios.add(user.getUser());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_spinner_item, nombresUsuarios);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spusuario.setAdapter(adapter);
        }
    }

    private void agregarPuntaje() {
        String dificultad = spdificultad.getSelectedItem().toString();
        String tipo = sptipo.getSelectedItem().toString();
        String fecha = etfecha.getText().toString();
        String scoreStr = etscore.getText().toString();
        int score = -1;
        if (dificultad.isEmpty() || fecha.isEmpty() || scoreStr.isEmpty() || tipo.isEmpty()) {
            Toast.makeText(getContext(), "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        score = Integer.parseInt(scoreStr);
        int fkUsuario = listaUsuarios.get(spusuario.getSelectedItemPosition()).getPkUsuario();
        ModeloScore puntaje = new ModeloScore(0, fkUsuario, dificultad, score, tipo, fecha);
        long resultado = sqlScore.insertarPuntaje(puntaje);

        if (resultado != -1) {
            Toast.makeText(getContext(), "Puntaje agregado exitosamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Error al agregar el puntaje", Toast.LENGTH_SHORT).show();
        }
    }
    private void cargarsp() {
        String[] dificultades = {"Fácil", "Medio", "Difícil"};
        ArrayAdapter<String> adapterDificultad = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, dificultades);
        adapterDificultad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spdificultad.setAdapter(adapterDificultad);

        String[] tipos = {"2x2", "3x3", "4x4"};
        ArrayAdapter<String> adaptertipo = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, tipos);
        adaptertipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sptipo.setAdapter(adaptertipo);
    }
}
