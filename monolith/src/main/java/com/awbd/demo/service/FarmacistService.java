package com.awbd.demo.service;

import com.awbd.demo.entity.Farmacist;
import com.awbd.demo.exception.BadRequestException;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.FarmacistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FarmacistService {

    private static final Logger log = LoggerFactory.getLogger(FarmacistService.class);

    private final FarmacistRepository repo;

    public FarmacistService(FarmacistRepository repo) {
        this.repo = repo;
    }

    public Farmacist create(Farmacist farmacist) {
        log.info("Se incearca crearea unui farmacist nou");
        log.debug("Date primite pentru creare farmacist: nume={}, prenume={}, email={}",
                farmacist.getNume(), farmacist.getPrenume(), farmacist.getEmail());

        if (farmacist.getEmail() != null && !farmacist.getEmail().isBlank()) {
            if (repo.existsByEmail(farmacist.getEmail())) {
                log.error("Crearea farmacistului a esuat: exista deja farmacist cu email {}", farmacist.getEmail());
                throw new BadRequestException("Exista deja un farmacist cu acest email.");
            }
        }

        Farmacist saved = repo.save(farmacist);
        log.info("Farmacist creat cu succes, id={}", saved.getId());
        return saved;
    }

    public Page<Farmacist> getAll(Pageable pageable) {
        log.info("Se solicita lista paginata a farmacistilor");
        log.debug("Page request: page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<Farmacist> farmacistiPage = repo.findAll(pageable);

        log.debug("Au fost gasiti {} farmacisti pe pagina curenta din total {}",
                farmacistiPage.getNumberOfElements(), farmacistiPage.getTotalElements());

        return farmacistiPage;
    }

    public Farmacist getById(Long id) {
        log.info("Se cauta farmacistul cu id={}", id);

        return repo.findById(id)
                .orElseThrow(() -> {
                    log.error("Farmacistul cu id={} nu a fost gasit", id);
                    return new ResourceNotFoundException("Farmacistul cu id " + id + " nu exista.");
                });
    }

    public Farmacist update(Long id, Farmacist updated) {
        log.info("Se incearca actualizarea farmacistului cu id={}", id);
        log.debug("Date primite pentru update: nume={}, prenume={}, email={}, telefon={}, dataAngajarii={}, salariu={}",
                updated.getNume(), updated.getPrenume(), updated.getEmail(),
                updated.getTelefon(), updated.getDataAngajarii(), updated.getSalariu());

        Farmacist existing = getById(id);

        if (updated.getEmail() != null && !updated.getEmail().isBlank()) {
            if (existing.getEmail() == null || !updated.getEmail().equals(existing.getEmail())) {
                if (repo.existsByEmail(updated.getEmail())) {
                    log.error("Actualizarea farmacistului a esuat: email duplicat {}", updated.getEmail());
                    throw new BadRequestException("Exista deja un farmacist cu acest email.");
                }
            }
            existing.setEmail(updated.getEmail());
        }

        if (updated.getNume() != null) existing.setNume(updated.getNume());
        if (updated.getPrenume() != null) existing.setPrenume(updated.getPrenume());
        if (updated.getTelefon() != null) existing.setTelefon(updated.getTelefon());
        if (updated.getDataAngajarii() != null) existing.setDataAngajarii(updated.getDataAngajarii());
        if (updated.getSalariu() != null) existing.setSalariu(updated.getSalariu());

        Farmacist saved = repo.save(existing);
        log.info("Farmacistul cu id={} a fost actualizat cu succes", saved.getId());
        return saved;
    }

    public void delete(Long id) {
        log.info("Se incearca stergerea farmacistului cu id={}", id);

        Farmacist existing = getById(id);
        repo.delete(existing);

        log.info("Farmacistul cu id={} a fost sters cu succes", id);
    }
}