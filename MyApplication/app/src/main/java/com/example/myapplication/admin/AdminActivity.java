package com.example.myapplication.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.myapplication.R;

public class AdminActivity extends AppCompatActivity {
    TextView tvlistauser, tvagregaruser,tvlistascore,tvagregarscore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        tvlistauser = findViewById(R.id.tvListarUsuarios);
        tvagregaruser = findViewById(R.id.tvAgregarUsuario);
        tvlistascore=findViewById(R.id.tvListarPuntajes);
        tvagregarscore=findViewById(R.id.tvAgregarPuntaje);
        tvlistauser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListaU_Fragment listauFragment = new ListaU_Fragment();
                cargarFragmento(listauFragment);
            }
        });
        tvagregaruser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarU_Fragment agregaruFragment = new AgregarU_Fragment();
                cargarFragmento(agregaruFragment);
            }
        });
        tvlistascore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListaS_Fragment listasFragment = new ListaS_Fragment();
                cargarFragmento(listasFragment);
            }
        });
        tvagregarscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarS_Fragment agregarsFragment = new AgregarS_Fragment();
                cargarFragmento(agregarsFragment);
            }
        });
    }
    public void cargarFragmento(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frCont, fragment);
        transaction.commit();
    }
}
