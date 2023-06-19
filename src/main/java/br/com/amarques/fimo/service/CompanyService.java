package br.com.amarques.fimo.service;

import br.com.amarques.fimo.domain.Company;
import br.com.amarques.fimo.dto.CompanyDTO;
import br.com.amarques.fimo.dto.SimpleEntityDTO;
import br.com.amarques.fimo.dto.createupdate.CreateUpdateCompanyDTO;
import br.com.amarques.fimo.exceptions.CompanyAlreadyRegisteredException;
import br.com.amarques.fimo.exceptions.NotFoundException;
import br.com.amarques.fimo.repository.CompanyRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(final CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional
    public SimpleEntityDTO create(final CreateUpdateCompanyDTO companyDTO) {
        final var registeredCompany = companyRepository.findByStockSymbol(companyDTO.stockSymbol());
        if(Objects.nonNull(registeredCompany)) {
            throw new CompanyAlreadyRegisteredException(companyDTO.stockSymbol());
        }

        final var company = new Company(companyDTO.name(), companyDTO.stockSymbol());

        companyRepository.save(company);

        return new SimpleEntityDTO(company.getId());
    }

    public CompanyDTO getOne(final Long id) {
        return new CompanyDTO(findCompanyById(id));
    }

    public List<CompanyDTO> getAll(final Pageable pageable) {
        final var companies = companyRepository.findAll(pageable);

        if(!companies.hasContent()) {
            return List.of();
        }

        return companies.stream().map(CompanyDTO::new).toList();
    }

    @Transactional
    public void update(final Long id, final CreateUpdateCompanyDTO createUpdateCompanyDTO) {
        final var companyAlreadyRegisteredWithStockSymbol = companyRepository.findByStockSymbolAndIdNot(createUpdateCompanyDTO.stockSymbol(), id);
        if(Objects.nonNull(companyAlreadyRegisteredWithStockSymbol)) {
            throw new CompanyAlreadyRegisteredException(createUpdateCompanyDTO.stockSymbol());
        }

        final var company = findCompanyById(id);
        company.setName(createUpdateCompanyDTO.name());
        company.setStockSymbol(createUpdateCompanyDTO.stockSymbol());

        companyRepository.save(company);
    }

    private Company findCompanyById(final Long id) {
        return companyRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    @Transactional
    public void delete(final Long id) {
        companyRepository.deleteById(id);
    }
}
