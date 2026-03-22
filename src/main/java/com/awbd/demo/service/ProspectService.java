package com.awbd.demo.service;

import com.awbd.demo.entity.Prospect;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.ProspectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProspectService {

    private static final Logger log = LoggerFactory.getLogger(ProspectService.class);

    private final ProspectRepository repo;

    public ProspectService(ProspectRepository repo) {
        this.repo = repo;
    }

    public Prospect create(Prospect prospect) {
        log.info("Se incearca crearea unui prospect nou");
        log.debug("Date primite pentru creare prospect: afectiuni={}, administrare={}",
                prospect.getAfectiuni(), prospect.getAdministrare());

        Prospect saved = repo.save(prospect);
        log.info("Prospect creat cu succes, id={}", saved.getId());
        return saved;
    }

    public Page<Prospect> getAll(Pageable pageable) {
        log.info("Se solicita lista paginata a prospectelor");
        log.debug("Page request: page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<Prospect> prospectePage = repo.findAll(pageable);

        log.debug("Au fost gasite {} prospecte pe pagina curenta din total {}",
                prospectePage.getNumberOfElements(), prospectePage.getTotalElements());

        return prospectePage;
    }

    public Prospect getById(Long id) {
        log.info("Se cauta prospectul cu id={}", id);

        return repo.findById(id)
                .orElseThrow(() -> {
                    log.error("Prospectul cu id={} nu a fost gasit", id);
                    return new ResourceNotFoundException("Prospectul cu id " + id + " nu exista.");
                });
    }

    public Prospect update(Long id, Prospect updated) {
        log.info("Se incearca actualizarea prospectului cu id={}", id);
        log.debug("Date primite pentru update: afectiuni={}, administrare={}",
                updated.getAfectiuni(), updated.getAdministrare());

        Prospect existing = getById(id);

        if (updated.getAfectiuni() != null) existing.setAfectiuni(updated.getAfectiuni());
        if (updated.getAdministrare() != null) existing.setAdministrare(updated.getAdministrare());

        Prospect saved = repo.save(existing);
        log.info("Prospectul cu id={} a fost actualizat cu succes", saved.getId());
        return saved;
    }

    public void delete(Long id) {
        log.info("Se incearca stergerea prospectului cu id={}", id);

        Prospect existing = getById(id);
        repo.delete(existing);

        log.info("Prospectul cu id={} a fost sters cu succes", id);
    }
}