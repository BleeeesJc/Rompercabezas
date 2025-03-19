package com.example.myapplication.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.myapplication.sql.sqlUser;

public class Sign extends AppCompatActivity {
    EditText etuser, etpassword;
    TextView btnregistrar;
    sqlUser sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        etuser = findViewById(R.id.etUsername);
        etpassword = findViewById(R.id.etPassword);
        btnregistrar = findViewById(R.id.btnRegistrar);
        sql = new sqlUser(this);

        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }
    private void registrarUsuario() {
        String user = etuser.getText().toString().trim();
        String password = etpassword.getText().toString().trim();

        if (user.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        ModeloUser nuevoUsuario = new ModeloUser();
        nuevoUsuario.setUser(user);
        nuevoUsuario.setPassword(password);
        long id = sql.insertarUsuario(nuevoUsuario);

        if (id > 0) {
            Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
        }
    }
}
