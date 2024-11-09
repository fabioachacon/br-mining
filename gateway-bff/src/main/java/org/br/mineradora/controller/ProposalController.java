package org.br.mineradora.controller;

import org.br.mineradora.dto.ProposalDetailsDTO;
import org.br.mineradora.service.ProposalService;

import jakarta.annotation.security.RolesAllowed;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.ServerErrorException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/trade")
public class ProposalController {

    @Inject
    ProposalService proposalService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "user", "manager" })
    public Response getProposalDetailsById(@PathParam("id") long id) {
        try {
            var proposal = proposalService.getProposalDetailsById(id);

            return Response.ok(proposal).build();
        } catch (ServerErrorException exception) {
            return Response.serverError().build();
        }
    }

    @POST
    @RolesAllowed("proposal-customer")
    public Response createNewProposal(ProposalDetailsDTO proposalDetailsDTO) {
        var response = proposalService.createProposal(proposalDetailsDTO);

        var statusCode = response.getStatus();
        if (statusCode > 199 || statusCode < 205) {
            return Response.ok().build();
        } else {
            return Response.status(statusCode).build();
        }
    }

    public Response removeProposal(@PathParam("id") long id) {
        var response = proposalService.removeProposal(id);

        var statusCode = response.getStatus();
        if (statusCode > 199 || statusCode < 205) {
            return Response.ok().build();
        } else {
            return Response.status(statusCode).build();
        }
    }
}
