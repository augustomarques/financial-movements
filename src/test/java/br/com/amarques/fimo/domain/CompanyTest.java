package br.com.amarques.fimo.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Instancio.create;

class CompanyTest {

    @Test
    @DisplayName("Should create a new company")
    void createCompany() {
        final String name = create(String.class);
        final String stockSymbol = create(String.class);

        final var company = new Company(name, stockSymbol);

        assertThat(company).isNotNull();
        assertThat(company.getId()).isNull();
        assertThat(company.getName()).isEqualTo(name);
        assertThat(company.getStockSymbol()).isEqualTo(stockSymbol);
    }

    @Test
    @DisplayName("Should update the company fields")
    void updateCompany() {
        final var company = new Company(create(String.class), create(String.class));

        final String name = create(String.class);
        final String stockSymbol = create(String.class);

        company.setName(name);
        company.setStockSymbol(stockSymbol);

        assertThat(company).isNotNull();
        assertThat(company.getId()).isNull();
        assertThat(company.getName()).isEqualTo(name);
        assertThat(company.getStockSymbol()).isEqualTo(stockSymbol);
    }
}
