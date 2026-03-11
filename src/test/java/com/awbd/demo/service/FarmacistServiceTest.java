package com.awbd.demo.service;

import com.awbd.demo.entity.Farmacist;
import com.awbd.demo.exception.BadRequestException;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.FarmacistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FarmacistServiceTest {

    @Mock
    private FarmacistRepository repo;

    @InjectMocks
    private FarmacistService service;

    @Test
    void create_shouldSaveFarmacist_whenEmailIsUnique() {
        Farmacist farmacist = new Farmacist();
        farmacist.setNume("Ionescu");
        farmacist.setPrenume("Maria");
        farmacist.setDataAngajarii(LocalDate.of(2024, 10, 1));
        farmacist.setTelefon("0722222222");
        farmacist.setEmail("maria.ionescu@example.com");
        farmacist.setSalariu(new BigDecimal("5500.50"));

        when(repo.existsByEmail("maria.ionescu@example.com")).thenReturn(false);
        when(repo.save(farmacist)).thenReturn(farmacist);

        Farmacist saved = service.create(farmacist);

        assertNotNull(saved);
        assertEquals("maria.ionescu@example.com", saved.getEmail());
        assertEquals("Ionescu", saved.getNume());
        verify(repo).existsByEmail("maria.ionescu@example.com");
        verify(repo).save(farmacist);
    }

    @Test
    void create_shouldPassCorrectFarmacistToSave() {
        Farmacist farmacist = new Farmacist();
        farmacist.setNume("Ionescu");
        farmacist.setPrenume("Maria");
        farmacist.setDataAngajarii(LocalDate.of(2024, 10, 1));
        farmacist.setTelefon("0722222222");
        farmacist.setEmail("maria.ionescu@example.com");
        farmacist.setSalariu(new BigDecimal("5500.50"));

        when(repo.existsByEmail("maria.ionescu@example.com")).thenReturn(false);
        when(repo.save(any(Farmacist.class))).thenReturn(farmacist);

        service.create(farmacist);

        ArgumentCaptor<Farmacist> captor = ArgumentCaptor.forClass(Farmacist.class);
        verify(repo).save(captor.capture());

        Farmacist captured = captor.getValue();
        assertEquals("Ionescu", captured.getNume());
        assertEquals("Maria", captured.getPrenume());
        assertEquals("maria.ionescu@example.com", captured.getEmail());
        assertEquals("0722222222", captured.getTelefon());
        assertEquals(new BigDecimal("5500.50"), captured.getSalariu());
    }

    @Test
    void create_shouldThrowException_whenEmailAlreadyExists() {
        Farmacist farmacist = new Farmacist();
        farmacist.setEmail("maria.ionescu@example.com");

        when(repo.existsByEmail("maria.ionescu@example.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> service.create(farmacist));

        verify(repo).existsByEmail("maria.ionescu@example.com");
        verify(repo, never()).save(any(Farmacist.class));
    }

    @Test
    void getById_shouldReturnFarmacist_whenExists() {
        Farmacist farmacist = new Farmacist();
        farmacist.setNume("Ionescu");
        farmacist.setPrenume("Maria");

        when(repo.findById(1L)).thenReturn(Optional.of(farmacist));

        Farmacist result = service.getById(1L);

        assertNotNull(result);
        assertEquals("Ionescu", result.getNume());
        assertEquals("Maria", result.getPrenume());
        verify(repo).findById(1L);
    }

    @Test
    void getById_shouldThrowException_whenNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));

        verify(repo).findById(99L);
    }
}