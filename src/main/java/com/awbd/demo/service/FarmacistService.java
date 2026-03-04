
package com.awbd.demo.service;

import com.awbd.demo.entity.Farmacist;
import com.awbd.demo.exception.BadRequestException;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.FarmacistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FarmacistService {

    private final FarmacistRepository repo;

    public FarmacistService(FarmacistRepository repo) {
        this.repo = repo;
    }

    public Farmacist create(Farmacist farmacist) {
        // validare business: email unic (daca este completat)
        if (farmacist.getEmail() != null && !farmacist.getEmail().isBlank()) {
            if (repo.existsByEmail(farmacist.getEmail())) {
                throw new BadRequestException("Exista deja un farmacist cu acest email.");
            }
        }
        return repo.save(farmacist);
    }

    public List<Farmacist> getAll() {
        return repo.findAll();
    }

    public Farmacist getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farmacistul cu id " + id + " nu exista."));
    }

    public Farmacist update(Long id, Farmacist updated) {
        Farmacist existing = getById(id);

        if (updated.getEmail() != null && !updated.getEmail().isBlank()) {
            if (existing.getEmail() == null || !updated.getEmail().equals(existing.getEmail())) {
                if (repo.existsByEmail(updated.getEmail())) {
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

        return repo.save(existing);
    }

    public void delete(Long id) {
        Farmacist existing = getById(id); // daca nu exista -> 404
        repo.delete(existing);
    }
}