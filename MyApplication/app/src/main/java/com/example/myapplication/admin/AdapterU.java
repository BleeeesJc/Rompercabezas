package com.example.myapplication.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myapplication.R;
import com.example.myapplication.login.ModeloUser;

import java.util.ArrayList;

public class AdapterU extends ArrayAdapter<ModeloUser> {

    private final Context context;
    ArrayList<ModeloUser> listaUsuarios;
    TextView tvuser, tvpassword;

    public AdapterU(Context context, ArrayList<ModeloUser> listaUsuarios) {
        super(context, 0, listaUsuarios);
        this.context = context;
        this.listaUsuarios = listaUsuarios;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_usuario, parent, false);
        }
        ModeloUser usuario = getItem(position);
        tvuser = convertView.findViewById(R.id.tvUser);
        tvpassword = convertView.findViewById(R.id.tvPassword);

        if (usuario != null) {
            tvuser.setText(usuario.getUser());
            tvpassword.setText(usuario.getPassword());
        }
        return convertView;
    }
}