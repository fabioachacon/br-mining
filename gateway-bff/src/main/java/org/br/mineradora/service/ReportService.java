package org.br.mineradora.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.br.mineradora.client.ReportRestClient;
import org.br.mineradora.dto.OpportunityDTO;
import org.br.mineradora.utils.CSVHelper;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ReportService {

    @Inject
    @RestClient
    ReportRestClient reportRestClient;

    public ByteArrayInputStream generateCSVOpportunityReport() {
        var opportunityDTO = getOpportunitiesData();

        return CSVHelper.oportunitiesCsv(opportunityDTO);

    }

    public List<OpportunityDTO> getOpportunitiesData() {
        return reportRestClient.requestOpportunitiesData();
    }
}
