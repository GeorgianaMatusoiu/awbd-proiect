package com.awbd.demo.service;

import com.awbd.demo.dto.CardFidelitateRequest;
import com.awbd.demo.entity.CardFidelitate;
import com.awbd.demo.entity.Client;
import com.awbd.demo.exception.BadRequestException;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.CardFidelitateRepository;
import com.awbd.demo.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardFidelitateService {

    private final CardFidelitateRepository cardRepo;
    private final ClientRepository clientRepo;

    public CardFidelitateService(CardFidelitateRepository cardRepo, ClientRepository clientRepo) {
        this.cardRepo = cardRepo;
        this.clientRepo = clientRepo;
    }

    public CardFidelitate create(CardFidelitateRequest req) {
        Client client = clientRepo.findById(req.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Clientul cu id " + req.getClientId() + " nu exista."));

        if (cardRepo.existsByClientId(req.getClientId())) {
            throw new BadRequestException("Clientul are deja un card de fidelitate.");
        }

        CardFidelitate card = new CardFidelitate();
        card.setNivel(req.getNivel());
        card.setPuncte(req.getPuncte());
        card.setClient(client);

        return cardRepo.save(card);
    }

    public List<CardFidelitate> getAll() {
        return cardRepo.findAll();
    }

    public CardFidelitate getById(Long id) {
        return cardRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cardul cu id " + id + " nu exista."));
    }

    public CardFidelitate update(Long id, CardFidelitateRequest req) {
        CardFidelitate existing = getById(id);

        if (req.getNivel() != null) {
            existing.setNivel(req.getNivel());
        }

        if (req.getPuncte() != null) {
            existing.setPuncte(req.getPuncte());
        }

        if (req.getClientId() != null) {
            Client client = clientRepo.findById(req.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Clientul cu id " + req.getClientId() + " nu exista."));

            if (!existing.getClient().getId().equals(req.getClientId()) && cardRepo.existsByClientId(req.getClientId())) {
                throw new BadRequestException("Clientul are deja un card de fidelitate.");
            }

            existing.setClient(client);
        }

        return cardRepo.save(existing);
    }

    public void delete(Long id) {
        CardFidelitate existing = getById(id);
        cardRepo.delete(existing);
    }
}