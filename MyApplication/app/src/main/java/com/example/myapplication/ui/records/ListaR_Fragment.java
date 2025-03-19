package com.example.myapplication.ui.records;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.myapplication.R;
import com.example.myapplication.login.ModeloScore;
import com.example.myapplication.sql.sqlScore;

import java.util.ArrayList;


public class ListaR_Fragment extends Fragment {
    private ListView listView;
    private ArrayList<ModeloScore> listaPuntajes;
    private AdapterR adapterR;
    private sqlScore sql;

    public ListaR_Fragment() {

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_s, container, false);
        listView = view.findViewById(R.id.lvData);
        sql = new sqlScore(getContext());
        listaPuntajes = sql.listarPuntajes();
        adapterR = new AdapterR(getContext(), listaPuntajes);
        listView.setAdapter(adapterR);

        return view;

    }
    public void actualizarLista(String orden, String dificultad, String puntaje,String tipo) {
        listaPuntajes = sql.listarPuntajesConFiltros(orden, dificultad, puntaje,tipo);
        if (adapterR != null) {
            adapterR.clear();
            adapterR.addAll(listaPuntajes);
            adapterR.notifyDataSetChanged();
        }
    }
}