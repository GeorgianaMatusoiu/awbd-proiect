package com.awbd.demo.controller;

import com.awbd.demo.entity.Furnizor;
import com.awbd.demo.service.FurnizorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<Furnizor> getAll() {
        return service.getAll();
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