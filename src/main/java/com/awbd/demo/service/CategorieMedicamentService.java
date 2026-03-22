package com.awbd.demo.service;

import com.awbd.demo.entity.CategorieMedicament;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.CategorieMedicamentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategorieMedicamentService {

    private static final Logger log = LoggerFactory.getLogger(CategorieMedicamentService.class);

    private final CategorieMedicamentRepository repo;

    public CategorieMedicamentService(CategorieMedicamentRepository repo) {
        this.repo = repo;
    }

    public CategorieMedicament create(CategorieMedicament categorie) {
        log.info("Se incearca crearea unei categorii de medicament");
        log.debug("Date primite pentru creare categorie: stoc={}, temperatura={}",
                categorie.getStoc(), categorie.getTemperatura());

        CategorieMedicament saved = repo.save(categorie);
        log.info("Categoria a fost creata cu succes, id={}", saved.getId());
        return saved;
    }

    public Page<CategorieMedicament> getAll(Pageable pageable) {
        log.info("Se solicita lista paginata a categoriilor de medicamente");
        log.debug("Page request: page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<CategorieMedicament> categoriiPage = repo.findAll(pageable);

        log.debug("Au fost gasite {} categorii pe pagina curenta din total {}",
                categoriiPage.getNumberOfElements(), categoriiPage.getTotalElements());

        return categoriiPage;
    }

    public CategorieMedicament getById(Long id) {
        log.info("Se cauta categoria cu id={}", id);

        return repo.findById(id)
                .orElseThrow(() -> {
                    log.error("Categoria cu id={} nu a fost gasita", id);
                    return new ResourceNotFoundException("Categoria cu id " + id + " nu exista.");
                });
    }

    public CategorieMedicament update(Long id, CategorieMedicament updated) {
        log.info("Se incearca actualizarea categoriei cu id={}", id);
        log.debug("Date primite pentru update: stoc={}, temperatura={}",
                updated.getStoc(), updated.getTemperatura());

        CategorieMedicament existing = getById(id);

        if (updated.getStoc() != null) existing.setStoc(updated.getStoc());
        if (updated.getTemperatura() != null) existing.setTemperatura(updated.getTemperatura());

        CategorieMedicament saved = repo.save(existing);
        log.info("Categoria cu id={} a fost actualizata cu succes", saved.getId());
        return saved;
    }

    public void delete(Long id) {
        log.info("Se incearca stergerea categoriei cu id={}", id);

        CategorieMedicament existing = getById(id);
        repo.delete(existing);

        log.info("Categoria cu id={} a fost stearsa cu succes", id);
    }
}