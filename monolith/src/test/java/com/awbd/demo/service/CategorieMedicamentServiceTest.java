package com.awbd.demo.service;

import com.awbd.demo.entity.CategorieMedicament;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.CategorieMedicamentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class CategorieMedicamentServiceTest {

    @Mock
    private CategorieMedicamentRepository repo;

    @InjectMocks
    private CategorieMedicamentService service;

    @Test
    void create_shouldSaveCategorie() {
        CategorieMedicament categorie = new CategorieMedicament();
        categorie.setStoc(100);
        categorie.setTemperatura(20);

        when(repo.save(categorie)).thenReturn(categorie);

        CategorieMedicament saved = service.create(categorie);

        assertNotNull(saved);
        assertEquals(100, saved.getStoc());
        assertEquals(20, saved.getTemperatura());
        verify(repo).save(categorie);
    }

    @Test
    void getAll_shouldReturnAllCategories() {
        CategorieMedicament c1 = new CategorieMedicament();
        c1.setStoc(100);

        CategorieMedicament c2 = new CategorieMedicament();
        c2.setStoc(200);

        Pageable pageable = PageRequest.of(0, 10);
        Page<CategorieMedicament> page = new PageImpl<>(Arrays.asList(c1, c2), pageable, 2);

        when(repo.findAll(pageable)).thenReturn(page);

        Page<CategorieMedicament> result = service.getAll(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals(100, result.getContent().get(0).getStoc());
        assertEquals(200, result.getContent().get(1).getStoc());

        verify(repo).findAll(pageable);
    }

    @Test
    void getById_shouldReturnCategorie_whenExists() {
        CategorieMedicament categorie = new CategorieMedicament();
        categorie.setStoc(100);
        categorie.setTemperatura(20);

        when(repo.findById(1L)).thenReturn(Optional.of(categorie));

        CategorieMedicament result = service.getById(1L);

        assertNotNull(result);
        assertEquals(100, result.getStoc());
        assertEquals(20, result.getTemperatura());
        verify(repo).findById(1L);
    }

    @Test
    void getById_shouldThrowException_whenNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));

        verify(repo).findById(99L);
    }

    @Test
    void update_shouldModifyFields_whenCategoryExists() {
        CategorieMedicament existing = new CategorieMedicament();
        existing.setStoc(100);
        existing.setTemperatura(20);

        CategorieMedicament updated = new CategorieMedicament();
        updated.setStoc(300);
        updated.setTemperatura(5);

        CategorieMedicament saved = new CategorieMedicament();
        saved.setStoc(300);
        saved.setTemperatura(5);

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(saved);

        CategorieMedicament result = service.update(1L, updated);

        assertNotNull(result);
        assertEquals(300, result.getStoc());
        assertEquals(5, result.getTemperatura());

        verify(repo).findById(1L);
        verify(repo).save(existing);
    }

    @Test
    void update_shouldThrowException_whenCategoryNotFound() {
        CategorieMedicament updated = new CategorieMedicament();
        updated.setStoc(500);
        updated.setTemperatura(10);

        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(99L, updated));

        verify(repo).findById(99L);
        verify(repo, never()).save(any(CategorieMedicament.class));
    }

    @Test
    void delete_shouldDeleteCategorie_whenExists() {
        CategorieMedicament existing = new CategorieMedicament();

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
        verify(repo, never()).delete(any(CategorieMedicament.class));
    }
}