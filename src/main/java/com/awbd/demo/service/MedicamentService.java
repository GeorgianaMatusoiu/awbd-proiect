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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicamentService {

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
        Furnizor furnizor = furnizorRepo.findById(req.getFurnizorId())
                .orElseThrow(() -> new ResourceNotFoundException("Furnizorul cu id " + req.getFurnizorId() + " nu exista."));

        Prospect prospect = prospectRepo.findById(req.getProspectId())
                .orElseThrow(() -> new ResourceNotFoundException("Prospectul cu id " + req.getProspectId() + " nu exista."));

        CategorieMedicament categorie = categorieRepo.findById(req.getCategorieId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria cu id " + req.getCategorieId() + " nu exista."));

        Medicament medicament = new Medicament();
        medicament.setDenumire(req.getDenumire());
        medicament.setDataExpirare(req.getDataExpirare());
        medicament.setPret(req.getPret());
        medicament.setFurnizor(furnizor);
        medicament.setProspect(prospect);
        medicament.setCategorie(categorie);

        return medicamentRepo.save(medicament);
    }

    public List<Medicament> getAll() {
        return medicamentRepo.findAll();
    }

    public Medicament getById(Long id) {
        return medicamentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicamentul cu id " + id + " nu exista."));
    }

    public Medicament update(Long id, MedicamentRequest req) {
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
                    .orElseThrow(() -> new ResourceNotFoundException("Furnizorul cu id " + req.getFurnizorId() + " nu exista."));
            existing.setFurnizor(furnizor);
        }

        if (req.getProspectId() != null) {
            Prospect prospect = prospectRepo.findById(req.getProspectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Prospectul cu id " + req.getProspectId() + " nu exista."));
            existing.setProspect(prospect);
        }

        if (req.getCategorieId() != null) {
            CategorieMedicament categorie = categorieRepo.findById(req.getCategorieId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria cu id " + req.getCategorieId() + " nu exista."));
            existing.setCategorie(categorie);
        }

        return medicamentRepo.save(existing);
    }

    public void delete(Long id) {
        Medicament existing = getById(id);
        medicamentRepo.delete(existing);
    }
}