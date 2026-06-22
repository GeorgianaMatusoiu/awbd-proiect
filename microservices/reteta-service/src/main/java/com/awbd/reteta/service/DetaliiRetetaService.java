package com.awbd.reteta.service;

import com.awbd.reteta.dto.DetaliiRetetaRequest;
import com.awbd.reteta.entity.DetaliiReteta;
import com.awbd.reteta.entity.DetaliiRetetaId;
import com.awbd.reteta.entity.Reteta;
import com.awbd.reteta.exception.BadRequestException;
import com.awbd.reteta.exception.ResourceNotFoundException;
import com.awbd.reteta.repository.DetaliiRetetaRepository;
import com.awbd.reteta.repository.RetetaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DetaliiRetetaService {

    private static final Logger log = LoggerFactory.getLogger(DetaliiRetetaService.class);

    private final DetaliiRetetaRepository detaliiRepo;
    private final RetetaRepository retetaRepo;

    public DetaliiRetetaService(DetaliiRetetaRepository detaliiRepo,
                                RetetaRepository retetaRepo) {
        this.detaliiRepo = detaliiRepo;
        this.retetaRepo = retetaRepo;
    }

    public DetaliiReteta create(DetaliiRetetaRequest req) {
        log.info("Se incearca crearea unui detaliu de reteta nou");

        Reteta reteta = retetaRepo.findById(req.getRetetaId())
                .orElseThrow(() -> {
                    log.error("Reteta cu id={} nu a fost gasita", req.getRetetaId());
                    return new ResourceNotFoundException("Reteta cu id " + req.getRetetaId() + " nu exista.");
                });

        DetaliiRetetaId id = new DetaliiRetetaId(req.getRetetaId(), req.getMedicamentId());

        if (detaliiRepo.existsById(id)) {
            log.error("Exista deja medicamentul cu id={} in reteta cu id={}",
                    req.getMedicamentId(), req.getRetetaId());
            throw new BadRequestException("Exista deja acest medicament in reteta.");
        }

        DetaliiReteta detalii = new DetaliiReteta();
        detalii.setId(id);
        detalii.setReteta(reteta);
        detalii.setMedicamentId(req.getMedicamentId());
        detalii.setPret(req.getPret());
        detalii.setCantitate(req.getCantitate());

        DetaliiReteta saved = detaliiRepo.save(detalii);

        log.info("Detaliul de reteta a fost creat cu succes pentru retetaId={} si medicamentId={}",
                saved.getId().getRetetaId(), saved.getId().getMedicamentId());

        return saved;
    }

    public Page<DetaliiReteta> getAll(Pageable pageable) {
        log.info("Se solicita lista paginata a detaliilor de retete");
        return detaliiRepo.findAll(pageable);
    }

    public DetaliiReteta getById(Long retetaId, Long medicamentId) {
        log.info("Se cauta detaliul pentru retetaId={} si medicamentId={}", retetaId, medicamentId);

        DetaliiRetetaId id = new DetaliiRetetaId(retetaId, medicamentId);

        return detaliiRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Detaliile pentru retetaId={} si medicamentId={} nu au fost gasite",
                            retetaId, medicamentId);
                    return new ResourceNotFoundException(
                            "Detaliile pentru reteta " + retetaId
                                    + " si medicamentul " + medicamentId + " nu exista."
                    );
                });
    }

    public DetaliiReteta update(Long retetaId, Long medicamentId, DetaliiRetetaRequest req) {
        log.info("Se incearca actualizarea detaliului pentru retetaId={} si medicamentId={}",
                retetaId, medicamentId);

        DetaliiReteta existing = getById(retetaId, medicamentId);

        if (req.getPret() != null) {
            existing.setPret(req.getPret());
        }

        if (req.getCantitate() != null) {
            existing.setCantitate(req.getCantitate());
        }

        DetaliiReteta saved = detaliiRepo.save(existing);

        log.info("Detaliul pentru retetaId={} si medicamentId={} a fost actualizat cu succes",
                retetaId, medicamentId);

        return saved;
    }

    public void delete(Long retetaId, Long medicamentId) {
        log.info("Se incearca stergerea detaliului pentru retetaId={} si medicamentId={}",
                retetaId, medicamentId);

        DetaliiReteta existing = getById(retetaId, medicamentId);
        detaliiRepo.delete(existing);

        log.info("Detaliul pentru retetaId={} si medicamentId={} a fost sters cu succes",
                retetaId, medicamentId);
    }
}