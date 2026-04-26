package com.awbd.demo.service;

import com.awbd.demo.entity.Furnizor;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.FurnizorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FurnizorServiceTest {

    @Mock
    private FurnizorRepository repo;

    @InjectMocks
    private FurnizorService service;

    @Test
    void create_shouldSaveFurnizor() {
        Furnizor furnizor = new Furnizor();
        furnizor.setNume("FarmSupplier");
        furnizor.setAdresa("Str. Lalelelor");
        furnizor.setOras("Bucuresti");
        furnizor.setTara("Romania");
        furnizor.setTelefon("0711111111");

        when(repo.save(furnizor)).thenReturn(furnizor);

        Furnizor saved = service.create(furnizor);

        assertNotNull(saved);
        assertEquals("FarmSupplier", saved.getNume());
        assertEquals("Bucuresti", saved.getOras());
        verify(repo).save(furnizor);
    }

    @Test
    void create_shouldPassCorrectDataToSave() {
        Furnizor furnizor = new Furnizor();
        furnizor.setNume("FarmSupplier");
        furnizor.setAdresa("Str. Lalelelor");
        furnizor.setOras("Bucuresti");
        furnizor.setTara("Romania");
        furnizor.setTelefon("0711111111");

        when(repo.save(any(Furnizor.class))).thenReturn(furnizor);

        service.create(furnizor);

        ArgumentCaptor<Furnizor> captor = ArgumentCaptor.forClass(Furnizor.class);
        verify(repo).save(captor.capture());

        Furnizor captured = captor.getValue();
        assertEquals("FarmSupplier", captured.getNume());
        assertEquals("Str. Lalelelor", captured.getAdresa());
        assertEquals("Bucuresti", captured.getOras());
        assertEquals("Romania", captured.getTara());
        assertEquals("0711111111", captured.getTelefon());
    }

    @Test
    void getById_shouldReturnFurnizor_whenExists() {
        Furnizor furnizor = new Furnizor();
        furnizor.setNume("FarmSupplier");

        when(repo.findById(1L)).thenReturn(Optional.of(furnizor));

        Furnizor result = service.getById(1L);

        assertNotNull(result);
        assertEquals("FarmSupplier", result.getNume());
        verify(repo).findById(1L);
    }

    @Test
    void getById_shouldThrowException_whenNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));

        verify(repo).findById(99L);
    }
}