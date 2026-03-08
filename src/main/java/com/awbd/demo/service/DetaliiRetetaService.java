package com.awbd.demo.service;

import com.awbd.demo.dto.DetaliiRetetaRequest;
import com.awbd.demo.entity.*;
import com.awbd.demo.exception.BadRequestException;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.DetaliiRetetaRepository;
import com.awbd.demo.repository.MedicamentRepository;
import com.awbd.demo.repository.RetetaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetaliiRetetaService {

    private final DetaliiRetetaRepository detaliiRepo;
    private final RetetaRepository retetaRepo;
    private final MedicamentRepository medicamentRepo;

    public DetaliiRetetaService(DetaliiRetetaRepository detaliiRepo,
                                RetetaRepository retetaRepo,
                                MedicamentRepository medicamentRepo) {
        this.detaliiRepo = detaliiRepo;
        this.retetaRepo = retetaRepo;
        this.medicamentRepo = medicamentRepo;
    }

    public DetaliiReteta create(DetaliiRetetaRequest req) {
        Reteta reteta = retetaRepo.findById(req.getRetetaId())
                .orElseThrow(() -> new ResourceNotFoundException("Reteta cu id " + req.getRetetaId() + " nu exista."));

        Medicament medicament = medicamentRepo.findById(req.getMedicamentId())
                .orElseThrow(() -> new ResourceNotFoundException("Medicamentul cu id " + req.getMedicamentId() + " nu exista."));

        DetaliiRetetaId id = new DetaliiRetetaId(req.getRetetaId(), req.getMedicamentId());

        if (detaliiRepo.existsById(id)) {
            throw new BadRequestException("Exista deja acest medicament in reteta.");
        }

        DetaliiReteta detalii = new DetaliiReteta();
        detalii.setId(id);
        detalii.setReteta(reteta);
        detalii.setMedicament(medicament);
        detalii.setPret(req.getPret());
        detalii.setCantitate(req.getCantitate());

        return detaliiRepo.save(detalii);
    }

    public List<DetaliiReteta> getAll() {
        return detaliiRepo.findAll();
    }

    public DetaliiReteta getById(Long retetaId, Long medicamentId) {
        DetaliiRetetaId id = new DetaliiRetetaId(retetaId, medicamentId);

        return detaliiRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Detaliile pentru reteta " + retetaId + " si medicamentul " + medicamentId + " nu exista."
                ));
    }

    public DetaliiReteta update(Long retetaId, Long medicamentId, DetaliiRetetaRequest req) {
        DetaliiReteta existing = getById(retetaId, medicamentId);

        if (req.getPret() != null) {
            existing.setPret(req.getPret());
        }

        if (req.getCantitate() != null) {
            existing.setCantitate(req.getCantitate());
        }

        return detaliiRepo.save(existing);
    }

    public void delete(Long retetaId, Long medicamentId) {
        DetaliiReteta existing = getById(retetaId, medicamentId);
        detaliiRepo.delete(existing);
    }
}