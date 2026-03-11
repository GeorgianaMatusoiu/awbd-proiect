package com.awbd.demo.integration;

import com.awbd.demo.dto.ProfilClientRequest;
import com.awbd.demo.entity.Client;
import com.awbd.demo.entity.ProfilClient;
import com.awbd.demo.service.ClientService;
import com.awbd.demo.service.ProfilClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ClientProfilClientIntegrationTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProfilClientService profilClientService;

    @Test
    void createClient_thenCreateProfil_thenFetchProfilById() {
        Client client = new Client();
        client.setCnp("9876543210123");
        client.setNume("Ionescu");
        client.setPrenume("Maria");
        client.setVarsta(30);
        client.setTelefon("0722222222");

        Client savedClient = clientService.create(client);

        assertNotNull(savedClient);
        assertNotNull(savedClient.getId());

        ProfilClientRequest request = new ProfilClientRequest();
        request.setVaccinari("Antigripal");
        request.setAlergie("Polen");
        request.setClientId(savedClient.getId());

        ProfilClient savedProfil = profilClientService.create(request);

        assertNotNull(savedProfil);
        assertNotNull(savedProfil.getId());
        assertEquals("Antigripal", savedProfil.getVaccinari());
        assertEquals("Polen", savedProfil.getAlergie());
        assertEquals(savedClient.getId(), savedProfil.getClient().getId());

        ProfilClient fetched = profilClientService.getById(savedProfil.getId());

        assertNotNull(fetched);
        assertEquals("Antigripal", fetched.getVaccinari());
        assertEquals("Polen", fetched.getAlergie());
        assertEquals(savedClient.getId(), fetched.getClient().getId());

        List<ProfilClient> allProfiles = profilClientService.getAll();
        assertEquals(1, allProfiles.size());
    }
}