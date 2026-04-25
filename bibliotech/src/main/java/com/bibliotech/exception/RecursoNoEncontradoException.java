package com.bibliotech.exception;

public class RecursoNoEncontradoException extends Exception {
 public RecursoNoEncontradoException(String isbn){
     super("No se encontro ningun recurso con ISBN:" + isbn);
 }
}
