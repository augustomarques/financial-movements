package br.com.amarques.fimo.resource;

import br.com.amarques.fimo.dto.CompanyDTO;
import br.com.amarques.fimo.dto.SimpleEntityDTO;
import br.com.amarques.fimo.dto.createupdate.CreateUpdateCompanyDTO;
import br.com.amarques.fimo.service.CompanyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CompanyResource.class)
class CompanyResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CompanyService companyService;

    @Test
    @DisplayName("should search for a company by id")
    void shouldFindById() throws Exception {
        final Long id = Instancio.create(Long.class);
        final CompanyDTO companyDTO = new CompanyDTO(id, Instancio.create(String.class), Instancio.create(String.class));

        when(companyService.getOne(id)).thenReturn(companyDTO);

        mockMvc.perform(get("/api/companies/{id}", id)
                        .accept(APPLICATION_JSON).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.name", is(companyDTO.name())))
                .andExpect(jsonPath("$.stock_symbol", is(companyDTO.stockSymbol())));
    }

    @Test
    @DisplayName("should create a new company")
    void shouldCreateNewCompany() throws Exception {
        final CreateUpdateCompanyDTO companyDTO = Instancio.create(CreateUpdateCompanyDTO.class);
        final SimpleEntityDTO simpleEntityDTO = Instancio.create(SimpleEntityDTO.class);

        when(companyService.create(companyDTO)).thenReturn(simpleEntityDTO);

        mockMvc.perform(post("/api/companies")
                        .content(objectMapper.writeValueAsString(companyDTO))
                        .accept(APPLICATION_JSON).contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(simpleEntityDTO.id().intValue())));
    }

    @Test
    @DisplayName("should update a company")
    void shouldUpdateCompany() throws Exception {
        final CreateUpdateCompanyDTO companyDTO = Instancio.create(CreateUpdateCompanyDTO.class);
        final Long id = Instancio.create(Long.class);

        mockMvc.perform(put("/api/companies/{id}", id)
                        .content(objectMapper.writeValueAsString(companyDTO))
                        .accept(APPLICATION_JSON).contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(companyService).update(id, companyDTO);
    }

    @Test
    @DisplayName("should delete a company")
    void shouldDeleteCompany() throws Exception {
        final Long id = Instancio.create(Long.class);

        mockMvc.perform(delete("/api/companies/{id}", id))
                .andExpect(status().isOk());

        verify(companyService).delete(id);
    }

    @Test
    @DisplayName("Should return all registered companies respecting the pagination")
    void shouldFindAllPaginated() throws Exception {
        final int page = 0;
        final int size = 2;
        final PageRequest pageRequest = PageRequest.of(page, size);
        final List<CompanyDTO> registeredCompanies = Instancio.ofList(CompanyDTO.class).size(size * 2).create();
        final Page<CompanyDTO> pageOfCompanies = new PageImpl<>(registeredCompanies, pageRequest, registeredCompanies.size());

        when(companyService.getAll(pageRequest)).thenReturn(pageOfCompanies);

        mockMvc.perform(get(URI.create("/api/companies"))
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.content.[0].id", is(registeredCompanies.get(0).id().intValue())))
                .andExpect(jsonPath("$.content.[0].name", is(registeredCompanies.get(0).name())))
                .andExpect(jsonPath("$.content.[0].stock_symbol", is(registeredCompanies.get(0).stockSymbol())))
                .andExpect(jsonPath("$.content.[1].id", is(registeredCompanies.get(1).id().intValue())))
                .andExpect(jsonPath("$.content.[2].name", is(registeredCompanies.get(2).name())))
                .andExpect(jsonPath("$.content.[3].stock_symbol", is(registeredCompanies.get(3).stockSymbol())))
                .andExpect(jsonPath("$.last", is(false)))
                .andExpect(jsonPath("$.total_pages", is(2)))
                .andExpect(jsonPath("$.total_elements", is(4)))
                .andExpect(jsonPath("$.first", is(true)))
                .andExpect(jsonPath("$.size", is(2)))
                .andExpect(jsonPath("$.number", is(0)));
    }
}
