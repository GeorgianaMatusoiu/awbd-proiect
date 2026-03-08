package com.awbd.demo.service;

import com.awbd.demo.entity.Furnizor;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.FurnizorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FurnizorService {

    private final FurnizorRepository repo;

    public FurnizorService(FurnizorRepository repo) {
        this.repo = repo;
    }

    public Furnizor create(Furnizor furnizor) {
        return repo.save(furnizor);
    }

    public List<Furnizor> getAll() {
        return repo.findAll();
    }

    public Furnizor getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Furnizorul cu id " + id + " nu exista."));
    }

    public Furnizor update(Long id, Furnizor updated) {
        Furnizor existing = getById(id);

        if (updated.getNume() != null) existing.setNume(updated.getNume());
        if (updated.getAdresa() != null) existing.setAdresa(updated.getAdresa());
        if (updated.getOras() != null) existing.setOras(updated.getOras());
        if (updated.getTara() != null) existing.setTara(updated.getTara());
        if (updated.getTelefon() != null) existing.setTelefon(updated.getTelefon());

        return repo.save(existing);
    }

    public void delete(Long id) {
        Furnizor existing = getById(id);
        repo.delete(existing);
    }
}