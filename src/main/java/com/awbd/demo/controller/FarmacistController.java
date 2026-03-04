
package com.awbd.demo.controller;

import com.awbd.demo.entity.Farmacist;
import com.awbd.demo.service.FarmacistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farmacisti")
public class FarmacistController {

    private final FarmacistService service;

    public FarmacistController(FarmacistService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Farmacist create(@RequestBody @Valid Farmacist farmacist) {
        return service.create(farmacist);
    }

    // READ ALL
    @GetMapping
    public List<Farmacist> getAll() {
        return service.getAll();
    }

    // READ ONE
    @GetMapping("/{id}")
    public Farmacist getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Farmacist update(@PathVariable Long id, @RequestBody @Valid Farmacist farmacist) {
        return service.update(id, farmacist);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}