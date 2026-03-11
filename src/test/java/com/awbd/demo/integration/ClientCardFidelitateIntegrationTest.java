package com.awbd.demo.integration;

import com.awbd.demo.dto.CardFidelitateRequest;
import com.awbd.demo.entity.CardFidelitate;
import com.awbd.demo.entity.Client;
import com.awbd.demo.service.CardFidelitateService;
import com.awbd.demo.service.ClientService;
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
class ClientCardFidelitateIntegrationTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private CardFidelitateService cardFidelitateService;

    @Test
    void createClient_thenCreateCard_thenFetchCardById() {
        Client client = new Client();
        client.setCnp("1234567890123");
        client.setNume("Popescu");
        client.setPrenume("Ana");
        client.setVarsta(25);
        client.setTelefon("0711111111");

        Client savedClient = clientService.create(client);

        assertNotNull(savedClient);
        assertNotNull(savedClient.getId());

        CardFidelitateRequest request = new CardFidelitateRequest();
        request.setNivel("gold");
        request.setPuncte(200);
        request.setClientId(savedClient.getId());

        CardFidelitate savedCard = cardFidelitateService.create(request);

        assertNotNull(savedCard);
        assertNotNull(savedCard.getId());
        assertEquals("gold", savedCard.getNivel());
        assertEquals(200, savedCard.getPuncte());
        assertEquals(savedClient.getId(), savedCard.getClient().getId());

        CardFidelitate fetched = cardFidelitateService.getById(savedCard.getId());

        assertNotNull(fetched);
        assertEquals("gold", fetched.getNivel());
        assertEquals(200, fetched.getPuncte());
        assertEquals(savedClient.getId(), fetched.getClient().getId());

        List<CardFidelitate> allCards = cardFidelitateService.getAll();
        assertEquals(1, allCards.size());
    }
}