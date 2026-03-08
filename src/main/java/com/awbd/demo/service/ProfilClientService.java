package com.awbd.demo.service;

import com.awbd.demo.dto.ProfilClientRequest;
import com.awbd.demo.entity.Client;
import com.awbd.demo.entity.ProfilClient;
import com.awbd.demo.exception.BadRequestException;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.ClientRepository;
import com.awbd.demo.repository.ProfilClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfilClientService {

    private final ProfilClientRepository profilRepo;
    private final ClientRepository clientRepo;

    public ProfilClientService(ProfilClientRepository profilRepo, ClientRepository clientRepo) {
        this.profilRepo = profilRepo;
        this.clientRepo = clientRepo;
    }

    public ProfilClient create(ProfilClientRequest req) {
        Client client = clientRepo.findById(req.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Clientul cu id " + req.getClientId() + " nu exista."));

        if (profilRepo.existsByClientId(req.getClientId())) {
            throw new BadRequestException("Clientul are deja un profil.");
        }

        ProfilClient profil = new ProfilClient();
        profil.setVaccinari(req.getVaccinari());
        profil.setAlergie(req.getAlergie());
        profil.setClient(client);

        return profilRepo.save(profil);
    }

    public List<ProfilClient> getAll() {
        return profilRepo.findAll();
    }

    public ProfilClient getById(Long id) {
        return profilRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profilul cu id " + id + " nu exista."));
    }

    public ProfilClient update(Long id, ProfilClientRequest req) {
        ProfilClient existing = getById(id);

        if (req.getVaccinari() != null) {
            existing.setVaccinari(req.getVaccinari());
        }

        if (req.getAlergie() != null) {
            existing.setAlergie(req.getAlergie());
        }

        if (req.getClientId() != null) {
            Client client = clientRepo.findById(req.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Clientul cu id " + req.getClientId() + " nu exista."));

            if (!existing.getClient().getId().equals(req.getClientId()) && profilRepo.existsByClientId(req.getClientId())) {
                throw new BadRequestException("Clientul are deja un profil.");
            }

            existing.setClient(client);
        }

        return profilRepo.save(existing);
    }

    public void delete(Long id) {
        ProfilClient existing = getById(id);
        profilRepo.delete(existing);
    }
}