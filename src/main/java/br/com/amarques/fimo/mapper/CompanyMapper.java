package br.com.amarques.fimo.mapper;

import br.com.amarques.fimo.domain.Company;
import br.com.amarques.fimo.dto.CompanyDTO;
import br.com.amarques.fimo.dto.SimpleEntityDTO;
import br.com.amarques.fimo.dto.createupdate.CreateUpdateCompanyDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyDTO entityToDTO(Company company);

    Company dtoToEntity(CreateUpdateCompanyDTO createUpdateCompanyDTO);

    SimpleEntityDTO toSimpleEntityDTO(Long id);
}
