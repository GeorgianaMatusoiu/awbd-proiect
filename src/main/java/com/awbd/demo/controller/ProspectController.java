package com.awbd.demo.controller;

import com.awbd.demo.entity.Prospect;
import com.awbd.demo.service.ProspectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prospecte")
public class ProspectController {

    private final ProspectService service;

    public ProspectController(ProspectService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prospect create(@RequestBody @Valid Prospect prospect) {
        return service.create(prospect);
    }

    @GetMapping
    public List<Prospect> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Prospect getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Prospect update(@PathVariable Long id, @RequestBody @Valid Prospect prospect) {
        return service.update(id, prospect);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}