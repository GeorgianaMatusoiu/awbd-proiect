package com.awbd.demo.controller;

import com.awbd.demo.dto.ProfilClientRequest;
import com.awbd.demo.entity.ProfilClient;
import com.awbd.demo.service.ProfilClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiluri")
public class ProfilClientController {

    private final ProfilClientService service;

    public ProfilClientController(ProfilClientService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfilClient create(@RequestBody @Valid ProfilClientRequest req) {
        return service.create(req);
    }

    @GetMapping
    public List<ProfilClient> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ProfilClient getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public ProfilClient update(@PathVariable Long id, @RequestBody @Valid ProfilClientRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}