package com.example.myapplication.login;

import com.example.myapplication.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.sql.sqlUser;

import java.util.ArrayList;

public class Login extends AppCompatActivity {

    EditText etusername, etpassword;
    TextView btnlogin,tvregister;
    sqlUser sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etusername = findViewById(R.id.etUsername);
        etpassword = findViewById(R.id.etPassword);
        btnlogin = findViewById(R.id.btnLogin);
        tvregister = findViewById(R.id.tvRegister);
        sql = new sqlUser(this);

        btnlogin.setOnClickListener(view -> {
            String username = etusername.getText().toString();
            String password = etpassword.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Por favor ingrese usuario y contraseña", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean validLogin = validateUserCredentials(username, password);

            if (validLogin) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(Login.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        });
        tvregister.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, Sign.class);
            startActivity(intent);
        });
    }

    private boolean validateUserCredentials(String username, String password) {
        ArrayList<ModeloUser> users = sql.listarUsuarios();

        for (ModeloUser user : users) {
            if (user.getUser().equals(username) && user.getPassword().equals(password)) {
                Sesion.setUsuarioId(Login.this, user.getPkUsuario());
                return true;
            }
        }
        return false;
    }
}