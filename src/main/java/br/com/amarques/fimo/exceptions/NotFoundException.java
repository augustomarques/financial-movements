package br.com.amarques.fimo.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException(Long id) {
        super(String.format("Record [ID: %d] not found", id));
    }
}
