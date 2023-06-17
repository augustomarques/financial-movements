package br.com.amarques.fimo.domain;

import br.com.amarques.fimo.enums.TradingType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "trading")
public class Trading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TradingType type;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_value")
    private BigDecimal unitValue;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", updatable = false)
    private Company company;

    protected Trading() {
    }

    public Trading(LocalDate date, TradingType type, Integer quantity, BigDecimal unitValue, Company company) {
        this.date = date;
        this.type = type;
        this.quantity = quantity;
        this.unitValue = unitValue;
        this.company = company;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TradingType getType() {
        return type;
    }

    public void setType(TradingType type) {
        this.type = type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(BigDecimal unitValue) {
        this.unitValue = unitValue;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", date=" + date +
                ", type=" + type +
                ", quantity=" + quantity +
                ", unitValue=" + unitValue +
                ", company=" + company +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trading that = (Trading) o;
        return Objects.equals(id, that.id) && Objects.equals(date, that.date)
                && type == that.type && Objects.equals(quantity, that.quantity) && Objects.equals(unitValue, that.unitValue)
                && Objects.equals(company, that.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, type, quantity, unitValue, company);
    }
}
