package com.example.arichbrof;

public class Imagenes {

    String Code64, Titulo;


    //CONSTRUCTOR


    public Imagenes() {
    }

    public Imagenes(String code64, String titulo) {
        Code64 = code64;
        Titulo = titulo;
    }


    //GETTERS Y SETTERS


    public String getCode64() {
        return Code64;
    }

    public void setCode64(String code64) {
        Code64 = code64;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }
}
