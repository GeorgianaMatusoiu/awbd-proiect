package com.awbd.demo.controller;

import com.awbd.demo.dto.MedicamentRequest;
import com.awbd.demo.entity.Medicament;
import com.awbd.demo.service.MedicamentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicamente")
public class MedicamentController {

    private final MedicamentService service;

    public MedicamentController(MedicamentService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Medicament create(@RequestBody @Valid MedicamentRequest req) {
        return service.create(req);
    }

    @GetMapping
    public List<Medicament> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Medicament getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Medicament update(@PathVariable Long id, @RequestBody @Valid MedicamentRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}