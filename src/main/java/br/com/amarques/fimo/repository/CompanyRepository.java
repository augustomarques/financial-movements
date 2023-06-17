package br.com.amarques.fimo.repository;

import br.com.amarques.fimo.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Company findByStockSymbol(String stockSymbol);

    Company findByStockSymbolAndIdNot(String stockSymbol, Long id);
}
