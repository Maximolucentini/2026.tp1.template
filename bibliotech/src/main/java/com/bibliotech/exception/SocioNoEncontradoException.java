package com.bibliotech.exception;

public class SocioNoEncontradoException extends Exception {

    public SocioNoEncontradoException(int dni) {
        super("No se encontro ningun socio con DNI: " + dni);
    }
}