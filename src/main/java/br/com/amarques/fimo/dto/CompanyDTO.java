package br.com.amarques.fimo.dto;

import br.com.amarques.fimo.domain.Company;

public record CompanyDTO(Long id, String name, String stockSymbol) {
    public CompanyDTO(final Company company) {
        this(company.getId(), company.getName(), company.getStockSymbol());
    }
}
