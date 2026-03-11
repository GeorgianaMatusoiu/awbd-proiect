package com.awbd.demo.service;

import com.awbd.demo.entity.Prospect;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.ProspectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProspectServiceTest {

    @Mock
    private ProspectRepository repo;

    @InjectMocks
    private ProspectService service;

    @Test
    void create_shouldSaveProspect() {
        Prospect prospect = new Prospect();
        prospect.setAfectiuni("Durere, febra");
        prospect.setAdministrare("2 comprimate pe zi");

        when(repo.save(prospect)).thenReturn(prospect);

        Prospect saved = service.create(prospect);

        assertNotNull(saved);
        assertEquals("Durere, febra", saved.getAfectiuni());
        assertEquals("2 comprimate pe zi", saved.getAdministrare());
        verify(repo).save(prospect);
    }

    @Test
    void getAll_shouldReturnAllProspects() {
        Prospect p1 = new Prospect();
        p1.setAfectiuni("Durere");

        Prospect p2 = new Prospect();
        p2.setAfectiuni("Febra");

        when(repo.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Prospect> result = service.getAll();

        assertEquals(2, result.size());
        assertEquals("Durere", result.get(0).getAfectiuni());
        assertEquals("Febra", result.get(1).getAfectiuni());
        verify(repo).findAll();
    }

    @Test
    void getById_shouldReturnProspect_whenExists() {
        Prospect prospect = new Prospect();
        prospect.setAfectiuni("Durere");
        prospect.setAdministrare("1 pe zi");

        when(repo.findById(1L)).thenReturn(Optional.of(prospect));

        Prospect result = service.getById(1L);

        assertNotNull(result);
        assertEquals("Durere", result.getAfectiuni());
        assertEquals("1 pe zi", result.getAdministrare());
        verify(repo).findById(1L);
    }

    @Test
    void getById_shouldThrowException_whenNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));

        verify(repo).findById(99L);
    }

    @Test
    void update_shouldModifyFields_whenProspectExists() {
        Prospect existing = new Prospect();
        existing.setAfectiuni("Durere");
        existing.setAdministrare("1 pe zi");

        Prospect updated = new Prospect();
        updated.setAfectiuni("Durere, febra");
        updated.setAdministrare("2 pe zi");

        Prospect saved = new Prospect();
        saved.setAfectiuni("Durere, febra");
        saved.setAdministrare("2 pe zi");

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(saved);

        Prospect result = service.update(1L, updated);

        assertNotNull(result);
        assertEquals("Durere, febra", result.getAfectiuni());
        assertEquals("2 pe zi", result.getAdministrare());

        verify(repo).findById(1L);
        verify(repo).save(existing);
    }

    @Test
    void update_shouldThrowException_whenProspectNotFound() {
        Prospect updated = new Prospect();
        updated.setAfectiuni("Nou");

        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(99L, updated));

        verify(repo).findById(99L);
        verify(repo, never()).save(any(Prospect.class));
    }

    @Test
    void delete_shouldDeleteProspect_whenExists() {
        Prospect existing = new Prospect();

        when(repo.findById(1L)).thenReturn(Optional.of(existing));

        service.delete(1L);

        verify(repo).findById(1L);
        verify(repo).delete(existing);
    }

    @Test
    void delete_shouldThrowException_whenNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));

        verify(repo).findById(99L);
        verify(repo, never()).delete(any(Prospect.class));
    }
}