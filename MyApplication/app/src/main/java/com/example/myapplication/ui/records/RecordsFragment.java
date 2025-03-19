package com.example.myapplication.ui.records;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

public class RecordsFragment extends Fragment {

    Spinner spnombres, spdificultad, spmayor, sptipo;
    ListaR_Fragment listaRFragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_records, container, false);
        spnombres = view.findViewById(R.id.spNombres);
        spdificultad = view.findViewById(R.id.spDificultad);
        spmayor = view.findViewById(R.id.spMayor);
        sptipo = view.findViewById(R.id.spTipo);

        ArrayAdapter<String> adapterNombres = new ArrayAdapter<>(requireContext(), R.layout.item_record, new String[]{"Nombre", "A-Z", "Z-A"});
        adapterNombres.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnombres.setAdapter(adapterNombres);

        ArrayAdapter<String> adapterDificultad = new ArrayAdapter<>(requireContext(), R.layout.item_record, new String[]{"Dificultad", "Fácil", "Medio", "Difícil"});
        adapterDificultad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spdificultad.setAdapter(adapterDificultad);

        ArrayAdapter<String> adapterMayor = new ArrayAdapter<>(requireContext(), R.layout.item_record, new String[]{"Tiempo", "Mayor", "Menor"});
        adapterMayor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spmayor.setAdapter(adapterMayor);

        ArrayAdapter<String> adapterTipo = new ArrayAdapter<>(requireContext(), R.layout.item_record, new String[]{"Tipo", "2x2", "3x3", "4x4"});
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sptipo.setAdapter(adapterTipo);

        if (savedInstanceState == null) {
            listaRFragment = new ListaR_Fragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.frCont, listaRFragment)
                    .commit();
        }

        AdapterView.OnItemSelectedListener filtroListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedNombre = spnombres.getSelectedItem().toString();
                String selectedDificultad = spdificultad.getSelectedItem().toString();
                String selectedPuntaje = spmayor.getSelectedItem().toString();
                String selectedTipo = sptipo.getSelectedItem().toString();

                actualizarLista(selectedNombre, selectedDificultad, selectedPuntaje, selectedTipo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        };

        spnombres.setOnItemSelectedListener(filtroListener);
        spdificultad.setOnItemSelectedListener(filtroListener);
        spmayor.setOnItemSelectedListener(filtroListener);
        sptipo.setOnItemSelectedListener(filtroListener);

        return view;
    }

    public void actualizarLista(String nombre, String dificultad, String puntaje, String tipo) {
        String orden = nombre.equals("A-Z") || nombre.equals("Z-A") ? nombre : "A-Z";
        Log.d("filtro", "Orden: " + orden + ", Dificultad: " + dificultad + ", Tiempo: " + puntaje + ", Tipo: " + tipo);
        listaRFragment.actualizarLista(orden, dificultad, puntaje, tipo);
    }
}
