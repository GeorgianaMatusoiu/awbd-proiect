package com.awbd.demo.controller;

import com.awbd.demo.entity.CategorieMedicament;
import com.awbd.demo.service.CategorieMedicamentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorii")
public class CategorieMedicamentController {

    private final CategorieMedicamentService service;

    public CategorieMedicamentController(CategorieMedicamentService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategorieMedicament create(@RequestBody @Valid CategorieMedicament categorie) {
        return service.create(categorie);
    }

    @GetMapping
    public List<CategorieMedicament> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public CategorieMedicament getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public CategorieMedicament update(@PathVariable Long id, @RequestBody @Valid CategorieMedicament categorie) {
        return service.update(id, categorie);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}