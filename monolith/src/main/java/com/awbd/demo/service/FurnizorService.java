package com.awbd.demo.service;

import com.awbd.demo.entity.Furnizor;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.FurnizorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FurnizorService {

    private static final Logger log = LoggerFactory.getLogger(FurnizorService.class);

    private final FurnizorRepository repo;

    public FurnizorService(FurnizorRepository repo) {
        this.repo = repo;
    }

    public Furnizor create(Furnizor furnizor) {
        log.info("Se incearca crearea unui furnizor nou");
        log.debug("Date primite pentru creare furnizor: nume={}, adresa={}, oras={}, tara={}, telefon={}",
                furnizor.getNume(), furnizor.getAdresa(), furnizor.getOras(),
                furnizor.getTara(), furnizor.getTelefon());

        Furnizor saved = repo.save(furnizor);
        log.info("Furnizor creat cu succes, id={}", saved.getId());
        return saved;
    }

    public Page<Furnizor> getAll(Pageable pageable) {
        log.info("Se solicita lista paginata a furnizorilor");
        log.debug("Page request: page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<Furnizor> furnizoriPage = repo.findAll(pageable);

        log.debug("Au fost gasiti {} furnizori pe pagina curenta din total {}",
                furnizoriPage.getNumberOfElements(), furnizoriPage.getTotalElements());

        return furnizoriPage;
    }

    public Furnizor getById(Long id) {
        log.info("Se cauta furnizorul cu id={}", id);

        return repo.findById(id)
                .orElseThrow(() -> {
                    log.error("Furnizorul cu id={} nu a fost gasit", id);
                    return new ResourceNotFoundException("Furnizorul cu id " + id + " nu exista.");
                });
    }

    public Furnizor update(Long id, Furnizor updated) {
        log.info("Se incearca actualizarea furnizorului cu id={}", id);
        log.debug("Date primite pentru update: nume={}, adresa={}, oras={}, tara={}, telefon={}",
                updated.getNume(), updated.getAdresa(), updated.getOras(),
                updated.getTara(), updated.getTelefon());

        Furnizor existing = getById(id);

        if (updated.getNume() != null) existing.setNume(updated.getNume());
        if (updated.getAdresa() != null) existing.setAdresa(updated.getAdresa());
        if (updated.getOras() != null) existing.setOras(updated.getOras());
        if (updated.getTara() != null) existing.setTara(updated.getTara());
        if (updated.getTelefon() != null) existing.setTelefon(updated.getTelefon());

        Furnizor saved = repo.save(existing);
        log.info("Furnizorul cu id={} a fost actualizat cu succes", saved.getId());
        return saved;
    }

    public void delete(Long id) {
        log.info("Se incearca stergerea furnizorului cu id={}", id);

        Furnizor existing = getById(id);
        repo.delete(existing);

        log.info("Furnizorul cu id={} a fost sters cu succes", id);
    }
}