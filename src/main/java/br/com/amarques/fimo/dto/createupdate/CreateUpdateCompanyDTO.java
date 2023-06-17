package br.com.amarques.fimo.dto.createupdate;

import jakarta.validation.constraints.NotBlank;

public record CreateUpdateCompanyDTO(@NotBlank(message = "Name is mandatory") String name,
                                     @NotBlank(message = "StockSymbol is mandatory") String stockSymbol) {
}
