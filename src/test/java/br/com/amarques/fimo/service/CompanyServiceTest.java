package br.com.amarques.fimo.service;

import br.com.amarques.fimo.domain.Company;
import br.com.amarques.fimo.dto.CompanyDTO;
import br.com.amarques.fimo.dto.createupdate.CreateUpdateCompanyDTO;
import br.com.amarques.fimo.exceptions.CompanyAlreadyRegisteredException;
import br.com.amarques.fimo.exceptions.NotFoundException;
import br.com.amarques.fimo.repository.CompanyRepository;
import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.instancio.Select.field;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    @BeforeEach
    public void before() {
        this.companyService = new CompanyService(companyRepository);
    }

    @Test
    @DisplayName("Should return the registered company when searching by Id")
    void shouldFindCompanyById() {
        final Long id = Instancio.create(Long.class);
        final Company company = Instancio.of(Company.class)
                .set(field(Company::getId), id)
                .create();

        when(companyRepository.findById(id)).thenReturn(Optional.of(company));

        final CompanyDTO companyDTO = companyService.getOne(id);

        assertThat(companyDTO).isNotNull();
        assertThat(companyDTO.name()).isEqualTo(company.getName());
        assertThat(companyDTO.stockSymbol()).isEqualTo(company.getStockSymbol());
    }

    @Test
    @DisplayName("Should throw exception when trying to search for an unregistered Id")
    void shouldFailWhenSearchingForUnregisteredId() {
        final Long id = Instancio.create(Long.class);

        when(companyRepository.findById(id)).thenReturn(Optional.empty());

        final Throwable throwable = catchThrowable(() -> companyService.getOne(id));

        assertThat(throwable)
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format(String.format("Record [ID: %d] not found", id)));
    }

    @Test
    @DisplayName("Should create a new company")
    void shouldCreateNewCompany() {
        final CreateUpdateCompanyDTO createUpdateCompanyDTO = Instancio.create(CreateUpdateCompanyDTO.class);
        final Company company = new Company(createUpdateCompanyDTO.name(), createUpdateCompanyDTO.stockSymbol());

        companyService.create(createUpdateCompanyDTO);

        verify(companyRepository).save(company);
    }

    @Test
    @DisplayName("Should throw exception when try create a new company with a stock symbol that is already registered")
    void shouldFailWhenCreatingCompanyWithStockSymbolAlreadyRegistered() {
        final CreateUpdateCompanyDTO companyDTO = Instancio.create(CreateUpdateCompanyDTO.class);
        final Company company = Instancio.of(Company.class)
                .set(field(Company::getStockSymbol), companyDTO.stockSymbol())
                .create();

        when(companyRepository.findByStockSymbol(companyDTO.stockSymbol())).thenReturn(company);

        final Throwable throwable = catchThrowable(() -> companyService.create(companyDTO));

        assertThat(throwable)
                .isInstanceOf(CompanyAlreadyRegisteredException.class)
                .hasMessage(String.format("A company already exists registered with the stock symbol [%s]", companyDTO.stockSymbol()));

        verify(companyRepository, never()).save(any(Company.class));
    }

    @Test
    @DisplayName("Should delete a company")
    void shouldDeleteCompany() {
        final Long id = Instancio.create(Long.class);

        companyService.delete(id);

        verify(companyRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should update a company")
    void shouldUpdateCompany() {
        final Long id = Instancio.create(Long.class);
        final CreateUpdateCompanyDTO companyDTO = Instancio.create(CreateUpdateCompanyDTO.class);
        final Company company = Instancio.of(Company.class)
                .set(field(Company::getId), id)
                .create();

        when(companyRepository.findByStockSymbolAndIdNot(companyDTO.stockSymbol(), id)).thenReturn(null);
        when(companyRepository.findById(id)).thenReturn(Optional.of(company));

        companyService.update(id, companyDTO);

        company.setName(companyDTO.name());
        company.setStockSymbol(companyDTO.stockSymbol());

        verify(companyRepository).save(company);
    }

    @Test
    @DisplayName("Should throw exception when try update a company with a stock symbol that is already registered")
    void shouldFailWhenUpdatingCompanyWithStockSymbolAlreadyRegistered() {
        final Long id = Instancio.create(Long.class);
        final CreateUpdateCompanyDTO companyDTO = Instancio.create(CreateUpdateCompanyDTO.class);
        final Company company = Instancio.of(Company.class)
                .set(field(Company::getStockSymbol), companyDTO.stockSymbol())
                .create();

        when(companyRepository.findByStockSymbolAndIdNot(companyDTO.stockSymbol(), id)).thenReturn(company);

        final Throwable throwable = Assertions.catchThrowable(() -> companyService.update(id, companyDTO));

        assertThat(throwable)
                .isInstanceOf(CompanyAlreadyRegisteredException.class)
                .hasMessage(String.format("A company already exists registered with the stock symbol [%s]", companyDTO.stockSymbol()));

        verify(companyRepository, never()).save(any(Company.class));
    }

    @DisplayName("Should return all registered companies respecting the pagination")
    @ParameterizedTest(name = "Should return registered companies respecting the pagination. Page={0} - size={1}")
    @CsvSource({
            "0, 10",
            "0, 50",
            "1, 10",
            "3, 10"
    })
    void shouldFindAllPaginated(int page, int pageSize) {
        final PageRequest pageRequest = PageRequest.of(page, pageSize);
        final List<Company> registeredCompanies = Instancio.ofList(Company.class).size(pageSize * 5).create();
        final List<Company> companyList = registeredCompanies.subList((page * pageSize), (page * pageSize) + pageSize);
        final Page<Company> pageOfCompanies = new PageImpl<>(companyList, pageRequest, pageSize);

        when(companyRepository.findAll(pageRequest)).thenReturn(pageOfCompanies);

        final Page<CompanyDTO> companies = companyService.getAll(pageRequest);

        assertThat(companies)
                .isNotNull()
                .isNotEmpty()
                .hasSize(pageSize);
    }

    @Test
    @DisplayName("Should return no company when nothing is registered")
    void shouldReturnEmptyWhenNothingIsRegistered() {
        final PageRequest pageRequest = PageRequest.of(0, 10);
        final Page<Company> pageOfCompanies = new PageImpl<>(List.of(), pageRequest, 10);

        when(companyRepository.findAll(pageRequest)).thenReturn(pageOfCompanies);

        final Page<CompanyDTO> companies = companyService.getAll(pageRequest);

        assertThat(companies)
                .isNotNull()
                .isEmpty();
    }
}
