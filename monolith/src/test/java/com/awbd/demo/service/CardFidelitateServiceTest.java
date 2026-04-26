package com.awbd.demo.service;

import com.awbd.demo.dto.CardFidelitateRequest;
import com.awbd.demo.entity.CardFidelitate;
import com.awbd.demo.entity.Client;
import com.awbd.demo.exception.BadRequestException;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.CardFidelitateRepository;
import com.awbd.demo.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardFidelitateServiceTest {

    @Mock
    private CardFidelitateRepository cardRepo;

    @Mock
    private ClientRepository clientRepo;

    @InjectMocks
    private CardFidelitateService service;

    @Test
    void create_shouldSaveCard_whenClientExistsAndHasNoCard() {
        CardFidelitateRequest req = new CardFidelitateRequest();
        req.setNivel("mediu");
        req.setPuncte(100);
        req.setClientId(1L);

        Client client = new Client();
        client.setNume("Popescu");

        CardFidelitate savedCard = new CardFidelitate();
        savedCard.setNivel("mediu");
        savedCard.setPuncte(100);
        savedCard.setClient(client);

        when(clientRepo.findById(1L)).thenReturn(Optional.of(client));
        when(cardRepo.existsByClientId(1L)).thenReturn(false);
        when(cardRepo.save(any(CardFidelitate.class))).thenReturn(savedCard);

        CardFidelitate result = service.create(req);

        assertNotNull(result);
        assertEquals("mediu", result.getNivel());
        assertEquals(100, result.getPuncte());
        assertEquals(client, result.getClient());

        verify(clientRepo).findById(1L);
        verify(cardRepo).existsByClientId(1L);
        verify(cardRepo).save(any(CardFidelitate.class));
    }

    @Test
    void create_shouldMapRequestCorrectly() {
        CardFidelitateRequest req = new CardFidelitateRequest();
        req.setNivel("gold");
        req.setPuncte(250);
        req.setClientId(1L);

        Client client = new Client();
        client.setNume("Ionescu");

        CardFidelitate savedCard = new CardFidelitate();
        savedCard.setNivel("gold");
        savedCard.setPuncte(250);
        savedCard.setClient(client);

        when(clientRepo.findById(1L)).thenReturn(Optional.of(client));
        when(cardRepo.existsByClientId(1L)).thenReturn(false);
        when(cardRepo.save(any(CardFidelitate.class))).thenReturn(savedCard);

        service.create(req);

        ArgumentCaptor<CardFidelitate> captor = ArgumentCaptor.forClass(CardFidelitate.class);
        verify(cardRepo).save(captor.capture());

        CardFidelitate toSave = captor.getValue();
        assertEquals("gold", toSave.getNivel());
        assertEquals(250, toSave.getPuncte());
        assertEquals(client, toSave.getClient());
    }

    @Test
    void create_shouldThrowException_whenClientAlreadyHasCard() {
        CardFidelitateRequest req = new CardFidelitateRequest();
        req.setNivel("silver");
        req.setPuncte(50);
        req.setClientId(1L);

        Client client = new Client();

        when(clientRepo.findById(1L)).thenReturn(Optional.of(client));
        when(cardRepo.existsByClientId(1L)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> service.create(req));

        verify(clientRepo).findById(1L);
        verify(cardRepo).existsByClientId(1L);
        verify(cardRepo, never()).save(any(CardFidelitate.class));
    }

    @Test
    void create_shouldThrowException_whenClientNotFound() {
        CardFidelitateRequest req = new CardFidelitateRequest();
        req.setNivel("silver");
        req.setPuncte(50);
        req.setClientId(99L);

        when(clientRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.create(req));

        verify(clientRepo).findById(99L);
        verify(cardRepo, never()).existsByClientId(anyLong());
        verify(cardRepo, never()).save(any(CardFidelitate.class));
    }

    @Test
    void getAll_shouldReturnAllCards() {
        CardFidelitate c1 = new CardFidelitate();
        c1.setNivel("silver");

        CardFidelitate c2 = new CardFidelitate();
        c2.setNivel("gold");

        Pageable pageable = PageRequest.of(0, 10);
        Page<CardFidelitate> page = new PageImpl<>(Arrays.asList(c1, c2), pageable, 2);

        when(cardRepo.findAll(pageable)).thenReturn(page);

        Page<CardFidelitate> result = service.getAll(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals("silver", result.getContent().get(0).getNivel());
        assertEquals("gold", result.getContent().get(1).getNivel());

        verify(cardRepo).findAll(pageable);
    }

    @Test
    void getById_shouldReturnCard_whenExists() {
        CardFidelitate card = new CardFidelitate();
        card.setNivel("mediu");
        card.setPuncte(100);

        when(cardRepo.findById(1L)).thenReturn(Optional.of(card));

        CardFidelitate result = service.getById(1L);

        assertNotNull(result);
        assertEquals("mediu", result.getNivel());
        assertEquals(100, result.getPuncte());
        verify(cardRepo).findById(1L);
    }

    @Test
    void getById_shouldThrowException_whenNotFound() {
        when(cardRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));

        verify(cardRepo).findById(99L);
    }

    @Test
    void update_shouldUpdateNivelAndPuncte_whenRequestContainsThem() {
        CardFidelitateRequest req = new CardFidelitateRequest();
        req.setNivel("gold");
        req.setPuncte(300);

        Client existingClient = mock(Client.class);

        CardFidelitate existing = new CardFidelitate();
        existing.setNivel("silver");
        existing.setPuncte(100);
        existing.setClient(existingClient);

        CardFidelitate saved = new CardFidelitate();
        saved.setNivel("gold");
        saved.setPuncte(300);
        saved.setClient(existingClient);

        when(cardRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(cardRepo.save(existing)).thenReturn(saved);

        CardFidelitate result = service.update(1L, req);

        assertNotNull(result);
        assertEquals("gold", result.getNivel());
        assertEquals(300, result.getPuncte());
        verify(cardRepo).findById(1L);
        verify(cardRepo).save(existing);
    }

    @Test
    void update_shouldChangeClient_whenNewClientExistsAndDoesNotHaveCard() {
        CardFidelitateRequest req = new CardFidelitateRequest();
        req.setClientId(2L);

        Client oldClient = mock(Client.class);
        when(oldClient.getId()).thenReturn(1L);

        Client newClient = mock(Client.class);

        CardFidelitate existing = new CardFidelitate();
        existing.setNivel("silver");
        existing.setPuncte(100);
        existing.setClient(oldClient);

        CardFidelitate saved = new CardFidelitate();
        saved.setNivel("silver");
        saved.setPuncte(100);
        saved.setClient(newClient);

        when(cardRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(clientRepo.findById(2L)).thenReturn(Optional.of(newClient));
        when(cardRepo.existsByClientId(2L)).thenReturn(false);
        when(cardRepo.save(existing)).thenReturn(saved);

        CardFidelitate result = service.update(1L, req);

        assertNotNull(result);
        assertEquals(newClient, result.getClient());

        verify(clientRepo).findById(2L);
        verify(cardRepo).existsByClientId(2L);
        verify(cardRepo).save(existing);
    }

    @Test
    void update_shouldThrowException_whenNewClientNotFound() {
        CardFidelitateRequest req = new CardFidelitateRequest();
        req.setClientId(2L);

        Client oldClient = mock(Client.class);

        CardFidelitate existing = new CardFidelitate();
        existing.setClient(oldClient);

        when(cardRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(clientRepo.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, req));

        verify(clientRepo).findById(2L);
        verify(cardRepo, never()).save(any(CardFidelitate.class));
    }

    @Test
    void update_shouldThrowException_whenNewClientAlreadyHasCard() {
        CardFidelitateRequest req = new CardFidelitateRequest();
        req.setClientId(2L);

        Client oldClient = mock(Client.class);
        when(oldClient.getId()).thenReturn(1L);

        Client newClient = mock(Client.class);

        CardFidelitate existing = new CardFidelitate();
        existing.setClient(oldClient);

        when(cardRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(clientRepo.findById(2L)).thenReturn(Optional.of(newClient));
        when(cardRepo.existsByClientId(2L)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> service.update(1L, req));

        verify(clientRepo).findById(2L);
        verify(cardRepo).existsByClientId(2L);
        verify(cardRepo, never()).save(any(CardFidelitate.class));
    }

    @Test
    void delete_shouldDeleteCard_whenExists() {
        CardFidelitate existing = new CardFidelitate();

        when(cardRepo.findById(1L)).thenReturn(Optional.of(existing));

        service.delete(1L);

        verify(cardRepo).findById(1L);
        verify(cardRepo).delete(existing);
    }

    @Test
    void delete_shouldThrowException_whenCardNotFound() {
        when(cardRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));

        verify(cardRepo).findById(99L);
        verify(cardRepo, never()).delete(any(CardFidelitate.class));
    }
}