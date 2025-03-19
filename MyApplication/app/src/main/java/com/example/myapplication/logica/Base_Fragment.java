package com.example.myapplication.logica;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.example.myapplication.login.ModeloScore;
import com.example.myapplication.login.Sesion;
import com.example.myapplication.sql.sqlScore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;


public class Base_Fragment extends Fragment {
    Map<String, Integer> coloresOriginales = new HashMap<>();
    TextView btgenerar,btia;
    ArrayList<TextView> vNodo = new ArrayList<>();
    ArrayList<TextView> vDisplay = new ArrayList<>();
    ArrayList<String> pGenerado = new ArrayList<>();
    EditText ettiempo;
    int segundos = 0;
    Handler handler = new Handler();
    boolean contador= false;
    boolean resolviendo = false;
    boolean usoResolver = false;

    private void contador() {
        contador = true;
        btgenerar.setEnabled(false);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (contador) {
                    ettiempo.setText(String.format("%02d:%02d", segundos / 60, segundos % 60));
                    segundos++;
                    handler.postDelayed(this, 1000);
                }
            }
        }, 10);
    }
    private void swap(int x, int y ) {
        String text = vNodo.get(x).getText().toString();
        Drawable color = vNodo.get(x).getBackground();
        vNodo.get(x).setText(vNodo.get(y).getText().toString());
        vNodo.get(x).setBackground(vNodo.get(y).getBackground());
        vNodo.get(y).setText(text);
        vNodo.get(y).setBackground(color);
    }

    private void generar() {
        new Thread(() -> {
            List<String> estado = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "X");
            List<String> estadoTemporal = new ArrayList<>(estado);
            Random random = new Random();

            String dificultad = getArguments() != null ? getArguments().getString("dificultad", "Fácil") : "Fácil";
            int movimientos;

            switch (dificultad) {
                case "Medio":
                    movimientos = 80;
                    break;
                case "Difícil":
                    movimientos = 300;
                    break;
                default:
                    movimientos = 20;
            }

            for (int i = 0; i < movimientos; i++) {
                List<List<String>> vecinos = obtenerVecinos(estadoTemporal);
                estadoTemporal = vecinos.get(random.nextInt(vecinos.size()));
            }

            List<String> nuevoEstado = new ArrayList<>(estadoTemporal);
            requireActivity().runOnUiThread(() -> {
                pGenerado.clear();
                pGenerado.addAll(nuevoEstado);
                actualizarDisplay(nuevoEstado);
            });
        }).start();
    }
    private void actualizarDisplay(List<String> estado) {
        for (int i = 0; i < vDisplay.size(); i++) {
            String nuevoValor = estado.get(i);
            vDisplay.get(i).setText(nuevoValor);
            vDisplay.get(i).setBackgroundColor(nuevoValor.equals("X") ? Color.WHITE : coloresOriginales.getOrDefault(nuevoValor, Color.DKGRAY));
        }
    }

    private void resolver() {
        resolviendo = true;
        usoResolver = true;
        new Thread(() -> {
            List<String> estadoInicial = new ArrayList<>();
            for (TextView tv : vNodo) estadoInicial.add(tv.getText().toString());
            List<List<String>> solucion = aStar(estadoInicial, pGenerado);
            requireActivity().runOnUiThread(() -> {
                if (solucion != null) animar(solucion);
                else Log.i("valen", resolviendo ? "No se encontró solución" : "Resolución detenida");
            });
        }).start();
    }

    private List<List<String>> aStar(List<String> inicio, List<String> objetivo) {
        PriorityQueue<Nodo> abiertos = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        Set<List<String>> cerrados = new HashSet<>();
        abiertos.add(new Nodo(inicio, null, 0, heuristica(inicio, objetivo)));

        while (!abiertos.isEmpty()) {
            Nodo actual = abiertos.poll();
            assert actual != null;
            if (actual.estado.equals(objetivo)) return reconstruir(actual);
            cerrados.add(actual.estado);

            for (List<String> vecino : obtenerVecinos(actual.estado)) {
                if (!cerrados.contains(vecino)) abiertos.add(new Nodo(vecino, actual, actual.g + 1, heuristica(vecino, objetivo) + actual.g + 1));
            }
        }
        return null;
    }

    private int heuristica(List<String> estado, List<String> objetivo) {
        int distancia = 0;
        for (int i = 0; i < estado.size(); i++) {
            if (!estado.get(i).equals("X")) {
                int indexObjetivo = objetivo.indexOf(estado.get(i));
                distancia += Math.abs(i / 3 - indexObjetivo / 3) + Math.abs(i % 3 - indexObjetivo % 3);
            }
        }
        return distancia;
    }

    private List<List<String>> obtenerVecinos(List<String> estado) {
        List<List<String>> vecinos = new ArrayList<>();
        int indexX = estado.indexOf("X");
        int[] movimientos = {-3, 3, -1, 1};
        for (int mov : movimientos) {
            int nuevoIndex = indexX + mov;
            if (nuevoIndex >= 0 && nuevoIndex < estado.size() && (indexX % 3 != 2 || mov != 1) && (indexX % 3 != 0 || mov != -1)) {
                List<String> nuevoEstado = new ArrayList<>(estado);
                Collections.swap(nuevoEstado, indexX, nuevoIndex);
                vecinos.add(nuevoEstado);
            }
        }
        return vecinos;
    }

    private List<List<String>> reconstruir(Nodo nodo) {
        List<List<String>> camino = new ArrayList<>();
        while (nodo != null) {
            camino.add(nodo.estado);
            nodo = nodo.padre;
        }
        Collections.reverse(camino);
        return camino;
    }

    private void animar(List<List<String>> solucion) {
        final int[] paso = {0};
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!resolviendo) return;

                if (paso[0] < solucion.size()) {
                    List<String> estado = solucion.get(paso[0]);
                    for (int i = 0; i < vNodo.size(); i++) {
                        String valor = estado.get(i);
                        vNodo.get(i).setText(valor);
                        int color = coloresOriginales.getOrDefault(valor, Color.DKGRAY);
                        vNodo.get(i).setBackgroundColor(valor.equals("X") ? Color.WHITE : color);
                    }
                    paso[0]++;
                    handler.postDelayed(this, 500);
                } else {
                    terminar();
                }
            }
        });
    }
    public void verificar() {
        List<String> estadoActual = new ArrayList<>();
        for (TextView tv : vNodo) {
            estadoActual.add(tv.getText().toString());
        }
        if (estadoActual.equals(pGenerado)) {
            terminar();
        }
    }
    private void terminar() {
        contador = false;
        if (usoResolver) {
            usoResolver = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("¡Inténtalo de nuevo!");
            builder.setMessage("Debes completar el rompecabezas sin usar la solución automática.");
            builder.setPositiveButton("Aceptar", (dialog, which) -> {
                dialog.dismiss();
                reiniciarJuego();
            });
            builder.show();
            return;
        }

        String tiempoFinal = ettiempo.getText().toString();
        int tiempoSegundos = segundos;
        int usuarioId = Sesion.getUsuarioId(requireContext());
        String dificultad = getArguments() != null ? getArguments().getString("dificultad", "Fácil") : "Fácil";
        String tipo = "3x3";
        String fecha = obtenerFechaActual();

        ModeloScore puntaje = new ModeloScore(0, usuarioId, dificultad, tiempoSegundos, tipo, fecha);
        sqlScore sql = new sqlScore(getContext());
        long resultado = sql.insertarPuntaje(puntaje);

        if (resultado != -1) {
            Log.i("valen", "Puntaje guardado con éxito.");
        } else {
            Log.e("valen", "Error al guardar el puntaje.");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("¡Felicidades!");
        builder.setMessage("Has completado el rompecabezas en " + tiempoFinal);
        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            dialog.dismiss();
            reiniciarJuego();
        });
        builder.show();
        btgenerar.setEnabled(true);
    }

    private String obtenerFechaActual() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void reiniciarJuego() {
        segundos = 0;
        ettiempo.setText("00:00");
        for (TextView tv : vNodo) {
            String valor = tv.getText().toString();
            int color = coloresOriginales.getOrDefault(valor, Color.DKGRAY);
            tv.setBackgroundColor(valor.equals("X") ? Color.WHITE : color);
        }
        generar();
    }

    private static class Nodo {
        List<String> estado;
        Nodo padre;
        int g, f;
        Nodo(List<String> estado, Nodo padre, int g, int f) { this.estado = estado; this.padre = padre; this.g = g; this.f = f; }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_base, container, false);
        ettiempo = view.findViewById(R.id.etTiempo);
        vNodo.add(view.findViewById(R.id.tvA));
        vNodo.add(view.findViewById(R.id.tvB));
        vNodo.add(view.findViewById(R.id.tvC));
        vNodo.add(view.findViewById(R.id.tvD));
        vNodo.add(view.findViewById(R.id.tvE));
        vNodo.add(view.findViewById(R.id.tvX));
        vNodo.add(view.findViewById(R.id.tvG));
        vNodo.add(view.findViewById(R.id.tvH));
        vNodo.add(view.findViewById(R.id.tvF));
        vDisplay.add(view.findViewById(R.id.tv0));
        vDisplay.add(view.findViewById(R.id.tv1));
        vDisplay.add(view.findViewById(R.id.tv2));
        vDisplay.add(view.findViewById(R.id.tv3));
        vDisplay.add(view.findViewById(R.id.tv4));
        vDisplay.add(view.findViewById(R.id.tv5));
        vDisplay.add(view.findViewById(R.id.tv6));
        vDisplay.add(view.findViewById(R.id.tv7));
        vDisplay.add(view.findViewById(R.id.tv8));

        for (TextView tv : vDisplay) {
            String valor = tv.getText().toString();
            coloresOriginales.put(valor, ((ColorDrawable) tv.getBackground()).getColor());
        }

        String[] conexiones = {
                "1,3",
                "0,2,4",
                "1,5",
                "0,4,6",
                "1,3,5,7",
                "2,4,8",
                "3,7",
                "4,6,8",
                "5,7"
        };
        for (int i = 0; i < vNodo.size(); i++) {
            final int j = i;
            vNodo.get(j).setOnClickListener(v -> {
                if (resolviendo) {
                    resolviendo=false;
                    return;
                }
                if (!contador) {
                    contador();
                }
                usoResolver = false;
                Log.i("valen", "Nodo tocado= " + vNodo.get(j).getText().toString());
                String[] vt = conexiones[j].split(",");
                for (String s : vt) {
                    int adj = Integer.parseInt(s);
                    if (vNodo.get(adj).getText().toString().equals("X")) {
                        swap(j, adj);
                        verificar();
                        break;
                    }
                }
            });
        }
        generar();

        btgenerar=view.findViewById(R.id.btCambiar);
        btgenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!contador) {
                    generar();
                }
            }
        });

        btia=view.findViewById(R.id.btIa);
        btia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolver();
                contador();
            }
        });
        return view;
    }
}