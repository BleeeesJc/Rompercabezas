package com.example.myapplication.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.login.ModeloScore;
import com.example.myapplication.sql.sqlScore;

import java.util.ArrayList;


public class ListaS_Fragment extends Fragment {
    ListView listView;
    private ArrayList<ModeloScore> listaPuntajes;
    private AdapterS adapterS;
    sqlScore sql;
    EditText etscore,etfecha;
    Spinner spdificultad,sptipo;

    public ListaS_Fragment() {

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_s, container, false);
        listView = view.findViewById(R.id.lvData);
        sql = new sqlScore(getContext());
        listaPuntajes = sql.listarPuntajes();
        adapterS = new AdapterS(getContext(), listaPuntajes);
        listView.setAdapter(adapterS);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ModeloScore puntajeSeleccionado = listaPuntajes.get(position);
                editar(puntajeSeleccionado);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final ModeloScore puntuajeSeleccionado = listaPuntajes.get(position);
                boolean eliminado = sql.eliminarPuntaje(puntuajeSeleccionado.getpkPuntuaje());
                if (eliminado) {
                    listaPuntajes.remove(position);
                    adapterS.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Puntuaje eliminado ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error al eliminar el puntuaje", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        return view;
    }
    private void editar(ModeloScore puntajeSeleccionado) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Modificar Puntaje");
        View dialogView = getLayoutInflater().inflate(R.layout.editar_score, null);
        builder.setView(dialogView);
        spdificultad = dialogView.findViewById(R.id.spDificultad);
        sptipo = dialogView.findViewById(R.id.spTipo);
        etscore = dialogView.findViewById(R.id.etScore);
        etfecha=dialogView.findViewById(R.id.etFecha);
        etscore.setText(String.valueOf(puntajeSeleccionado.getScore()));
        etfecha.setText(puntajeSeleccionado.getFecha());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.dificultades, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spdificultad.setAdapter(adapter);

        ArrayAdapter<CharSequence> adaptertipo = ArrayAdapter.createFromResource(requireContext(),
                R.array.tamanos, android.R.layout.simple_spinner_item);
        adaptertipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sptipo.setAdapter(adaptertipo);

        int pos = adapter.getPosition(puntajeSeleccionado.getDificultad());
        int tip = adaptertipo.getPosition(puntajeSeleccionado.getTipo());
        spdificultad.setSelection(pos);
        sptipo.setSelection(tip);
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nuevaDificultad = spdificultad.getSelectedItem().toString();
                String nuevoTipo = sptipo.getSelectedItem().toString();
                int nuevoScore = Integer.parseInt(etscore.getText().toString());
                String nuevaFecha = etfecha.getText().toString();
                puntajeSeleccionado.setDificultad(nuevaDificultad);
                puntajeSeleccionado.setScore(nuevoScore);
                puntajeSeleccionado.setTipo(nuevoTipo);
                puntajeSeleccionado.setFecha(nuevaFecha);
                try {
                    boolean actualizado = sql.actualizarPuntaje(puntajeSeleccionado.getpkPuntuaje(), nuevaDificultad, nuevoScore,nuevoTipo,nuevaFecha);
                    if (actualizado) {
                        adapterS.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Puntaje actualizado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Error al actualizar el puntaje", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error al actualizar el puntaje", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}