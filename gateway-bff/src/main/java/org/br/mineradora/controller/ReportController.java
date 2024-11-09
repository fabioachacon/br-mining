package org.br.mineradora.controller;

import java.util.*;

import org.br.mineradora.service.ReportService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.ServerErrorException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/opportunity")
public class ReportController {

    @Inject
    ReportService reportService;

    @GET
    @Path("/report")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @RolesAllowed({ "user", "manager" })
    public Response generateReport() {
        try {
            var reportAsCSV = reportService.generateCSVOpportunityReport();

            return Response.ok(reportAsCSV)
                    .header("content-type", "attachment; filename" + new Date() + "--opportunities.css").build();
        } catch (ServerErrorException exception) {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/data")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "user", "manager" })
    public Response generateOpportunitiesData() {
        try {
            var data = reportService.getOpportunitiesData();

            return Response.ok(data).build();
        } catch (ServerErrorException exception) {
            return Response.serverError().build();
        }
    }

}
