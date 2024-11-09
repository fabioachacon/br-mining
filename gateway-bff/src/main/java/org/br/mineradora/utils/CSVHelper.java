package org.br.mineradora.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.br.mineradora.dto.OpportunityDTO;


public class CSVHelper {

    public static ByteArrayInputStream oportunitiesCsv(List<OpportunityDTO> opportunities) {

        try {
            final var format = CSVFormat.DEFAULT.withHeader("ID Proposta", " Cliente", "Preço por Tonelada",
                    "Melhor cotação de Moeda");

            final var outputStream = new ByteArrayOutputStream();

            final var csvPrinter = new CSVPrinter(new PrintWriter(outputStream), format);

            for (var item : opportunities) {
                var data = Arrays.asList(
                        item.getProposalId().toString(),
                        item.getCustomer(),
                        item.getPriceTonne().toString(),
                        item.getLastDollarQuotation());

                csvPrinter.printRecord(data);
            }

            csvPrinter.close();

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to import data to CSV format");
        }
    }
}
