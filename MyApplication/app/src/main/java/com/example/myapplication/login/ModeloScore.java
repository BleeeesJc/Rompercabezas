package com.example.myapplication.login;

public class ModeloScore {
    private int pkPuntuaje;
    private int fkUsuario;
    private String dificultad;
    private int score;
    private String tipo;
    private String fecha;

    public ModeloScore(int pkPuntuaje, int fkUsuario, String dificultad, int score, String tipo, String fecha) {
        this.pkPuntuaje = pkPuntuaje;
        this.fkUsuario = fkUsuario;
        this.dificultad = dificultad;
        this.score = score;
        this.tipo=tipo;
        this.fecha = fecha;
    }

    public int getpkPuntuaje() {
        return pkPuntuaje;
    }

    public void setpkPuntuaje(int pkPuntuaje) {
        this.pkPuntuaje = pkPuntuaje;
    }

    public int getFkUsuario() {
        return fkUsuario;
    }

    public void setFkUsuario(int fkUsuario) {
        this.fkUsuario = fkUsuario;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}