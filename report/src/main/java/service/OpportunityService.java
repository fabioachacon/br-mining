package service;

import java.util.*;

import entity.*;
import repository.*;
import dto.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class OpportunityService {

    @Inject
    QuotationRepository quotationRepository;

    @Inject
    OpportunityRepository opportunityRepository;

    @Transactional
    public void buildOpportunity(ProposalDTO proposal) {
        var quotationEntities = quotationRepository.findAll().list();

        Collections.reverse(quotationEntities);

        var opportunity = new OpportunityEntity();
        opportunity.setDate(new Date());
        opportunity.setProposalId(proposal.getProposalId());
        opportunity.setCustomer(proposal.getCustomer());
        opportunity.setPriceTonne(proposal.getPriceTonne());
        opportunity.setLastDollarQuotation(quotationEntities.get(0).getCurrencyPrice());

        opportunityRepository.persist(opportunity);

    }

    @Transactional
    public void saveQuotation(QuotationDTO quotation) {
        var quotationEntity = new QuotationEntity();
        quotationEntity.setDate(new Date());
        quotationEntity.setCurrencyPrice(quotation.getCurrencyPrice());

        quotationRepository.persist(quotationEntity);
    }

    public List<OpportunityDTO> generateOpportunityData() {
        var opportunities = new ArrayList<OpportunityDTO>();

        opportunityRepository.findAll().list().forEach(item -> {
            opportunities.add(OpportunityDTO
                    .builder()
                    .proposalId(item.getProposalId())
                    .customer(item.getCustomer())
                    .priceTonne(item.getPriceTonne())
                    .lastDollarQuotation(item.getLastDollarQuotation())
                    .build());
        });

        return opportunities;
    }

    // public ByteArrayInputStream generateCSVOpportunityReport() {
    // var opportunityList = new ArrayList<OpportunityDTO>();

    // opportunityRepository.findAll().list().forEach(item -> {
    // var element = OpportunityDTO
    // .builder()
    // .proposalId(item.getProposalId())
    // .customer(item.getCustomer())
    // .priceTonne(item.getPriceTonne())
    // .lastDollarQuotation(item.getLastDollarQuotation())
    // .build();

    // opportunityList.add(element);

    // });

    // return CSVHelper.oportunitiesCsv(opportunityList);
    // }
}
