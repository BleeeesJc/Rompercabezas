package com.example.myapplication.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.myapplication.R;
import com.example.myapplication.login.ModeloUser;
import com.example.myapplication.sql.sqlUser;
import java.util.ArrayList;

public class ListaU_Fragment extends Fragment {
    ListView listView;
    private ArrayList<ModeloUser> listaUsuarios;
    private AdapterU adapterU;
    sqlUser sql;
    EditText etUser,etPassword;

    public ListaU_Fragment() {

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_u, container, false);
        listView = view.findViewById(R.id.lvData);
        sql = new sqlUser(getContext());
        listaUsuarios = sql.listarUsuarios();
        adapterU = new AdapterU(getContext(), listaUsuarios);
        listView.setAdapter(adapterU);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ModeloUser usuarioSeleccionado = listaUsuarios.get(position);
                editar(usuarioSeleccionado);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final ModeloUser usuarioSeleccionado = listaUsuarios.get(position);
                boolean eliminado = sql.eliminarUsuario(usuarioSeleccionado.getPkUsuario());
                if (eliminado) {
                    listaUsuarios.remove(position);
                    adapterU.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Usuario eliminado: " + usuarioSeleccionado.getUser(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error al eliminar el usuario", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        return view;
    }
    private void editar(ModeloUser usuarioSeleccionado) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Modificar Usuario");
        View dialogView = getLayoutInflater().inflate(R.layout.editar_user, null);
        builder.setView(dialogView);
        etUser = dialogView.findViewById(R.id.etUser);
        etPassword = dialogView.findViewById(R.id.etPassword);
        etUser.setText(usuarioSeleccionado.getUser());
        etPassword.setText(usuarioSeleccionado.getPassword());
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newUser = etUser.getText().toString();
                String newPassword = etPassword.getText().toString();
                usuarioSeleccionado.setUser(newUser);
                usuarioSeleccionado.setPassword(newPassword);
                try {
                    boolean actualizado = sql.actualizarUsuario(usuarioSeleccionado.getPkUsuario(), newUser, newPassword);
                    if (actualizado) {
                        adapterU.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Usuario actualizado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Error al actualizar el usuario", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error al actualizar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}