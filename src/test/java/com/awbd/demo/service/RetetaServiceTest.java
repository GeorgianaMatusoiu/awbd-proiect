package com.awbd.demo.service;

import com.awbd.demo.dto.RetetaRequest;
import com.awbd.demo.entity.Client;
import com.awbd.demo.entity.Farmacist;
import com.awbd.demo.entity.Reteta;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.ClientRepository;
import com.awbd.demo.repository.FarmacistRepository;
import com.awbd.demo.repository.RetetaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RetetaServiceTest {

    @Mock
    private RetetaRepository retetaRepo;

    @Mock
    private ClientRepository clientRepo;

    @Mock
    private FarmacistRepository farmacistRepo;

    @InjectMocks
    private RetetaService service;

    @Test
    void create_shouldSaveReteta_whenClientAndFarmacistExist() {
        RetetaRequest req = new RetetaRequest();
        req.setDataTiparire(LocalDate.of(2026, 3, 8));
        req.setClientId(1L);
        req.setFarmacistId(2L);

        Client client = new Client();
        client.setNume("Popescu");

        Farmacist farmacist = new Farmacist();
        farmacist.setNume("Ionescu");

        Reteta savedReteta = new Reteta();
        savedReteta.setDataTiparire(req.getDataTiparire());
        savedReteta.setClient(client);
        savedReteta.setFarmacist(farmacist);

        when(clientRepo.findById(1L)).thenReturn(Optional.of(client));
        when(farmacistRepo.findById(2L)).thenReturn(Optional.of(farmacist));
        when(retetaRepo.save(any(Reteta.class))).thenReturn(savedReteta);

        Reteta result = service.create(req);

        assertNotNull(result);
        assertEquals(LocalDate.of(2026, 3, 8), result.getDataTiparire());
        assertEquals("Popescu", result.getClient().getNume());
        assertEquals("Ionescu", result.getFarmacist().getNume());

        verify(clientRepo).findById(1L);
        verify(farmacistRepo).findById(2L);
        verify(retetaRepo).save(any(Reteta.class));
    }

    @Test
    void create_shouldMapRequestCorrectly() {
        RetetaRequest req = new RetetaRequest();
        req.setDataTiparire(LocalDate.of(2026, 4, 1));
        req.setClientId(1L);
        req.setFarmacistId(2L);

        Client client = new Client();
        Farmacist farmacist = new Farmacist();

        when(clientRepo.findById(1L)).thenReturn(Optional.of(client));
        when(farmacistRepo.findById(2L)).thenReturn(Optional.of(farmacist));
        when(retetaRepo.save(any(Reteta.class))).thenAnswer(inv -> inv.getArgument(0));

        service.create(req);

        ArgumentCaptor<Reteta> captor = ArgumentCaptor.forClass(Reteta.class);
        verify(retetaRepo).save(captor.capture());

        Reteta toSave = captor.getValue();
        assertEquals(LocalDate.of(2026, 4, 1), toSave.getDataTiparire());
        assertEquals(client, toSave.getClient());
        assertEquals(farmacist, toSave.getFarmacist());
    }

    @Test
    void create_shouldThrowException_whenClientDoesNotExist() {
        RetetaRequest req = new RetetaRequest();
        req.setDataTiparire(LocalDate.of(2026, 3, 8));
        req.setClientId(1L);
        req.setFarmacistId(2L);

        when(clientRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.create(req));

        verify(clientRepo).findById(1L);
        verify(farmacistRepo, never()).findById(anyLong());
        verify(retetaRepo, never()).save(any(Reteta.class));
    }

    @Test
    void create_shouldThrowException_whenFarmacistDoesNotExist() {
        RetetaRequest req = new RetetaRequest();
        req.setDataTiparire(LocalDate.of(2026, 3, 8));
        req.setClientId(1L);
        req.setFarmacistId(2L);

        Client client = new Client();

        when(clientRepo.findById(1L)).thenReturn(Optional.of(client));
        when(farmacistRepo.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.create(req));

        verify(clientRepo).findById(1L);
        verify(farmacistRepo).findById(2L);
        verify(retetaRepo, never()).save(any(Reteta.class));
    }

    @Test
    void getAll_shouldReturnAllRetete() {
        Reteta r1 = new Reteta();
        r1.setDataTiparire(LocalDate.of(2026, 3, 8));

        Reteta r2 = new Reteta();
        r2.setDataTiparire(LocalDate.of(2026, 3, 9));

        when(retetaRepo.findAll()).thenReturn(Arrays.asList(r1, r2));

        List<Reteta> result = service.getAll();

        assertEquals(2, result.size());
        assertEquals(LocalDate.of(2026, 3, 8), result.get(0).getDataTiparire());
        assertEquals(LocalDate.of(2026, 3, 9), result.get(1).getDataTiparire());

        verify(retetaRepo).findAll();
    }

    @Test
    void getById_shouldReturnReteta_whenExists() {
        Reteta reteta = new Reteta();
        reteta.setDataTiparire(LocalDate.of(2026, 3, 8));

        when(retetaRepo.findById(1L)).thenReturn(Optional.of(reteta));

        Reteta result = service.getById(1L);

        assertNotNull(result);
        assertEquals(LocalDate.of(2026, 3, 8), result.getDataTiparire());

        verify(retetaRepo).findById(1L);
    }

    @Test
    void getById_shouldThrowException_whenRetetaDoesNotExist() {
        when(retetaRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));

        verify(retetaRepo).findById(99L);
    }

    @Test
    void update_shouldModifyDateClientAndFarmacist_whenAllExist() {
        Reteta existing = new Reteta();
        existing.setDataTiparire(LocalDate.of(2026, 3, 8));

        Client newClient = new Client();
        Farmacist newFarmacist = new Farmacist();

        RetetaRequest req = new RetetaRequest();
        req.setDataTiparire(LocalDate.of(2026, 5, 10));
        req.setClientId(10L);
        req.setFarmacistId(20L);

        Reteta saved = new Reteta();
        saved.setDataTiparire(LocalDate.of(2026, 5, 10));
        saved.setClient(newClient);
        saved.setFarmacist(newFarmacist);

        when(retetaRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(clientRepo.findById(10L)).thenReturn(Optional.of(newClient));
        when(farmacistRepo.findById(20L)).thenReturn(Optional.of(newFarmacist));
        when(retetaRepo.save(existing)).thenReturn(saved);

        Reteta result = service.update(1L, req);

        assertNotNull(result);
        assertEquals(LocalDate.of(2026, 5, 10), result.getDataTiparire());
        assertEquals(newClient, result.getClient());
        assertEquals(newFarmacist, result.getFarmacist());

        verify(retetaRepo).findById(1L);
        verify(clientRepo).findById(10L);
        verify(farmacistRepo).findById(20L);
        verify(retetaRepo).save(existing);
    }

    @Test
    void update_shouldThrowException_whenRetetaNotFound() {
        RetetaRequest req = new RetetaRequest();
        req.setDataTiparire(LocalDate.of(2026, 5, 10));

        when(retetaRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(99L, req));

        verify(retetaRepo).findById(99L);
        verify(retetaRepo, never()).save(any(Reteta.class));
    }

    @Test
    void update_shouldThrowException_whenUpdatedClientNotFound() {
        Reteta existing = new Reteta();

        RetetaRequest req = new RetetaRequest();
        req.setClientId(10L);

        when(retetaRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(clientRepo.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, req));

        verify(clientRepo).findById(10L);
        verify(retetaRepo, never()).save(any(Reteta.class));
    }

    @Test
    void update_shouldThrowException_whenUpdatedFarmacistNotFound() {
        Reteta existing = new Reteta();
        Client client = new Client();

        RetetaRequest req = new RetetaRequest();
        req.setClientId(10L);
        req.setFarmacistId(20L);

        when(retetaRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(clientRepo.findById(10L)).thenReturn(Optional.of(client));
        when(farmacistRepo.findById(20L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(1L, req));

        verify(clientRepo).findById(10L);
        verify(farmacistRepo).findById(20L);
        verify(retetaRepo, never()).save(any(Reteta.class));
    }

    @Test
    void delete_shouldDeleteReteta_whenExists() {
        Reteta existing = new Reteta();

        when(retetaRepo.findById(1L)).thenReturn(Optional.of(existing));

        service.delete(1L);

        verify(retetaRepo).findById(1L);
        verify(retetaRepo).delete(existing);
    }

    @Test
    void delete_shouldThrowException_whenRetetaNotFound() {
        when(retetaRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));

        verify(retetaRepo).findById(99L);
        verify(retetaRepo, never()).delete(any(Reteta.class));
    }
}