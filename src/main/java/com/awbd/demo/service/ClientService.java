package com.awbd.demo.service;

import com.awbd.demo.entity.Client;
import com.awbd.demo.exception.BadRequestException;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository repo;

    public ClientService(ClientRepository repo) {
        this.repo = repo;
    }

    public Client create(Client client) {
        if (client.getCnp() == null || client.getCnp().isBlank()) {
            throw new BadRequestException("CNP este obligatoriu.");
        }
        if (repo.existsByCnp(client.getCnp())) {
            throw new BadRequestException("Exista deja un client cu acest CNP.");
        }
        return repo.save(client);
    }

    public List<Client> getAll() {
        return repo.findAll();
    }

    public Client getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clientul cu id " + id + " nu exista."));
    }

    public Client update(Long id, Client updated) {
        Client existing = getById(id);

        if (updated.getCnp() != null && !updated.getCnp().isBlank()) {
            // dacă vrei să permiți schimbarea CNP-ului, verifică duplicate
            if (!updated.getCnp().equals(existing.getCnp()) && repo.existsByCnp(updated.getCnp())) {
                throw new BadRequestException("Exista deja un client cu acest CNP.");
            }
            existing.setCnp(updated.getCnp());
        }

        if (updated.getNume() != null) existing.setNume(updated.getNume());
        if (updated.getPrenume() != null) existing.setPrenume(updated.getPrenume());
        if (updated.getVarsta() != null) existing.setVarsta(updated.getVarsta());
        if (updated.getTelefon() != null) existing.setTelefon(updated.getTelefon());

        return repo.save(existing);
    }

    public void delete(Long id) {
        Client existing = getById(id); // dacă nu există -> 404
        repo.delete(existing);
    }
}