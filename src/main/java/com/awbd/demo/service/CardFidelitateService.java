package com.awbd.demo.service;

import com.awbd.demo.dto.CardFidelitateRequest;
import com.awbd.demo.entity.CardFidelitate;
import com.awbd.demo.entity.Client;
import com.awbd.demo.exception.BadRequestException;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.CardFidelitateRepository;
import com.awbd.demo.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CardFidelitateService {

    private static final Logger log = LoggerFactory.getLogger(CardFidelitateService.class);

    private final CardFidelitateRepository cardRepo;
    private final ClientRepository clientRepo;

    public CardFidelitateService(CardFidelitateRepository cardRepo, ClientRepository clientRepo) {
        this.cardRepo = cardRepo;
        this.clientRepo = clientRepo;
    }

    public CardFidelitate create(CardFidelitateRequest req) {
        log.info("Se incearca crearea unui card de fidelitate nou");
        log.debug("Date primite pentru creare card: nivel={}, puncte={}, clientId={}",
                req.getNivel(), req.getPuncte(), req.getClientId());

        Client client = clientRepo.findById(req.getClientId())
                .orElseThrow(() -> {
                    log.error("Crearea cardului a esuat: clientul cu id={} nu a fost gasit", req.getClientId());
                    return new ResourceNotFoundException("Clientul cu id " + req.getClientId() + " nu exista.");
                });

        if (cardRepo.existsByClientId(req.getClientId())) {
            log.error("Crearea cardului a esuat: clientul cu id={} are deja card de fidelitate", req.getClientId());
            throw new BadRequestException("Clientul are deja un card de fidelitate.");
        }

        CardFidelitate card = new CardFidelitate();
        card.setNivel(req.getNivel());
        card.setPuncte(req.getPuncte());
        card.setClient(client);

        CardFidelitate saved = cardRepo.save(card);
        log.info("Cardul de fidelitate a fost creat cu succes, id={}", saved.getId());
        return saved;
    }

    public Page<CardFidelitate> getAll(Pageable pageable) {
        log.info("Se solicita lista paginata a cardurilor de fidelitate");
        log.debug("Page request: page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<CardFidelitate> carduriPage = cardRepo.findAll(pageable);

        log.debug("Au fost gasite {} carduri pe pagina curenta din total {}",
                carduriPage.getNumberOfElements(), carduriPage.getTotalElements());

        return carduriPage;
    }

    public CardFidelitate getById(Long id) {
        log.info("Se cauta cardul de fidelitate cu id={}", id);

        return cardRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Cardul de fidelitate cu id={} nu a fost gasit", id);
                    return new ResourceNotFoundException("Cardul cu id " + id + " nu exista.");
                });
    }

    public CardFidelitate update(Long id, CardFidelitateRequest req) {
        log.info("Se incearca actualizarea cardului de fidelitate cu id={}", id);
        log.debug("Date primite pentru update card: nivel={}, puncte={}, clientId={}",
                req.getNivel(), req.getPuncte(), req.getClientId());

        CardFidelitate existing = getById(id);

        if (req.getNivel() != null) {
            existing.setNivel(req.getNivel());
        }

        if (req.getPuncte() != null) {
            existing.setPuncte(req.getPuncte());
        }

        if (req.getClientId() != null) {
            Client client = clientRepo.findById(req.getClientId())
                    .orElseThrow(() -> {
                        log.error("Actualizarea cardului a esuat: clientul cu id={} nu a fost gasit", req.getClientId());
                        return new ResourceNotFoundException("Clientul cu id " + req.getClientId() + " nu exista.");
                    });

            if (!existing.getClient().getId().equals(req.getClientId()) && cardRepo.existsByClientId(req.getClientId())) {
                log.error("Actualizarea cardului a esuat: clientul cu id={} are deja card de fidelitate", req.getClientId());
                throw new BadRequestException("Clientul are deja un card de fidelitate.");
            }

            existing.setClient(client);
        }

        CardFidelitate saved = cardRepo.save(existing);
        log.info("Cardul de fidelitate cu id={} a fost actualizat cu succes", saved.getId());
        return saved;
    }

    public void delete(Long id) {
        log.info("Se incearca stergerea cardului de fidelitate cu id={}", id);

        CardFidelitate existing = getById(id);
        cardRepo.delete(existing);

        log.info("Cardul de fidelitate cu id={} a fost sters cu succes", id);
    }
}