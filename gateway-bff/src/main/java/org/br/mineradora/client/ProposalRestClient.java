package org.br.mineradora.client;

import org.br.mineradora.dto.ProposalDetailsDTO;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.quarkus.oidc.client.filter.OidcClientFilter;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/api/proposal")
@OidcClientFilter
@RegisterClientHeaders
@RegisterRestClient
public interface ProposalRestClient {

    @GET
    @Path("/{id}")
    ProposalDetailsDTO getProposalDetailsById(@PathParam("id") long id);

    @POST
    Response createProposal(ProposalDetailsDTO proposalDetailsDTO);

    @Path("/{id}")
    Response removeProposal(@PathParam("id") long id);
}
