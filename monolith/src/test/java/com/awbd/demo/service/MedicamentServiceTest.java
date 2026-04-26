package com.awbd.demo.service;

import com.awbd.demo.dto.MedicamentRequest;
import com.awbd.demo.entity.CategorieMedicament;
import com.awbd.demo.entity.Furnizor;
import com.awbd.demo.entity.Medicament;
import com.awbd.demo.entity.Prospect;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.CategorieMedicamentRepository;
import com.awbd.demo.repository.FurnizorRepository;
import com.awbd.demo.repository.MedicamentRepository;
import com.awbd.demo.repository.ProspectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicamentServiceTest {

    @Mock
    private MedicamentRepository medicamentRepo;
    @Mock
    private FurnizorRepository furnizorRepo;
    @Mock
    private ProspectRepository prospectRepo;
    @Mock
    private CategorieMedicamentRepository categorieRepo;

    @InjectMocks
    private MedicamentService service;

    @Test
    void create_shouldSaveMedicament_whenAllRelationsExist() {
        MedicamentRequest req = new MedicamentRequest();
        req.setDenumire("Paracetamol");
        req.setDataExpirare(LocalDate.of(2027, 5, 10));
        req.setPret(new BigDecimal("15.50"));
        req.setFurnizorId(1L);
        req.setProspectId(2L);
        req.setCategorieId(3L);

        Furnizor furnizor = new Furnizor();
        Prospect prospect = new Prospect();
        CategorieMedicament categorie = new CategorieMedicament();

        Medicament medicament = new Medicament();
        medicament.setDenumire("Paracetamol");
        medicament.setDataExpirare(req.getDataExpirare());
        medicament.setPret(req.getPret());
        medicament.setFurnizor(furnizor);
        medicament.setProspect(prospect);
        medicament.setCategorie(categorie);

        when(furnizorRepo.findById(1L)).thenReturn(Optional.of(furnizor));
        when(prospectRepo.findById(2L)).thenReturn(Optional.of(prospect));
        when(categorieRepo.findById(3L)).thenReturn(Optional.of(categorie));
        when(medicamentRepo.save(any(Medicament.class))).thenReturn(medicament);

        Medicament result = service.create(req);

        assertNotNull(result);
        assertEquals("Paracetamol", result.getDenumire());
        verify(furnizorRepo).findById(1L);
        verify(prospectRepo).findById(2L);
        verify(categorieRepo).findById(3L);
        verify(medicamentRepo).save(any(Medicament.class));
    }

    @Test
    void create_shouldThrowException_whenFurnizorDoesNotExist() {
        MedicamentRequest req = new MedicamentRequest();
        req.setFurnizorId(1L);
        req.setProspectId(2L);
        req.setCategorieId(3L);

        when(furnizorRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.create(req));

        verify(furnizorRepo).findById(1L);
        verify(medicamentRepo, never()).save(any());
    }

    @Test
    void getById_shouldReturnMedicament_whenExists() {
        Medicament medicament = new Medicament();
        medicament.setDenumire("Paracetamol");

        when(medicamentRepo.findById(1L)).thenReturn(Optional.of(medicament));

        Medicament result = service.getById(1L);

        assertEquals("Paracetamol", result.getDenumire());
        verify(medicamentRepo).findById(1L);
    }

    @Test
    void getById_shouldThrowException_whenNotFound() {
        when(medicamentRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));

        verify(medicamentRepo).findById(99L);
    }
}