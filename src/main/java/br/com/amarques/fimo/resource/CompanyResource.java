package br.com.amarques.fimo.resource;

import br.com.amarques.fimo.dto.CompanyDTO;
import br.com.amarques.fimo.dto.SimpleEntityDTO;
import br.com.amarques.fimo.dto.createupdate.CreateUpdateCompanyDTO;
import br.com.amarques.fimo.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Tag(name = "Company")
@RestController
@RequestMapping("/api/companies")
public class CompanyResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyResource.class);

    private final CompanyService companyService;

    public CompanyResource(final CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    @Operation(summary = "Register a new Company")
    public ResponseEntity<SimpleEntityDTO> create(@Valid @RequestBody final CreateUpdateCompanyDTO companyDTO,
                                                  UriComponentsBuilder uriBuilder) {
        LOGGER.info("REST request to create a new Company {}", companyDTO);

        final var simpleEntityDTO = companyService.create(companyDTO);
        final var uri = uriBuilder.path("/api/companies/{id}").buildAndExpand(simpleEntityDTO).toUri();

        return ResponseEntity.created(uri).body(simpleEntityDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Search a Company by ID")
    public ResponseEntity<CompanyDTO> get(@PathVariable final Long id) {
        LOGGER.info("REST request to get a Company [id: {}]", id);

        final var recipeDTO = companyService.getOne(id);

        return ResponseEntity.ok(recipeDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Change a registered Company")
    public ResponseEntity<Void> update(@PathVariable final Long id,
                                       @Valid @RequestBody final CreateUpdateCompanyDTO companyDTO) {
        LOGGER.info("REST request to update a Company [id: {}] [dto: {}]", id, companyDTO);

        companyService.update(id, companyDTO);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Search all Companies")
    public ResponseEntity<List<CompanyDTO>> gelAll(@RequestParam(value = "page", defaultValue = "0", required = false) final int page,
                                                   @RequestParam(value = "size", defaultValue = "10", required = false) final int size) {
        LOGGER.info("REST request to gel all Companies [Page: {}  - Size: {}]", page, size);

        final var companies = companyService.getAll(PageRequest.of(page, size));

        return ResponseEntity.ok().body(companies);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove a Company")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        LOGGER.info("REST request to delete a Company [id: {}]", id);

        companyService.delete(id);

        return ResponseEntity.ok().build();
    }
}
