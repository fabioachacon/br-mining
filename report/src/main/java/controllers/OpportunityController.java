package controllers;

import java.util.*;

import org.eclipse.microprofile.jwt.JsonWebToken;

import dto.OpportunityDTO;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import service.OpportunityService;

@Authenticated
@Path("/api/opportunity")
public class OpportunityController {

    @Inject
    JsonWebToken token;

    @Inject
    OpportunityService opportunityService;

    @GET
    @Path("/report")
    @RolesAllowed({ "user", "manager" })
    public List<OpportunityDTO> generateReport() {
        return opportunityService.generateOpportunityData();
    }
}

// @GET
// @Path("/report")
// @Produces(MediaType.APPLICATION_OCTET_STREAM)
// public Response generateReport() {
// try {
// var opportunityReportAsCsv =
// opportunityService.generateCSVOpportunityReport();

// return Response.ok(opportunityReportAsCsv,
// MediaType.APPLICATION_OCTET_STREAM)
// .header("content-type", "attachmnet; filename " + new Date() +
// "--oportunidade-venda.csv").build();
// } catch (Exception e) {
// return Response.serverError().build();
// }
// }
