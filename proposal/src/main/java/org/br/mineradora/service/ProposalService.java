package org.br.mineradora.service;

import java.util.*;

import org.br.mineradora.dto.ProposalDTO;
import org.br.mineradora.dto.ProposalDetailsDTO;
import org.br.mineradora.entity.ProposalEntity;
import org.br.mineradora.message.KafkaEvent;
import org.br.mineradora.repository.ProposalRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

@ApplicationScoped
public class ProposalService {

    @Inject
    ProposalRepository proposalRepository;

    @Inject
    KafkaEvent kafkaEvent;

    public ProposalDetailsDTO findFullProposal(long id) {
        var proposalEntity = proposalRepository.findById(id);

        if (proposalEntity == null) {
            throw new WebApplicationException("Não há propostas para consulta!");
        }

        return ProposalDetailsDTO
                .builder()
                .proposalId(proposalEntity.getId())
                .country(proposalEntity.getCountry())
                .customer(proposalEntity.getCustomer())
                .tonnes(proposalEntity.getTonnes())
                .proposalValidityDays(proposalEntity.getProposalValidityDays())
                .build();

    }

    @Transactional
    public void createNewProposal(ProposalDetailsDTO proposalDetailsDTO) {
        var proposal = buildAndSaveNewProposal(proposalDetailsDTO);

        kafkaEvent.sendNewKafkaEvent(proposal);
    }

    private ProposalDTO buildAndSaveNewProposal(ProposalDetailsDTO proposalDetailsDTO) {
        try {
            var proposalEntity = new ProposalEntity();

            proposalEntity.setCreated(new Date());
            proposalEntity.setCountry(proposalDetailsDTO.getCountry());
            proposalEntity.setCustomer(proposalDetailsDTO.getCustomer());
            proposalEntity.setTonnes(proposalDetailsDTO.getTonnes());
            proposalEntity.setPriceTonne(proposalDetailsDTO.getPriceTonne());
            proposalEntity.setProposalValidityDays(proposalDetailsDTO.getProposalValidityDays());

            proposalRepository.persist(proposalEntity);

            return ProposalDTO.builder()
                    .proposalId(proposalRepository.findByCustomer(proposalEntity.getCustomer()).get().getId())
                    .priceTonne(proposalEntity.getPriceTonne())
                    .customer(proposalEntity.getCustomer())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    @Transactional
    public void removeProposal(long id) {
        proposalRepository.deleteById(id);
    }

}
