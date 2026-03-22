package com.awbd.demo.service;

import com.awbd.demo.dto.MedicamentRequest;
import com.awbd.demo.entity.CategorieMedicament;
import com.awbd.demo.entity.Furnizor;
import com.awbd.demo.entity.Medicament;
import com.awbd.demo.entity.Prospect;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.CategorieMedicamentRepository;
import com.awbd.demo.repository.FurnizorRepository;
import com.awbd.demo.repository.MedicamentRepository;
import com.awbd.demo.repository.ProspectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MedicamentService {

    private static final Logger log = LoggerFactory.getLogger(MedicamentService.class);

    private final MedicamentRepository medicamentRepo;
    private final FurnizorRepository furnizorRepo;
    private final ProspectRepository prospectRepo;
    private final CategorieMedicamentRepository categorieRepo;

    public MedicamentService(MedicamentRepository medicamentRepo,
                             FurnizorRepository furnizorRepo,
                             ProspectRepository prospectRepo,
                             CategorieMedicamentRepository categorieRepo) {
        this.medicamentRepo = medicamentRepo;
        this.furnizorRepo = furnizorRepo;
        this.prospectRepo = prospectRepo;
        this.categorieRepo = categorieRepo;
    }

    public Medicament create(MedicamentRequest req) {
        log.info("Se incearca crearea unui medicament nou");
        log.debug("Date primite pentru creare medicament: denumire={}, dataExpirare={}, pret={}, furnizorId={}, prospectId={}, categorieId={}",
                req.getDenumire(), req.getDataExpirare(), req.getPret(),
                req.getFurnizorId(), req.getProspectId(), req.getCategorieId());

        Furnizor furnizor = furnizorRepo.findById(req.getFurnizorId())
                .orElseThrow(() -> {
                    log.error("Crearea medicamentului a esuat: furnizorul cu id={} nu a fost gasit", req.getFurnizorId());
                    return new ResourceNotFoundException("Furnizorul cu id " + req.getFurnizorId() + " nu exista.");
                });

        Prospect prospect = prospectRepo.findById(req.getProspectId())
                .orElseThrow(() -> {
                    log.error("Crearea medicamentului a esuat: prospectul cu id={} nu a fost gasit", req.getProspectId());
                    return new ResourceNotFoundException("Prospectul cu id " + req.getProspectId() + " nu exista.");
                });

        CategorieMedicament categorie = categorieRepo.findById(req.getCategorieId())
                .orElseThrow(() -> {
                    log.error("Crearea medicamentului a esuat: categoria cu id={} nu a fost gasita", req.getCategorieId());
                    return new ResourceNotFoundException("Categoria cu id " + req.getCategorieId() + " nu exista.");
                });

        Medicament medicament = new Medicament();
        medicament.setDenumire(req.getDenumire());
        medicament.setDataExpirare(req.getDataExpirare());
        medicament.setPret(req.getPret());
        medicament.setFurnizor(furnizor);
        medicament.setProspect(prospect);
        medicament.setCategorie(categorie);

        Medicament saved = medicamentRepo.save(medicament);
        log.info("Medicament creat cu succes, id={}", saved.getId());
        return saved;
    }

    public Page<Medicament> getAll(Pageable pageable) {
        log.info("Se solicita lista paginata a medicamentelor");
        log.debug("Page request: page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<Medicament> medicamentePage = medicamentRepo.findAll(pageable);

        log.debug("Au fost gasite {} medicamente pe pagina curenta din total {}",
                medicamentePage.getNumberOfElements(), medicamentePage.getTotalElements());

        return medicamentePage;
    }

    public Medicament getById(Long id) {
        log.info("Se cauta medicamentul cu id={}", id);

        return medicamentRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Medicamentul cu id={} nu a fost gasit", id);
                    return new ResourceNotFoundException("Medicamentul cu id " + id + " nu exista.");
                });
    }

    public Medicament update(Long id, MedicamentRequest req) {
        log.info("Se incearca actualizarea medicamentului cu id={}", id);
        log.debug("Date primite pentru update medicament: denumire={}, dataExpirare={}, pret={}, furnizorId={}, prospectId={}, categorieId={}",
                req.getDenumire(), req.getDataExpirare(), req.getPret(),
                req.getFurnizorId(), req.getProspectId(), req.getCategorieId());

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

        if (req.getFurnizorId() != null) {
            Furnizor furnizor = furnizorRepo.findById(req.getFurnizorId())
                    .orElseThrow(() -> {
                        log.error("Actualizarea medicamentului a esuat: furnizorul cu id={} nu a fost gasit", req.getFurnizorId());
                        return new ResourceNotFoundException("Furnizorul cu id " + req.getFurnizorId() + " nu exista.");
                    });
            existing.setFurnizor(furnizor);
        }

        if (req.getProspectId() != null) {
            Prospect prospect = prospectRepo.findById(req.getProspectId())
                    .orElseThrow(() -> {
                        log.error("Actualizarea medicamentului a esuat: prospectul cu id={} nu a fost gasit", req.getProspectId());
                        return new ResourceNotFoundException("Prospectul cu id " + req.getProspectId() + " nu exista.");
                    });
            existing.setProspect(prospect);
        }

        if (req.getCategorieId() != null) {
            CategorieMedicament categorie = categorieRepo.findById(req.getCategorieId())
                    .orElseThrow(() -> {
                        log.error("Actualizarea medicamentului a esuat: categoria cu id={} nu a fost gasita", req.getCategorieId());
                        return new ResourceNotFoundException("Categoria cu id " + req.getCategorieId() + " nu exista.");
                    });
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