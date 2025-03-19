package com.example.myapplication.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.login.ModeloUser;
import com.example.myapplication.sql.sqlUser;

public class AgregarU_Fragment extends Fragment {
    EditText etuser, etpassword;
    TextView btnagregar;
    sqlUser sql;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar_u, container, false);

        etuser = view.findViewById(R.id.etUsername);
        etpassword = view.findViewById(R.id.etPassword);
        btnagregar = view.findViewById(R.id.btnAgregar);
        sql = new sqlUser(getContext());

        btnagregar.setOnClickListener(v -> {
            String user = etuser.getText().toString().trim();
            String password = etpassword.getText().toString().trim();

            if (user.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT).show();
                return;
            }

            ModeloUser nuevoUsuario = new ModeloUser(0, user, password);
            long resultado = sql.insertarUsuario(nuevoUsuario);

            if (resultado != -1) {
                Toast.makeText(getContext(), "Usuario agregado exitosamente", Toast.LENGTH_SHORT).show();
                etuser.setText("");
                etpassword.setText("");
            } else {
                Toast.makeText(getContext(), "Error al agregar usuario", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
