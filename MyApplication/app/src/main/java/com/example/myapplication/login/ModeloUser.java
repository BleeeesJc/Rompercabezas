package com.example.myapplication.login;

import androidx.annotation.NonNull;

public class ModeloUser {
    private int pkUsuario;
    private String user;
    private String password;

    public ModeloUser(int pkUsuario, String user,String password) {
        this.pkUsuario = pkUsuario;
        this.user = user;
        this.password = password;
    }

    public ModeloUser() {}

    public int getPkUsuario() { return pkUsuario; }
    public String getUser() { return user; }
    public String getPassword() { return password; }

    public void setPkUsuario(int pkUsuario) { this.pkUsuario = pkUsuario; }
    public void setUser(String user) { this.user = user; }
    public void setPassword(String password) { this.password = password; }

    @NonNull
    @Override
    public String toString() {
        return "Usuario ID: " + pkUsuario +
                ", User: " + user ;
    }
}
