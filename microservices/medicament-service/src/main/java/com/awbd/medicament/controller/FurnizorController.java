package com.awbd.medicament.controller;

import com.awbd.medicament.entity.Furnizor;
import com.awbd.medicament.service.FurnizorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/furnizori")
public class FurnizorController {

    private final FurnizorService service;

    public FurnizorController(FurnizorService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Furnizor create(@RequestBody @Valid Furnizor furnizor) {
        return service.create(furnizor);
    }

    @GetMapping
    public Page<Furnizor> getAll(Pageable pageable) {
        return service.getAll(pageable);
    }

    @GetMapping("/{id}")
    public Furnizor getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Furnizor update(@PathVariable Long id, @RequestBody @Valid Furnizor furnizor) {
        return service.update(id, furnizor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}