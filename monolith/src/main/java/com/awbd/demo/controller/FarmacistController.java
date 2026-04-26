package com.awbd.demo.controller;

import com.awbd.demo.entity.Farmacist;
import com.awbd.demo.service.FarmacistService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/farmacisti")
public class FarmacistController {

    private final FarmacistService service;

    public FarmacistController(FarmacistService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Farmacist create(@RequestBody @Valid Farmacist farmacist) {
        return service.create(farmacist);
    }

    @GetMapping
    public Page<Farmacist> getAll(Pageable pageable) {
        return service.getAll(pageable);
    }

    @GetMapping("/{id}")
    public Farmacist getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Farmacist update(@PathVariable Long id, @RequestBody @Valid Farmacist farmacist) {
        return service.update(id, farmacist);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}