package com.example.myapplication.logica;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.login.ModeloScore;
import com.example.myapplication.login.Sesion;
import com.example.myapplication.sql.sqlScore;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;


public class Tres_Fragment extends Fragment {

    TextView btmezclar,btresolver;
    ArrayList<ImageView> vNodo = new ArrayList<>();
    ArrayList<ImageView> vSolucion = new ArrayList<>();
    Bitmap[] piezas = new Bitmap[9];
    List<Integer> estadoActual = new ArrayList<>();
    private int emptyPieceIndex = -1;
    boolean contador= false;
    EditText ettiempo;
    int segundos = 0;
    Handler handler = new Handler();
    boolean resolviendo = false;
    boolean usoResolver = false;

    private void dividirImagen(Bitmap imagen) {
        imagen = Bitmap.createScaledBitmap(imagen, 300, 300, true);
        int size = imagen.getWidth();
        int pieceSize = size / 3;
        estadoActual.clear();
        for (int i = 0; i < 9; i++) {
            int row = i / 3;
            int col = i % 3;
            int x = col * pieceSize;
            int y = row * pieceSize;

            piezas[i] = Bitmap.createBitmap(imagen, x, y, pieceSize, pieceSize);
            estadoActual.add(i);
        }
        for (int i = 0; i < 9; i++) {
            if (i < 8) {
                vSolucion.get(i).setImageBitmap(piezas[i]);
            } else {
                vSolucion.get(i).setImageBitmap(null);
            }
        }
        emptyPieceIndex = 8;
        String dificultad = getArguments() != null ? getArguments().getString("dificultad", "Fácil") : "Fácil";
        int movimientos = dificultad.equals("Fácil") ? 20 : (dificultad.equals("Medio") ? 80 : 300);
        mezclar(movimientos);
        actualizar();
    }

    private void swap(int from, int to) {
        Collections.swap(estadoActual, from, to);
        actualizar();
        emptyPieceIndex = from;
    }

    private void mezclar(int movimientos) {
        Random random = new Random();
        int[] desplazamientos = {-3, 3, -1, 1};
        List<Integer> secuencia = new ArrayList<>();
        for (int i = 0; i < movimientos; i++) {
            List<Integer> vecinos = new ArrayList<>();
            for (int d : desplazamientos) {
                int nuevaPos = emptyPieceIndex + d;
                if (nuevaPos >= 0 && nuevaPos < 9 && esMovimientoValido(emptyPieceIndex, nuevaPos)) {
                    vecinos.add(nuevaPos);
                }
            }
            if (!vecinos.isEmpty()) {
                int seleccion = vecinos.get(random.nextInt(vecinos.size()));
                secuencia.add(seleccion);
                swap(seleccion, emptyPieceIndex);
            }
        }
        actualizar();
    }

    private List<Integer> resolver(List<Integer> estadoInicial) {
        PriorityQueue<Nodo> frontera = new PriorityQueue<>(Comparator.comparingInt(n -> n.costoTotal));
        Set<List<Integer>> visitados = new HashSet<>();
        Nodo inicio = new Nodo(estadoInicial, null, 0, heuristica(estadoInicial));
        frontera.add(inicio);

        while (!frontera.isEmpty()) {
            Nodo actual = frontera.poll();

            assert actual != null;
            if (esEstadoObjetivo(actual.estado)) {
                return reconstruirCamino(actual);
            }
            visitados.add(actual.estado);
            for (Nodo vecino : obtenerVecinos(actual)) {
                if (!visitados.contains(vecino.estado)) {
                    frontera.add(vecino);
                }
            }
        }
        return null;
    }

    private int heuristica(List<Integer> estado) {
        int h = 0;
        for (int i = 0; i < estado.size(); i++) {
            int valor = estado.get(i);
            if (valor != 8) {
                h += Math.abs(i / 3 - valor / 3) + Math.abs(i % 3 - valor % 3);
            }
        }
        return h;
    }
    public boolean esEstadoObjetivo(List<Integer> estado) {
        List<Integer> estadoObjetivo = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8);
        return estado.equals(estadoObjetivo);
    }
    private List<Nodo> obtenerVecinos(Nodo nodo) {
        List<Nodo> vecinos = new ArrayList<>();
        int posVacia = nodo.estado.indexOf(8);

        int[] movimientos = {-3, 3, -1, 1};
        for (int mov : movimientos) {
            int nuevaPos = posVacia + mov;
            if (nuevaPos >= 0 && nuevaPos < 9 && esMovimientoValido(posVacia, nuevaPos)) {
                List<Integer> nuevoEstado = new ArrayList<>(nodo.estado);
                Collections.swap(nuevoEstado, posVacia, nuevaPos);
                vecinos.add(new Nodo(nuevoEstado, nodo, nodo.costo + 1, heuristica(nuevoEstado)));
            }
        }
        return vecinos;
    }

    private boolean esMovimientoValido(int pos, int nuevaPos) {
        return (pos % 3 != 2 || nuevaPos % 3 != 0) && (pos % 3 != 0 || nuevaPos % 3 != 2);
    }

    private List<Integer> reconstruirCamino(Nodo nodo) {
        List<Integer> camino = new ArrayList<>();
        while (nodo.padre != null) {
            camino.add(0, nodo.estado.indexOf(8));
            nodo = nodo.padre;
        }
        return camino;
    }

    private void Solucion(List<Integer> solucion) {
        resolviendo = true;
        usoResolver=true;
        int delay = 500;
        for (int i = 0; i < solucion.size(); i++) {
            final int movimiento = solucion.get(i);
            handler.postDelayed(() -> {
                if (resolviendo) {
                    swap(movimiento, emptyPieceIndex);
                    actualizar();
                    verificar();
                }
            }, i * delay);
        }
    }

    private static class Nodo {
        List<Integer> estado;
        Nodo padre;
        int costo;
        int heuristica;
        int costoTotal;

        Nodo(List<Integer> estado, Nodo padre, int costo, int heuristica) {
            this.estado = estado;
            this.padre = padre;
            this.costo = costo;
            this.heuristica = heuristica;
            this.costoTotal = costo + heuristica;
        }
    }

    public void contador() {
        if (!contador) {
            contador = true;
            btmezclar.setEnabled(false);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (contador) {
                        int minutos = segundos / 60;
                        int seg = segundos % 60;
                        ettiempo.setText(String.format("%02d:%02d", minutos, seg));
                        segundos++;
                        handler.postDelayed(this, 1000);
                    }
                }
            });
        }
    }

    public void verificar() {
        List<Integer> estadoObjetivo = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8);
        if (estadoActual.equals(estadoObjetivo)) {
            terminar();
        }
    }

    private void terminar() {
        contador = false;
        handler.removeCallbacksAndMessages(null);
        if(usoResolver){
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("¡Inténtalo de nuevo!");
            builder.setMessage("Debes completar el rompecabezas sin usar la solución automática.");
            builder.setPositiveButton("Aceptar", (dialog, which) -> {
                dialog.dismiss();
                reiniciarJuego();
            });
            builder.show();
        }else{
            String tiempoFinal = ettiempo.getText().toString();
            int tiempoSegundos = segundos;
            int usuarioId = Sesion.getUsuarioId(requireContext());
            String dificultad = getArguments() != null ? getArguments().getString("dificultad", "Fácil") : "Fácil";
            String tipo = getArguments() !=null ? getArguments().getString("tamaño","3x3"):"3x3";
            String fecha = obtenerFechaActual();
            ModeloScore puntaje = new ModeloScore(0, usuarioId, dificultad, tiempoSegundos, tipo,fecha);
            sqlScore sql = new sqlScore(getContext());
            long resultado = sql.insertarPuntaje(puntaje);
            if (resultado != -1) {
                Log.i("valen", "Puntaje guardado con éxito.");
            } else {
                Log.e("valen", "Error al guardar el puntaje.");
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("¡Felicidades!")
                    .setMessage("Has completado el rompecabezas en " + tiempoFinal)
                    .setPositiveButton("Aceptar", (dialog, which) -> reiniciarJuego())
                    .show();
            btmezclar.setEnabled(true);
        }
    }

    private String obtenerFechaActual() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    public void reiniciarJuego() {
        segundos = 0;
        ettiempo.setText("00:00");
        String dificultad = getArguments() != null ? getArguments().getString("dificultad", "Fácil") : "Fácil";
        int movimientos = dificultad.equals("Fácil") ? 20 : (dificultad.equals("Medio") ? 80 : 300);
        mezclar(movimientos);
        actualizar();
        contador = false;
    }

    public void actualizar() {
        for (int i = 0; i < 9; i++) {
            int piezaIndex = estadoActual.get(i);
            vNodo.get(i).setImageBitmap(piezaIndex == 8 ? null : piezas[piezaIndex]);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_tres, container, false);
        btresolver=view.findViewById(R.id.btIa);
        btresolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> solucion = resolver(estadoActual);
                if (solucion != null) {
                    Solucion(solucion);
                } else {
                    Log.e("valen", "No se encontró solución.");
                }
                contador();
            }
        });
        btmezclar=view.findViewById(R.id.btMezclar);
        btmezclar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dificultad = getArguments() != null ? getArguments().getString("dificultad", "Fácil") : "Fácil";
                int movimientos = dificultad.equals("Fácil") ? 20 : (dificultad.equals("Medio") ? 80 : 300);
                mezclar(movimientos);
                actualizar();
            }
        });

        ettiempo = view.findViewById(R.id.etTiempo);
        vNodo.add(view.findViewById(R.id.tvS0));
        vNodo.add(view.findViewById(R.id.tvS1));
        vNodo.add(view.findViewById(R.id.tvS2));
        vNodo.add(view.findViewById(R.id.tvS3));
        vNodo.add(view.findViewById(R.id.tvS4));
        vNodo.add(view.findViewById(R.id.tvS5));
        vNodo.add(view.findViewById(R.id.tvS6));
        vNodo.add(view.findViewById(R.id.tvS7));
        vNodo.add(view.findViewById(R.id.tvS8));

        vSolucion.add(view.findViewById(R.id.tvSA));
        vSolucion.add(view.findViewById(R.id.tvSB));
        vSolucion.add(view.findViewById(R.id.tvSC));
        vSolucion.add(view.findViewById(R.id.tvSD));
        vSolucion.add(view.findViewById(R.id.tvSE));
        vSolucion.add(view.findViewById(R.id.tvSF));
        vSolucion.add(view.findViewById(R.id.tvSG));
        vSolucion.add(view.findViewById(R.id.tvSH));
        vSolucion.add(view.findViewById(R.id.tvSX));

        if (getArguments() != null) {
            String imageUriString = getArguments().getString("imageUri");
            Uri imageUri = Uri.parse(imageUriString);
            try {
                InputStream imageStream = requireContext().getContentResolver().openInputStream(imageUri);
                Bitmap imagen = BitmapFactory.decodeStream(imageStream);
                dividirImagen(imagen);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                resolviendo=false;
                if (!contador) {
                    contador();
                }
                usoResolver = false;
                Log.i("valen", "Nodo tocado= " + j);
                String[] vt = conexiones[j].split(",");
                for (String s : vt) {
                    int adj = Integer.parseInt(s);
                    if (emptyPieceIndex == adj) {
                        swap(j, adj);
                        verificar();
                        break;
                    }
                }
            });
        }
        return view;
    }
}