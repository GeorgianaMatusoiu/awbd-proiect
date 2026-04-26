package com.awbd.demo.service;

import com.awbd.demo.dto.ProfilClientRequest;
import com.awbd.demo.entity.Client;
import com.awbd.demo.entity.ProfilClient;
import com.awbd.demo.exception.BadRequestException;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.ClientRepository;
import com.awbd.demo.repository.ProfilClientRepository;
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
class ProfilClientServiceTest {

    @Mock
    private ProfilClientRepository profilRepo;

    @Mock
    private ClientRepository clientRepo;

    @InjectMocks
    private ProfilClientService service;

    @Test
    void create_shouldSaveProfil_whenClientExistsAndHasNoProfil() {
        ProfilClientRequest req = new ProfilClientRequest();
        req.setVaccinari("Antigripal");
        req.setAlergie("Penicilina");
        req.setClientId(1L);

        Client client = new Client();

        ProfilClient profil = new ProfilClient();
        profil.setVaccinari("Antigripal");
        profil.setAlergie("Penicilina");
        profil.setClient(client);

        when(clientRepo.findById(1L)).thenReturn(Optional.of(client));
        when(profilRepo.existsByClientId(1L)).thenReturn(false);
        when(profilRepo.save(any(ProfilClient.class))).thenReturn(profil);

        ProfilClient result = service.create(req);

        assertNotNull(result);
        assertEquals("Antigripal", result.getVaccinari());
        assertEquals("Penicilina", result.getAlergie());
        assertEquals(client, result.getClient());

        verify(clientRepo).findById(1L);
        verify(profilRepo).existsByClientId(1L);
        verify(profilRepo).save(any(ProfilClient.class));
    }

    @Test
    void create_shouldMapRequestCorrectly() {
        ProfilClientRequest req = new ProfilClientRequest();
        req.setVaccinari("Hepatita B");
        req.setAlergie("Polen");
        req.setClientId(1L);

        Client client = new Client();

        when(clientRepo.findById(1L)).thenReturn(Optional.of(client));
        when(profilRepo.existsByClientId(1L)).thenReturn(false);
        when(profilRepo.save(any(ProfilClient.class))).thenAnswer(inv -> inv.getArgument(0));

        service.create(req);

        ArgumentCaptor<ProfilClient> captor = ArgumentCaptor.forClass(ProfilClient.class);
        verify(profilRepo).save(captor.capture());

        ProfilClient toSave = captor.getValue();

        assertEquals("Hepatita B", toSave.getVaccinari());
        assertEquals("Polen", toSave.getAlergie());
        assertEquals(client, toSave.getClient());
    }

    @Test
    void create_shouldThrowException_whenClientAlreadyHasProfil() {
        ProfilClientRequest req = new ProfilClientRequest();
        req.setClientId(1L);

        Client client = new Client();

        when(clientRepo.findById(1L)).thenReturn(Optional.of(client));
        when(profilRepo.existsByClientId(1L)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> service.create(req));

        verify(clientRepo).findById(1L);
        verify(profilRepo).existsByClientId(1L);
        verify(profilRepo, never()).save(any(ProfilClient.class));
    }

    @Test
    void create_shouldThrowException_whenClientNotFound() {
        ProfilClientRequest req = new ProfilClientRequest();
        req.setClientId(99L);

        when(clientRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.create(req));

        verify(clientRepo).findById(99L);
        verify(profilRepo, never()).existsByClientId(anyLong());
        verify(profilRepo, never()).save(any(ProfilClient.class));
    }

    @Test
    void getAll_shouldReturnAllProfiles() {
        ProfilClient p1 = new ProfilClient();
        p1.setVaccinari("Antigripal");

        ProfilClient p2 = new ProfilClient();
        p2.setVaccinari("COVID");

        Pageable pageable = PageRequest.of(0, 10);
        Page<ProfilClient> page = new PageImpl<>(Arrays.asList(p1, p2), pageable, 2);

        when(profilRepo.findAll(pageable)).thenReturn(page);

        Page<ProfilClient> result = service.getAll(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals("Antigripal", result.getContent().get(0).getVaccinari());
        assertEquals("COVID", result.getContent().get(1).getVaccinari());

        verify(profilRepo).findAll(pageable);
    }

    @Test
    void getById_shouldReturnProfil_whenExists() {
        ProfilClient profil = new ProfilClient();
        profil.setVaccinari("Antigripal");

        when(profilRepo.findById(1L)).thenReturn(Optional.of(profil));

        ProfilClient result = service.getById(1L);

        assertNotNull(result);
        assertEquals("Antigripal", result.getVaccinari());

        verify(profilRepo).findById(1L);
    }

    @Test
    void getById_shouldThrowException_whenNotFound() {
        when(profilRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));

        verify(profilRepo).findById(99L);
    }

    @Test
    void update_shouldModifyVaccinariAndAlergie() {
        ProfilClient existing = new ProfilClient();
        existing.setVaccinari("Antigripal");
        existing.setAlergie("Praf");

        ProfilClientRequest req = new ProfilClientRequest();
        req.setVaccinari("COVID");
        req.setAlergie("Polen");

        ProfilClient saved = new ProfilClient();
        saved.setVaccinari("COVID");
        saved.setAlergie("Polen");

        when(profilRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(profilRepo.save(existing)).thenReturn(saved);

        ProfilClient result = service.update(1L, req);

        assertNotNull(result);
        assertEquals("COVID", result.getVaccinari());
        assertEquals("Polen", result.getAlergie());

        verify(profilRepo).findById(1L);
        verify(profilRepo).save(existing);
    }

    @Test
    void update_shouldChangeClient_whenNewClientExistsAndHasNoProfil() {
        ProfilClientRequest req = new ProfilClientRequest();
        req.setClientId(2L);

        Client oldClient = mock(Client.class);
        when(oldClient.getId()).thenReturn(1L);

        Client newClient = mock(Client.class);

        ProfilClient existing = new ProfilClient();
        existing.setClient(oldClient);

        ProfilClient saved = new ProfilClient();
        saved.setClient(newClient);

        when(profilRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(clientRepo.findById(2L)).thenReturn(Optional.of(newClient));
        when(profilRepo.existsByClientId(2L)).thenReturn(false);
        when(profilRepo.save(existing)).thenReturn(saved);

        ProfilClient result = service.update(1L, req);

        assertNotNull(result);
        assertEquals(newClient, result.getClient());

        verify(profilRepo).findById(1L);
        verify(clientRepo).findById(2L);
        verify(profilRepo).existsByClientId(2L);
        verify(profilRepo).save(existing);
    }

    @Test
    void update_shouldThrowException_whenNewClientNotFound() {
        ProfilClientRequest req = new ProfilClientRequest();
        req.setClientId(2L);

        ProfilClient existing = new ProfilClient();
        existing.setClient(mock(Client.class));

        when(profilRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(clientRepo.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, req));

        verify(profilRepo).findById(1L);
        verify(clientRepo).findById(2L);
        verify(profilRepo, never()).save(any(ProfilClient.class));
    }

    @Test
    void update_shouldThrowException_whenNewClientAlreadyHasProfil() {
        ProfilClientRequest req = new ProfilClientRequest();
        req.setClientId(2L);

        Client oldClient = mock(Client.class);
        when(oldClient.getId()).thenReturn(1L);

        Client newClient = mock(Client.class);

        ProfilClient existing = new ProfilClient();
        existing.setClient(oldClient);

        when(profilRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(clientRepo.findById(2L)).thenReturn(Optional.of(newClient));
        when(profilRepo.existsByClientId(2L)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> service.update(1L, req));

        verify(profilRepo).findById(1L);
        verify(clientRepo).findById(2L);
        verify(profilRepo).existsByClientId(2L);
        verify(profilRepo, never()).save(any(ProfilClient.class));
    }

    @Test
    void delete_shouldDeleteProfil_whenExists() {
        ProfilClient existing = new ProfilClient();

        when(profilRepo.findById(1L)).thenReturn(Optional.of(existing));

        service.delete(1L);

        verify(profilRepo).findById(1L);
        verify(profilRepo).delete(existing);
    }

    @Test
    void delete_shouldThrowException_whenNotFound() {
        when(profilRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));

        verify(profilRepo).findById(99L);
        verify(profilRepo, never()).delete(any(ProfilClient.class));
    }
}