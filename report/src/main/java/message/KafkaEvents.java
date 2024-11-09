package message;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import dto.ProposalDTO;
import dto.QuotationDTO;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import service.OpportunityService;

@Slf4j
@ApplicationScoped
public class KafkaEvents {

    @Inject
    OpportunityService opportunityService;

    @Incoming("proposal")
    @Transactional
    public void receiveProposal(ProposalDTO proposal) {
        log.info("-- Receiving a new proposal from Kafka Topic");

        opportunityService.buildOpportunity(proposal);
    }

    @Incoming("quotation")
    @Blocking
    public void receiveQuotation(QuotationDTO quotation) {
        log.info("-- Receiving currency quotation from Kafka Topic");

        opportunityService.saveQuotation(quotation);
    }
}