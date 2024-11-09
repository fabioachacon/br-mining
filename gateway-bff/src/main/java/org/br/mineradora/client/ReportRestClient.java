package org.br.mineradora.client;

import java.util.List;

import org.br.mineradora.dto.OpportunityDTO;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import io.quarkus.oidc.client.filter.OidcClientFilter;
import jakarta.ws.rs.Path;

@Path("/api/opportunity")
@OidcClientFilter
@RegisterRestClient
public interface ReportRestClient {

    @GET
    List<OpportunityDTO> requestOpportunitiesData();
}
