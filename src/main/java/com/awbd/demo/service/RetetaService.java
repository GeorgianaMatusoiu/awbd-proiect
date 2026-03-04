package com.awbd.demo.service;

import com.awbd.demo.dto.RetetaRequest;
import com.awbd.demo.entity.Client;
import com.awbd.demo.entity.Farmacist;
import com.awbd.demo.entity.Reteta;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.ClientRepository;
import com.awbd.demo.repository.FarmacistRepository;
import com.awbd.demo.repository.RetetaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RetetaService {

    private final RetetaRepository retetaRepo;
    private final ClientRepository clientRepo;
    private final FarmacistRepository farmacistRepo;

    public RetetaService(RetetaRepository retetaRepo, ClientRepository clientRepo, FarmacistRepository farmacistRepo) {
        this.retetaRepo = retetaRepo;
        this.clientRepo = clientRepo;
        this.farmacistRepo = farmacistRepo;
    }

    public Reteta create(RetetaRequest req) {
        Client client = clientRepo.findById(req.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Clientul cu id " + req.getClientId() + " nu exista."));

        Farmacist farmacist = farmacistRepo.findById(req.getFarmacistId())
                .orElseThrow(() -> new ResourceNotFoundException("Farmacistul cu id " + req.getFarmacistId() + " nu exista."));

        Reteta r = new Reteta();
        r.setDataTiparire(req.getDataTiparire());
        r.setClient(client);
        r.setFarmacist(farmacist);

        return retetaRepo.save(r);
    }

    public List<Reteta> getAll() {
        return retetaRepo.findAll();
    }

    public Reteta getById(Long id) {
        return retetaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reteta cu id " + id + " nu exista."));
    }

    public Reteta update(Long id, RetetaRequest req) {
        Reteta existing = getById(id);

        if (req.getDataTiparire() != null) {
            existing.setDataTiparire(req.getDataTiparire());
        }

        if (req.getClientId() != null) {
            Client client = clientRepo.findById(req.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Clientul cu id " + req.getClientId() + " nu exista."));
            existing.setClient(client);
        }

        if (req.getFarmacistId() != null) {
            Farmacist farmacist = farmacistRepo.findById(req.getFarmacistId())
                    .orElseThrow(() -> new ResourceNotFoundException("Farmacistul cu id " + req.getFarmacistId() + " nu exista."));
            existing.setFarmacist(farmacist);
        }

        return retetaRepo.save(existing);
    }

    public void delete(Long id) {
        Reteta existing = getById(id);
        retetaRepo.delete(existing);
    }
}