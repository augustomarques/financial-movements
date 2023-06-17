package br.com.amarques.fimo.service;

import br.com.amarques.fimo.domain.Company;
import br.com.amarques.fimo.dto.CompanyDTO;
import br.com.amarques.fimo.dto.SimpleEntityDTO;
import br.com.amarques.fimo.dto.createupdate.CreateUpdateCompanyDTO;
import br.com.amarques.fimo.exceptions.CompanyAlreadyRegisteredException;
import br.com.amarques.fimo.exceptions.NotFoundException;
import br.com.amarques.fimo.mapper.CompanyMapper;
import br.com.amarques.fimo.repository.CompanyRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public CompanyService(final CompanyRepository companyRepository, final CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    @Transactional
    public SimpleEntityDTO create(final CreateUpdateCompanyDTO createUpdateCompanyDTO) {
        final var registeredCompany = companyRepository.findByStockSymbol(createUpdateCompanyDTO.stockSymbol());
        if(Objects.nonNull(registeredCompany)) {
            throw new CompanyAlreadyRegisteredException(createUpdateCompanyDTO.stockSymbol());
        }

        final var company = companyMapper.dtoToEntity(createUpdateCompanyDTO);

        companyRepository.save(company);

        return companyMapper.toSimpleEntityDTO(company.getId());
    }

    public CompanyDTO getOne(final Long id) {
        return companyMapper.entityToDTO(findCompanyById(id));
    }

    public List<CompanyDTO> getAll(final Pageable pageable) {
        final var companies = companyRepository.findAll(pageable);

        if(!companies.hasContent()) {
            return List.of();
        }

        return companies.stream().map(companyMapper::entityToDTO).toList();
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
