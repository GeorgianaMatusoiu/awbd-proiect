package com.awbd.demo.service;

import com.awbd.demo.entity.Client;
import com.awbd.demo.exception.BadRequestException;
import com.awbd.demo.exception.ResourceNotFoundException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository repo;

    @InjectMocks
    private ClientService service;

    @Test
    void create_shouldSaveClient_whenCnpIsUnique() {
        Client client = new Client();
        client.setCnp("1234567890123");
        client.setNume("Popescu");
        client.setPrenume("Ana");
        client.setVarsta(22);
        client.setTelefon("0712345678");

        when(repo.existsByCnp("1234567890123")).thenReturn(false);
        when(repo.save(client)).thenReturn(client);

        Client saved = service.create(client);

        assertNotNull(saved);
        assertEquals("1234567890123", saved.getCnp());
        assertEquals("Popescu", saved.getNume());
        assertEquals("Ana", saved.getPrenume());

        verify(repo).existsByCnp("1234567890123");
        verify(repo).save(client);
    }

    @Test
    void create_shouldThrowException_whenCnpIsNull() {
        Client client = new Client();
        client.setCnp(null);

        assertThrows(BadRequestException.class, () -> service.create(client));

        verify(repo, never()).existsByCnp(anyString());
        verify(repo, never()).save(any(Client.class));
    }

    @Test
    void create_shouldThrowException_whenCnpIsBlank() {
        Client client = new Client();
        client.setCnp("   ");

        assertThrows(BadRequestException.class, () -> service.create(client));

        verify(repo, never()).existsByCnp(anyString());
        verify(repo, never()).save(any(Client.class));
    }

    @Test
    void create_shouldThrowException_whenCnpAlreadyExists() {
        Client client = new Client();
        client.setCnp("1234567890123");

        when(repo.existsByCnp("1234567890123")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> service.create(client));

        verify(repo).existsByCnp("1234567890123");
        verify(repo, never()).save(any(Client.class));
    }

    @Test
    void create_shouldCallSaveWithExpectedClient() {
        Client client = new Client();
        client.setCnp("1234567890123");
        client.setNume("Popescu");
        client.setPrenume("Ana");
        client.setVarsta(22);
        client.setTelefon("0712345678");

        when(repo.existsByCnp("1234567890123")).thenReturn(false);
        when(repo.save(any(Client.class))).thenReturn(client);

        service.create(client);

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(repo).save(captor.capture());

        Client captured = captor.getValue();
        assertEquals("1234567890123", captured.getCnp());
        assertEquals("Popescu", captured.getNume());
        assertEquals("Ana", captured.getPrenume());
        assertEquals(22, captured.getVarsta());
        assertEquals("0712345678", captured.getTelefon());
    }

    @Test
    void getAll_shouldReturnAllClients() {
        Client c1 = new Client();
        c1.setNume("Ana");

        Client c2 = new Client();
        c2.setNume("Ion");

        Pageable pageable = PageRequest.of(0, 10);
        Page<Client> page = new PageImpl<>(Arrays.asList(c1, c2), pageable, 2);

        when(repo.findAll(pageable)).thenReturn(page);

        Page<Client> result = service.getAll(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals("Ana", result.getContent().get(0).getNume());
        assertEquals("Ion", result.getContent().get(1).getNume());

        verify(repo).findAll(pageable);
    }

    @Test
    void getById_shouldReturnClient_whenExists() {
        Client client = new Client();
        client.setCnp("1234567890123");
        client.setNume("Popescu");
        client.setPrenume("Ana");

        when(repo.findById(1L)).thenReturn(Optional.of(client));

        Client result = service.getById(1L);

        assertNotNull(result);
        assertEquals("Popescu", result.getNume());
        assertEquals("Ana", result.getPrenume());
        assertEquals("1234567890123", result.getCnp());

        verify(repo).findById(1L);
    }

    @Test
    void getById_shouldThrowException_whenNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));

        verify(repo).findById(99L);
    }

    @Test
    void update_shouldUpdateFields_whenValidDataProvided() {
        Client existing = new Client();
        existing.setCnp("1234567890123");
        existing.setNume("Popescu");
        existing.setPrenume("Ana");
        existing.setVarsta(22);
        existing.setTelefon("0711111111");

        Client updated = new Client();
        updated.setNume("Ionescu");
        updated.setPrenume("Maria");
        updated.setVarsta(30);
        updated.setTelefon("0722222222");

        Client saved = new Client();
        saved.setCnp("1234567890123");
        saved.setNume("Ionescu");
        saved.setPrenume("Maria");
        saved.setVarsta(30);
        saved.setTelefon("0722222222");

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(saved);

        Client result = service.update(1L, updated);

        assertNotNull(result);
        assertEquals("Ionescu", result.getNume());
        assertEquals("Maria", result.getPrenume());
        assertEquals(30, result.getVarsta());
        assertEquals("0722222222", result.getTelefon());

        verify(repo).findById(1L);
        verify(repo).save(existing);
    }

    @Test
    void update_shouldUpdateCnp_whenNewUniqueCnpProvided() {
        Client existing = new Client();
        existing.setCnp("1234567890123");
        existing.setNume("Popescu");

        Client updated = new Client();
        updated.setCnp("9999999999999");

        Client saved = new Client();
        saved.setCnp("9999999999999");
        saved.setNume("Popescu");

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.existsByCnp("9999999999999")).thenReturn(false);
        when(repo.save(existing)).thenReturn(saved);

        Client result = service.update(1L, updated);

        assertNotNull(result);
        assertEquals("9999999999999", result.getCnp());

        verify(repo).findById(1L);
        verify(repo).existsByCnp("9999999999999");
        verify(repo).save(existing);
    }

    @Test
    void update_shouldNotCheckDuplicate_whenSameCnpProvided() {
        Client existing = new Client();
        existing.setCnp("1234567890123");

        Client updated = new Client();
        updated.setCnp("1234567890123");

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        Client result = service.update(1L, updated);

        assertNotNull(result);
        assertEquals("1234567890123", result.getCnp());

        verify(repo).findById(1L);
        verify(repo, never()).existsByCnp(anyString());
        verify(repo).save(existing);
    }

    @Test
    void update_shouldIgnoreBlankCnp() {
        Client existing = new Client();
        existing.setCnp("1234567890123");
        existing.setNume("Popescu");

        Client updated = new Client();
        updated.setCnp("   ");
        updated.setNume("Ionescu");

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        Client result = service.update(1L, updated);

        assertNotNull(result);
        assertEquals("1234567890123", result.getCnp());
        assertEquals("Ionescu", result.getNume());

        verify(repo).findById(1L);
        verify(repo, never()).existsByCnp(anyString());
        verify(repo).save(existing);
    }

    @Test
    void update_shouldThrowException_whenNewCnpAlreadyExists() {
        Client existing = new Client();
        existing.setCnp("1234567890123");

        Client updated = new Client();
        updated.setCnp("9999999999999");

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.existsByCnp("9999999999999")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> service.update(1L, updated));

        verify(repo).findById(1L);
        verify(repo).existsByCnp("9999999999999");
        verify(repo, never()).save(any(Client.class));
    }

    @Test
    void update_shouldThrowException_whenClientNotFound() {
        Client updated = new Client();
        updated.setNume("Test");

        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(99L, updated));

        verify(repo).findById(99L);
        verify(repo, never()).save(any(Client.class));
    }

    @Test
    void delete_shouldDeleteClient_whenExists() {
        Client existing = new Client();

        when(repo.findById(1L)).thenReturn(Optional.of(existing));

        service.delete(1L);

        verify(repo).findById(1L);
        verify(repo).delete(existing);
    }

    @Test
    void delete_shouldThrowException_whenClientNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));

        verify(repo).findById(99L);
        verify(repo, never()).delete(any(Client.class));
    }
}