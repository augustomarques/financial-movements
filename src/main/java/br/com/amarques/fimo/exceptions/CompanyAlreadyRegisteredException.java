package br.com.amarques.fimo.exceptions;

public class CompanyAlreadyRegisteredException extends RuntimeException {
    public CompanyAlreadyRegisteredException(final String stockSymbol) {
        super(String.format("A company already exists registered with the stock symbol [%s]", stockSymbol));
    }
}
