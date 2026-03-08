package com.awbd.demo.service;

import com.awbd.demo.entity.CategorieMedicament;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.CategorieMedicamentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieMedicamentService {

    private final CategorieMedicamentRepository repo;

    public CategorieMedicamentService(CategorieMedicamentRepository repo) {
        this.repo = repo;
    }

    public CategorieMedicament create(CategorieMedicament categorie) {
        return repo.save(categorie);
    }

    public List<CategorieMedicament> getAll() {
        return repo.findAll();
    }

    public CategorieMedicament getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria cu id " + id + " nu exista."));
    }

    public CategorieMedicament update(Long id, CategorieMedicament updated) {
        CategorieMedicament existing = getById(id);

        if (updated.getStoc() != null) existing.setStoc(updated.getStoc());
        if (updated.getTemperatura() != null) existing.setTemperatura(updated.getTemperatura());

        return repo.save(existing);
    }

    public void delete(Long id) {
        CategorieMedicament existing = getById(id);
        repo.delete(existing);
    }
}