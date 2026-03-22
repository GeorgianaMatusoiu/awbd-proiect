package com.awbd.demo.service;

import com.awbd.demo.entity.Client;
import com.awbd.demo.exception.BadRequestException;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository repo;

    public ClientService(ClientRepository repo) {
        this.repo = repo;
    }

    public Client create(Client client) {
        log.info("Se incearca crearea unui client nou");
        log.debug("Date primite pentru creare client: nume={}, prenume={}, cnp={}",
                client.getNume(), client.getPrenume(), client.getCnp());

        if (client.getCnp() == null || client.getCnp().isBlank()) {
            log.error("Crearea clientului a esuat: CNP lipsa");
            throw new BadRequestException("CNP este obligatoriu.");
        }

        if (repo.existsByCnp(client.getCnp())) {
            log.error("Crearea clientului a esuat: exista deja client cu CNP {}", client.getCnp());
            throw new BadRequestException("Exista deja un client cu acest CNP.");
        }

        Client saved = repo.save(client);
        log.info("Client creat cu succes, id={}", saved.getId());
        return saved;
    }

    public Page<Client> getAll(Pageable pageable) {
        log.info("Se solicita lista paginata a clientilor");
        log.debug("Page request: page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<Client> clientsPage = repo.findAll(pageable);

        log.debug("Au fost gasiti {} clienti pe pagina curenta din total {}",
                clientsPage.getNumberOfElements(), clientsPage.getTotalElements());

        return clientsPage;
    }

    public Client getById(Long id) {
        log.info("Se cauta clientul cu id={}", id);

        return repo.findById(id)
                .orElseThrow(() -> {
                    log.error("Clientul cu id={} nu a fost gasit", id);
                    return new ResourceNotFoundException("Clientul cu id " + id + " nu exista.");
                });
    }

    public Client update(Long id, Client updated) {
        log.info("Se incearca actualizarea clientului cu id={}", id);
        log.debug("Date primite pentru update: nume={}, prenume={}, cnp={}, varsta={}, telefon={}",
                updated.getNume(), updated.getPrenume(), updated.getCnp(),
                updated.getVarsta(), updated.getTelefon());

        Client existing = getById(id);

        if (updated.getCnp() != null && !updated.getCnp().isBlank()) {
            if (!updated.getCnp().equals(existing.getCnp()) && repo.existsByCnp(updated.getCnp())) {
                log.error("Actualizarea clientului a esuat: CNP duplicat {}", updated.getCnp());
                throw new BadRequestException("Exista deja un client cu acest CNP.");
            }
            existing.setCnp(updated.getCnp());
        }

        if (updated.getNume() != null) existing.setNume(updated.getNume());
        if (updated.getPrenume() != null) existing.setPrenume(updated.getPrenume());
        if (updated.getVarsta() != null) existing.setVarsta(updated.getVarsta());
        if (updated.getTelefon() != null) existing.setTelefon(updated.getTelefon());

        Client saved = repo.save(existing);
        log.info("Clientul cu id={} a fost actualizat cu succes", saved.getId());
        return saved;
    }

    public void delete(Long id) {
        log.info("Se incearca stergerea clientului cu id={}", id);

        Client existing = getById(id);
        repo.delete(existing);

        log.info("Clientul cu id={} a fost sters cu succes", id);
    }
}