package com.awbd.demo.service;

import com.awbd.demo.dto.ProfilClientRequest;
import com.awbd.demo.entity.Client;
import com.awbd.demo.entity.ProfilClient;
import com.awbd.demo.exception.BadRequestException;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.ClientRepository;
import com.awbd.demo.repository.ProfilClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProfilClientService {

    private static final Logger log = LoggerFactory.getLogger(ProfilClientService.class);

    private final ProfilClientRepository profilRepo;
    private final ClientRepository clientRepo;

    public ProfilClientService(ProfilClientRepository profilRepo, ClientRepository clientRepo) {
        this.profilRepo = profilRepo;
        this.clientRepo = clientRepo;
    }

    public ProfilClient create(ProfilClientRequest req) {
        log.info("Se incearca crearea unui profil de client nou");
        log.debug("Date primite pentru creare profil: vaccinari={}, alergie={}, clientId={}",
                req.getVaccinari(), req.getAlergie(), req.getClientId());

        Client client = clientRepo.findById(req.getClientId())
                .orElseThrow(() -> {
                    log.error("Crearea profilului a esuat: clientul cu id={} nu a fost gasit", req.getClientId());
                    return new ResourceNotFoundException("Clientul cu id " + req.getClientId() + " nu exista.");
                });

        if (profilRepo.existsByClientId(req.getClientId())) {
            log.error("Crearea profilului a esuat: clientul cu id={} are deja profil", req.getClientId());
            throw new BadRequestException("Clientul are deja un profil.");
        }

        ProfilClient profil = new ProfilClient();
        profil.setVaccinari(req.getVaccinari());
        profil.setAlergie(req.getAlergie());
        profil.setClient(client);

        ProfilClient saved = profilRepo.save(profil);
        log.info("Profilul clientului a fost creat cu succes, id={}", saved.getId());
        return saved;
    }

    public Page<ProfilClient> getAll(Pageable pageable) {
        log.info("Se solicita lista paginata a profilurilor de clienti");
        log.debug("Page request: page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<ProfilClient> profiluriPage = profilRepo.findAll(pageable);

        log.debug("Au fost gasite {} profiluri pe pagina curenta din total {}",
                profiluriPage.getNumberOfElements(), profiluriPage.getTotalElements());

        return profiluriPage;
    }

    public ProfilClient getById(Long id) {
        log.info("Se cauta profilul cu id={}", id);

        return profilRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("Profilul cu id={} nu a fost gasit", id);
                    return new ResourceNotFoundException("Profilul cu id " + id + " nu exista.");
                });
    }

    public ProfilClient update(Long id, ProfilClientRequest req) {
        log.info("Se incearca actualizarea profilului cu id={}", id);
        log.debug("Date primite pentru update profil: vaccinari={}, alergie={}, clientId={}",
                req.getVaccinari(), req.getAlergie(), req.getClientId());

        ProfilClient existing = getById(id);

        if (req.getVaccinari() != null) {
            existing.setVaccinari(req.getVaccinari());
        }

        if (req.getAlergie() != null) {
            existing.setAlergie(req.getAlergie());
        }

        if (req.getClientId() != null) {
            Client client = clientRepo.findById(req.getClientId())
                    .orElseThrow(() -> {
                        log.error("Actualizarea profilului a esuat: clientul cu id={} nu a fost gasit", req.getClientId());
                        return new ResourceNotFoundException("Clientul cu id " + req.getClientId() + " nu exista.");
                    });

            if (!existing.getClient().getId().equals(req.getClientId()) && profilRepo.existsByClientId(req.getClientId())) {
                log.error("Actualizarea profilului a esuat: clientul cu id={} are deja profil", req.getClientId());
                throw new BadRequestException("Clientul are deja un profil.");
            }

            existing.setClient(client);
        }

        ProfilClient saved = profilRepo.save(existing);
        log.info("Profilul cu id={} a fost actualizat cu succes", saved.getId());
        return saved;
    }

    public void delete(Long id) {
        log.info("Se incearca stergerea profilului cu id={}", id);

        ProfilClient existing = getById(id);
        profilRepo.delete(existing);

        log.info("Profilul cu id={} a fost sters cu succes", id);
    }
}