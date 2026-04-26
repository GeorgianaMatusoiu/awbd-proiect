package com.awbd.demo.integration;

import com.awbd.demo.dto.MedicamentRequest;
import com.awbd.demo.entity.CategorieMedicament;
import com.awbd.demo.entity.Furnizor;
import com.awbd.demo.entity.Medicament;
import com.awbd.demo.entity.Prospect;
import com.awbd.demo.service.CategorieMedicamentService;
import com.awbd.demo.service.FurnizorService;
import com.awbd.demo.service.MedicamentService;
import com.awbd.demo.service.ProspectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MedicamentIntegrationTest {

    @Autowired
    private MedicamentService medicamentService;

    @Autowired
    private ProspectService prospectService;

    @Autowired
    private CategorieMedicamentService categorieService;

    @Autowired
    private FurnizorService furnizorService;

    @Test
    void createCategorieProspectAndFurnizor_thenCreateMedicament_thenFetchMedicament() {
        CategorieMedicament categorie = new CategorieMedicament();
        categorie.setStoc(100);
        categorie.setTemperatura(20);

        CategorieMedicament savedCategorie = categorieService.create(categorie);

        assertNotNull(savedCategorie);
        assertNotNull(savedCategorie.getId());

        Prospect prospect = new Prospect();
        prospect.setAfectiuni("Durere, febra");
        prospect.setAdministrare("2 comprimate pe zi");

        Prospect savedProspect = prospectService.create(prospect);

        assertNotNull(savedProspect);
        assertNotNull(savedProspect.getId());

        Furnizor furnizor = new Furnizor();
        furnizor.setNume("FarmSupplier");
        furnizor.setAdresa("Str. Lalelelor 10");
        furnizor.setOras("Bucuresti");
        furnizor.setTara("Romania");
        furnizor.setTelefon("0711111111");

        Furnizor savedFurnizor = furnizorService.create(furnizor);

        assertNotNull(savedFurnizor);
        assertNotNull(savedFurnizor.getId());

        MedicamentRequest request = new MedicamentRequest();
        request.setDenumire("Paracetamol");
        request.setDataExpirare(LocalDate.of(2027, 1, 1));
        request.setPret(new BigDecimal("15.50"));
        request.setFurnizorId(savedFurnizor.getId());
        request.setProspectId(savedProspect.getId());
        request.setCategorieId(savedCategorie.getId());

        Medicament savedMedicament = medicamentService.create(request);

        assertNotNull(savedMedicament);
        assertNotNull(savedMedicament.getId());
        assertEquals("Paracetamol", savedMedicament.getDenumire());
        assertEquals(new BigDecimal("15.50"), savedMedicament.getPret());
        assertEquals(savedFurnizor.getId(), savedMedicament.getFurnizor().getId());
        assertEquals(savedCategorie.getId(), savedMedicament.getCategorie().getId());
        assertEquals(savedProspect.getId(), savedMedicament.getProspect().getId());

        Medicament fetched = medicamentService.getById(savedMedicament.getId());

        assertNotNull(fetched);
        assertEquals("Paracetamol", fetched.getDenumire());
        assertEquals(new BigDecimal("15.50"), fetched.getPret());
        assertEquals(savedFurnizor.getId(), fetched.getFurnizor().getId());
        assertEquals(savedCategorie.getId(), fetched.getCategorie().getId());
        assertEquals(savedProspect.getId(), fetched.getProspect().getId());

        Page<Medicament> all = medicamentService.getAll(PageRequest.of(0, 10));

        assertEquals(1, all.getContent().size());
        assertEquals("Paracetamol", all.getContent().get(0).getDenumire());
    }
}