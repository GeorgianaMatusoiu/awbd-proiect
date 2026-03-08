package com.awbd.demo.service;

import com.awbd.demo.entity.Prospect;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.ProspectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProspectService {

    private final ProspectRepository repo;

    public ProspectService(ProspectRepository repo) {
        this.repo = repo;
    }

    public Prospect create(Prospect prospect) {
        return repo.save(prospect);
    }

    public List<Prospect> getAll() {
        return repo.findAll();
    }

    public Prospect getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prospectul cu id " + id + " nu exista."));
    }

    public Prospect update(Long id, Prospect updated) {
        Prospect existing = getById(id);

        if (updated.getAfectiuni() != null) existing.setAfectiuni(updated.getAfectiuni());
        if (updated.getAdministrare() != null) existing.setAdministrare(updated.getAdministrare());

        return repo.save(existing);
    }

    public void delete(Long id) {
        Prospect existing = getById(id);
        repo.delete(existing);
    }
}