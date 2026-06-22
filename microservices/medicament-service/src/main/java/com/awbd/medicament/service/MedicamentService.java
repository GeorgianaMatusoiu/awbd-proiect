package com.awbd.medicament.service;

import com.awbd.medicament.dto.MedicamentRequest;
import com.awbd.medicament.entity.CategorieMedicament;
import com.awbd.medicament.entity.Furnizor;
import com.awbd.medicament.entity.Medicament;
import com.awbd.medicament.entity.Prospect;
import com.awbd.medicament.exception.ResourceNotFoundException;
import com.awbd.medicament.repository.CategorieMedicamentRepository;
import com.awbd.medicament.repository.FurnizorRepository;
import com.awbd.medicament.repository.MedicamentRepository;
import com.awbd.medicament.repository.ProspectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MedicamentService {

    private static final Logger log = LoggerFactory.getLogger(MedicamentService.class);

    private final MedicamentRepository medicamentRepo;
    private final ProspectRepository prospectRepo;
    private final FurnizorRepository furnizorRepo;
    private final CategorieMedicamentRepository categorieRepo;

    public MedicamentService(MedicamentRepository medicamentRepo,
                             ProspectRepository prospectRepo,
                             FurnizorRepository furnizorRepo,
                             CategorieMedicamentRepository categorieRepo) {
        this.medicamentRepo = medicamentRepo;
        this.prospectRepo = prospectRepo;
        this.furnizorRepo = furnizorRepo;
        this.categorieRepo = categorieRepo;
    }

    public Medicament create(MedicamentRequest req) {
        log.info("Se incearca crearea unui medicament nou");

        Prospect prospect = prospectRepo.findById(req.getProspectId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Prospectul cu id " + req.getProspectId() + " nu exista."
                ));

        Furnizor furnizor = furnizorRepo.findById(req.getFurnizorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Furnizorul cu id " + req.getFurnizorId() + " nu exista."
                ));

        CategorieMedicament categorie = categorieRepo.findById(req.getCategorieId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria cu id " + req.getCategorieId() + " nu exista."
                ));

        Medicament medicament = new Medicament();
        medicament.setDenumire(req.getDenumire());
        medicament.setDataExpirare(req.getDataExpirare());
        medicament.setPret(req.getPret());
        medicament.setProspect(prospect);
        medicament.setFurnizor(furnizor);
        medicament.setCategorie(categorie);

        Medicament saved = medicamentRepo.save(medicament);
        log.info("Medicament creat cu succes, id={}", saved.getId());

        return saved;
    }

    public Page<Medicament> getAll(Pageable pageable) {
        log.info("Se solicita lista paginata a medicamentelor");
        return medicamentRepo.findAll(pageable);
    }

    public Medicament getById(Long id) {
        log.info("Se cauta medicamentul cu id={}", id);

        return medicamentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Medicamentul cu id " + id + " nu exista."
                ));
    }

    public Medicament update(Long id, MedicamentRequest req) {
        log.info("Se incearca actualizarea medicamentului cu id={}", id);

        Medicament existing = getById(id);

        if (req.getDenumire() != null) {
            existing.setDenumire(req.getDenumire());
        }

        if (req.getDataExpirare() != null) {
            existing.setDataExpirare(req.getDataExpirare());
        }

        if (req.getPret() != null) {
            existing.setPret(req.getPret());
        }

        if (req.getProspectId() != null) {
            Prospect prospect = prospectRepo.findById(req.getProspectId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Prospectul cu id " + req.getProspectId() + " nu exista."
                    ));
            existing.setProspect(prospect);
        }

        if (req.getFurnizorId() != null) {
            Furnizor furnizor = furnizorRepo.findById(req.getFurnizorId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Furnizorul cu id " + req.getFurnizorId() + " nu exista."
                    ));
            existing.setFurnizor(furnizor);
        }

        if (req.getCategorieId() != null) {
            CategorieMedicament categorie = categorieRepo.findById(req.getCategorieId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Categoria cu id " + req.getCategorieId() + " nu exista."
                    ));
            existing.setCategorie(categorie);
        }

        Medicament saved = medicamentRepo.save(existing);
        log.info("Medicamentul cu id={} a fost actualizat cu succes", saved.getId());

        return saved;
    }

    public void delete(Long id) {
        log.info("Se incearca stergerea medicamentului cu id={}", id);

        Medicament existing = getById(id);
        medicamentRepo.delete(existing);

        log.info("Medicamentul cu id={} a fost sters cu succes", id);
    }
}