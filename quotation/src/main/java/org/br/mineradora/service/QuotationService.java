package org.br.mineradora.service;

import java.math.*;
import java.util.*;

import org.br.mineradora.client.CurrencyPriceClient;
import org.br.mineradora.dto.CurrencyPriceDTO;
import org.br.mineradora.dto.QuotationDTO;
import org.br.mineradora.entity.QuotationEntity;
import org.br.mineradora.message.KafkaEvents;
import org.br.mineradora.repository.QuotationRepository;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class QuotationService {

    @Inject
    @RestClient
    CurrencyPriceClient currencyPriceClient;

    @Inject
    QuotationRepository quotationRepository;

    @Inject
    KafkaEvents kafkaEvents;

    public void getCurrencyPrice() {
        var currencyPrice = currencyPriceClient.getPriceByPair("USD-BRL");
        var priceHasUpdated = updateCurrentInfoPrice(currencyPrice);
        if (priceHasUpdated) {
            var quotationDTO = QuotationDTO
                    .builder()
                    .date(new Date())
                    .currecyPrice(getCurrentBidAsBigDecimal(currencyPrice))
                    .build();

            kafkaEvents.sendNewKafkaEvent(quotationDTO);
        }

    }

    private boolean updateCurrentInfoPrice(CurrencyPriceDTO priceInfo) {
        boolean priceUpdated = false;

        var quotationList = quotationRepository.findAll().list();
        if (quotationList.isEmpty()) {
            saveQuotation(priceInfo);
            priceUpdated = true;
        } else {
            var currentPrice = getCurrentBidAsBigDecimal(priceInfo);
            var lastDollarPrice = quotationList.get(quotationList.size() - 1);
            if (currentPrice.floatValue() > lastDollarPrice.getCurrencyPrice().floatValue()) {
                saveQuotation(priceInfo);
                priceUpdated = true;
            }
        }

        return priceUpdated;

    }

    private void saveQuotation(CurrencyPriceDTO currencyPriceDTO) {
        var quotation = new QuotationEntity();
        quotation.setDate(new Date());
        quotation.setCurrencyPrice(getCurrentBidAsBigDecimal(currencyPriceDTO));
        quotation.setPctChange(currencyPriceDTO.getUSDBRL().getPctChange());
        quotation.setPair("USD-BRL");

        quotationRepository.persist(quotation);

    }

    private BigDecimal getCurrentBidAsBigDecimal(CurrencyPriceDTO currencyPrice) {
        return BigDecimal.valueOf(Long.parseLong(currencyPrice.getUSDBRL().getBid()));
    }

}
