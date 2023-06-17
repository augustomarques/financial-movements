package br.com.amarques.fimo.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException(Long id) {
        super("Record [ID: "+id+"] not found");
    }
}
