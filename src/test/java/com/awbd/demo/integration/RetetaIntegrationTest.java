package com.awbd.demo.integration;

import com.awbd.demo.dto.RetetaRequest;
import com.awbd.demo.entity.Client;
import com.awbd.demo.entity.Farmacist;
import com.awbd.demo.entity.Reteta;
import com.awbd.demo.service.ClientService;
import com.awbd.demo.service.FarmacistService;
import com.awbd.demo.service.RetetaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RetetaIntegrationTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private FarmacistService farmacistService;

    @Autowired
    private RetetaService retetaService;

    @Test
    void createClientAndFarmacist_thenCreateReteta_thenFetchRetetaById() {
        Client client = new Client();
        client.setCnp("5555555555555");
        client.setNume("Georgescu");
        client.setPrenume("Dan");
        client.setVarsta(41);
        client.setTelefon("0733333333");

        Client savedClient = clientService.create(client);

        assertNotNull(savedClient);
        assertNotNull(savedClient.getId());

        Farmacist farmacist = new Farmacist();
        farmacist.setNume("Pop");
        farmacist.setPrenume("Elena");
        farmacist.setDataAngajarii(LocalDate.of(2024, 1, 10));
        farmacist.setTelefon("0744444444");
        farmacist.setEmail("elena.pop@test.com");
        farmacist.setSalariu(new BigDecimal("5000.00"));

        Farmacist savedFarmacist = farmacistService.create(farmacist);

        assertNotNull(savedFarmacist);
        assertNotNull(savedFarmacist.getId());

        RetetaRequest request = new RetetaRequest();
        request.setDataTiparire(LocalDate.of(2026, 3, 8));
        request.setClientId(savedClient.getId());
        request.setFarmacistId(savedFarmacist.getId());

        Reteta savedReteta = retetaService.create(request);

        assertNotNull(savedReteta);
        assertNotNull(savedReteta.getId());
        assertEquals(LocalDate.of(2026, 3, 8), savedReteta.getDataTiparire());
        assertEquals(savedClient.getId(), savedReteta.getClient().getId());
        assertEquals(savedFarmacist.getId(), savedReteta.getFarmacist().getId());

        Reteta fetched = retetaService.getById(savedReteta.getId());

        assertNotNull(fetched);
        assertEquals(LocalDate.of(2026, 3, 8), fetched.getDataTiparire());
        assertEquals(savedClient.getId(), fetched.getClient().getId());
        assertEquals(savedFarmacist.getId(), fetched.getFarmacist().getId());

        List<Reteta> allRetete = retetaService.getAll();
        assertEquals(1, allRetete.size());
    }
}