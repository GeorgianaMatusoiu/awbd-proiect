package com.awbd.demo.service;

import com.awbd.demo.dto.RetetaRequest;
import com.awbd.demo.entity.Client;
import com.awbd.demo.entity.Farmacist;
import com.awbd.demo.entity.Reteta;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.ClientRepository;
import com.awbd.demo.repository.FarmacistRepository;
import com.awbd.demo.repository.RetetaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RetetaService {

    private static final Logger log = LoggerFactory.getLogger(RetetaService.class);

    private final RetetaRepository retetaRepo;
    private final ClientRepository clientRepo;
    private final FarmacistRepository farmacistRepo;

    public RetetaService(RetetaRepository retetaRepo, ClientRepository clientRepo, FarmacistRepository farmacistRepo) {
        this.retetaRepo = retetaRepo;
        this.clientRepo = clientRepo;
        this.farmacistRepo = farmacistRepo;
    }

    public Reteta create(RetetaRequest req) {
        log.info("Se incearca crearea unei retete noi");
        log.debug("Date primite pentru creare reteta: dataTiparire={}, clientId={}, farmacistId={}",
                req.getDataTiparire(), req.getClientId(), req.getFarmacistId());

        Client client = clientRepo.findById(req.getClientId())
                .orElseThrow(() -> {
                    log.error("Crearea retetei a esuat: clientul cu id={} nu a fost gasit", req.getClientId());
                    return new ResourceNotFoundException("Clientul cu id " + req.getClientId() + " nu exista.");
                });

        Farmacist farmacist = farmacistRepo.findById(req.getFarmacistId())
                .orElseThrow(() -> {
                    log.error("Crearea retetei a esuat: farmacistul cu id={} nu a fost gasit", req.getFarmacistId());
                    return new ResourceNotFoundException("Farmacistul cu id " + req.getFarmacistId() + " nu exista.");
                });

        Reteta r = new Reteta();
        r.setDataTiparire(req.getDataTiparire());
        r.setClient(client);
        r.setFarmacist(farmacist);

        Reteta saved = retetaRepo.save(r);
        log.info("Reteta a fost creata cu succes, id={}", saved.getId());
        return saved;
    }

    public Page<Reteta> getAll(Pageable pageable) {
        log.info("Se solicita lista paginata a retetelor");
        log.debug("Page request: page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<Reteta> retetePage = retetaRepo.findAll(pageable);

        log.debug("Au fost gasite {} retete pe pagina curenta din total {}",
                retetePage.getNumberOfElements(), retetePage.getTotalElements());

        return retetePage;
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
        log.debug("Date primite pentru update reteta: dataTiparire={}, clientId={}, farmacistId={}",
                req.getDataTiparire(), req.getClientId(), req.getFarmacistId());

        Reteta existing = getById(id);

        if (req.getDataTiparire() != null) {
            existing.setDataTiparire(req.getDataTiparire());
        }

        if (req.getClientId() != null) {
            Client client = clientRepo.findById(req.getClientId())
                    .orElseThrow(() -> {
                        log.error("Actualizarea retetei a esuat: clientul cu id={} nu a fost gasit", req.getClientId());
                        return new ResourceNotFoundException("Clientul cu id " + req.getClientId() + " nu exista.");
                    });
            existing.setClient(client);
        }

        if (req.getFarmacistId() != null) {
            Farmacist farmacist = farmacistRepo.findById(req.getFarmacistId())
                    .orElseThrow(() -> {
                        log.error("Actualizarea retetei a esuat: farmacistul cu id={} nu a fost gasit", req.getFarmacistId());
                        return new ResourceNotFoundException("Farmacistul cu id " + req.getFarmacistId() + " nu exista.");
                    });
            existing.setFarmacist(farmacist);
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