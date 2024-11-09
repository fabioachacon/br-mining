package org.br.mineradora.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Data
@AllArgsConstructor
public class ProposalDTO {

    private Long proposalId;

    private String customer;

    private BigDecimal priceTonne;
}
