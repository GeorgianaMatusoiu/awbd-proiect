package com.awbd.demo.service;

import com.awbd.demo.dto.DetaliiRetetaRequest;
import com.awbd.demo.entity.DetaliiReteta;
import com.awbd.demo.entity.DetaliiRetetaId;
import com.awbd.demo.entity.Medicament;
import com.awbd.demo.entity.Reteta;
import com.awbd.demo.exception.BadRequestException;
import com.awbd.demo.exception.ResourceNotFoundException;
import com.awbd.demo.repository.DetaliiRetetaRepository;
import com.awbd.demo.repository.MedicamentRepository;
import com.awbd.demo.repository.RetetaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetaliiRetetaServiceTest {

    @Mock
    private DetaliiRetetaRepository detaliiRepo;

    @Mock
    private RetetaRepository retetaRepo;

    @Mock
    private MedicamentRepository medicamentRepo;

    @InjectMocks
    private DetaliiRetetaService service;

    @Test
    void create_shouldSaveDetalii_whenRetetaAndMedicamentExist() {
        DetaliiRetetaRequest req = new DetaliiRetetaRequest();
        req.setRetetaId(1L);
        req.setMedicamentId(2L);
        req.setPret(new BigDecimal("25.50"));
        req.setCantitate(2);

        Reteta reteta = new Reteta();
        Medicament medicament = new Medicament();

        DetaliiReteta detalii = new DetaliiReteta();
        detalii.setId(new DetaliiRetetaId(1L, 2L));
        detalii.setReteta(reteta);
        detalii.setMedicament(medicament);
        detalii.setPret(req.getPret());
        detalii.setCantitate(req.getCantitate());

        when(retetaRepo.findById(1L)).thenReturn(Optional.of(reteta));
        when(medicamentRepo.findById(2L)).thenReturn(Optional.of(medicament));
        when(detaliiRepo.existsById(any(DetaliiRetetaId.class))).thenReturn(false);
        when(detaliiRepo.save(any(DetaliiReteta.class))).thenReturn(detalii);

        DetaliiReteta result = service.create(req);

        assertNotNull(result);
        assertEquals(2, result.getCantitate());
        assertEquals(new BigDecimal("25.50"), result.getPret());
        verify(detaliiRepo).save(any(DetaliiReteta.class));
    }

    @Test
    void create_shouldMapRequestCorrectly() {
        DetaliiRetetaRequest req = new DetaliiRetetaRequest();
        req.setRetetaId(1L);
        req.setMedicamentId(2L);
        req.setPret(new BigDecimal("30.00"));
        req.setCantitate(3);

        Reteta reteta = new Reteta();
        Medicament medicament = new Medicament();

        DetaliiReteta saved = new DetaliiReteta();
        saved.setId(new DetaliiRetetaId(1L, 2L));
        saved.setReteta(reteta);
        saved.setMedicament(medicament);
        saved.setPret(new BigDecimal("30.00"));
        saved.setCantitate(3);

        when(retetaRepo.findById(1L)).thenReturn(Optional.of(reteta));
        when(medicamentRepo.findById(2L)).thenReturn(Optional.of(medicament));
        when(detaliiRepo.existsById(any(DetaliiRetetaId.class))).thenReturn(false);
        when(detaliiRepo.save(any(DetaliiReteta.class))).thenReturn(saved);

        service.create(req);

        ArgumentCaptor<DetaliiReteta> captor = ArgumentCaptor.forClass(DetaliiReteta.class);
        verify(detaliiRepo).save(captor.capture());

        DetaliiReteta entity = captor.getValue();
        assertEquals(new BigDecimal("30.00"), entity.getPret());
        assertEquals(3, entity.getCantitate());
        assertEquals(reteta, entity.getReteta());
        assertEquals(medicament, entity.getMedicament());
    }

    @Test
    void create_shouldThrowException_whenCombinationAlreadyExists() {
        DetaliiRetetaRequest req = new DetaliiRetetaRequest();
        req.setRetetaId(1L);
        req.setMedicamentId(2L);

        Reteta reteta = new Reteta();
        Medicament medicament = new Medicament();

        when(retetaRepo.findById(1L)).thenReturn(Optional.of(reteta));
        when(medicamentRepo.findById(2L)).thenReturn(Optional.of(medicament));
        when(detaliiRepo.existsById(any(DetaliiRetetaId.class))).thenReturn(true);

        assertThrows(BadRequestException.class, () -> service.create(req));

        verify(detaliiRepo, never()).save(any());
    }

    @Test
    void create_shouldThrowException_whenRetetaNotFound() {
        DetaliiRetetaRequest req = new DetaliiRetetaRequest();
        req.setRetetaId(1L);
        req.setMedicamentId(2L);

        when(retetaRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.create(req));

        verify(medicamentRepo, never()).findById(anyLong());
        verify(detaliiRepo, never()).existsById(any());
        verify(detaliiRepo, never()).save(any());
    }

    @Test
    void create_shouldThrowException_whenMedicamentNotFound() {
        DetaliiRetetaRequest req = new DetaliiRetetaRequest();
        req.setRetetaId(1L);
        req.setMedicamentId(2L);

        Reteta reteta = new Reteta();

        when(retetaRepo.findById(1L)).thenReturn(Optional.of(reteta));
        when(medicamentRepo.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.create(req));

        verify(detaliiRepo, never()).existsById(any());
        verify(detaliiRepo, never()).save(any());
    }

    @Test
    void getById_shouldReturnDetalii_whenExists() {
        DetaliiReteta detalii = new DetaliiReteta();
        detalii.setId(new DetaliiRetetaId(1L, 2L));

        when(detaliiRepo.findById(any(DetaliiRetetaId.class))).thenReturn(Optional.of(detalii));

        DetaliiReteta result = service.getById(1L, 2L);

        assertNotNull(result);
        assertEquals(1L, result.getId().getRetetaId());
        assertEquals(2L, result.getId().getMedicamentId());
        verify(detaliiRepo).findById(any(DetaliiRetetaId.class));
    }

    @Test
    void getById_shouldThrowException_whenNotFound() {
        when(detaliiRepo.findById(any(DetaliiRetetaId.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(1L, 2L));

        verify(detaliiRepo).findById(any(DetaliiRetetaId.class));
    }
}