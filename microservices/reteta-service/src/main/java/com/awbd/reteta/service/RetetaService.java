package com.awbd.reteta.service;

import com.awbd.reteta.dto.RetetaRequest;
import com.awbd.reteta.entity.Reteta;
import com.awbd.reteta.exception.ResourceNotFoundException;
import com.awbd.reteta.repository.RetetaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RetetaService {

    private static final Logger log = LoggerFactory.getLogger(RetetaService.class);

    private final RetetaRepository retetaRepo;

    public RetetaService(RetetaRepository retetaRepo) {
        this.retetaRepo = retetaRepo;
    }

    public Reteta create(RetetaRequest req) {
        log.info("Se incearca crearea unei retete noi");
        log.debug("Date primite pentru creare reteta: dataTiparire={}, clientId={}, farmacistId={}",
                req.getDataTiparire(), req.getClientId(), req.getFarmacistId());

        Reteta r = new Reteta();
        r.setDataTiparire(req.getDataTiparire());
        r.setClientId(req.getClientId());
        r.setFarmacistId(req.getFarmacistId());

        Reteta saved = retetaRepo.save(r);
        log.info("Reteta a fost creata cu succes, id={}", saved.getId());

        return saved;
    }

    public Page<Reteta> getAll(Pageable pageable) {
        log.info("Se solicita lista paginata a retetelor");
        return retetaRepo.findAll(pageable);
    }

    public Reteta getById(Long id) {
        log.info("Se cauta reteta cu id={}", id);

        return retetaRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Reteta cu id={} nu a fost gasita", id);
                    return new ResourceNotFoundException("Reteta cu id " + id + " nu exista.");
                });
    }

    public Reteta update(Long id, RetetaRequest req) {
        log.info("Se incearca actualizarea retetei cu id={}", id);

        Reteta existing = getById(id);

        if (req.getDataTiparire() != null) {
            existing.setDataTiparire(req.getDataTiparire());
        }

        if (req.getClientId() != null) {
            existing.setClientId(req.getClientId());
        }

        if (req.getFarmacistId() != null) {
            existing.setFarmacistId(req.getFarmacistId());
        }

        Reteta saved = retetaRepo.save(existing);
        log.info("Reteta cu id={} a fost actualizata cu succes", saved.getId());

        return saved;
    }

    public void delete(Long id) {
        log.info("Se incearca stergerea retetei cu id={}", id);

        Reteta existing = getById(id);
        retetaRepo.delete(existing);

        log.info("Reteta cu id={} a fost stearsa cu succes", id);
    }
}