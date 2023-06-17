package br.com.amarques.fimo.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "company",
        indexes = { @Index(name = "index_stock_symbol", columnList = "stock_symbol", unique = true ) })
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "stock_symbol", nullable = false)
    private String stockSymbol;

    protected Company() {
    }

    public Company(final String name, final String stockSymbol) {
        this.name = name;
        this.stockSymbol = stockSymbol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stockSymbol='" + stockSymbol + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(id, company.id) && Objects.equals(name, company.name) && Objects.equals(stockSymbol, company.stockSymbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, stockSymbol);
    }
}
