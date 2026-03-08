package com.awbd.demo.controller;

import com.awbd.demo.dto.DetaliiRetetaRequest;
import com.awbd.demo.entity.DetaliiReteta;
import com.awbd.demo.service.DetaliiRetetaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalii-retete")
public class DetaliiRetetaController {

    private final DetaliiRetetaService service;

    public DetaliiRetetaController(DetaliiRetetaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DetaliiReteta create(@RequestBody @Valid DetaliiRetetaRequest req) {
        return service.create(req);
    }

    @GetMapping
    public List<DetaliiReteta> getAll() {
        return service.getAll();
    }

    @GetMapping("/{retetaId}/{medicamentId}")
    public DetaliiReteta getById(@PathVariable Long retetaId, @PathVariable Long medicamentId) {
        return service.getById(retetaId, medicamentId);
    }

    @PutMapping("/{retetaId}/{medicamentId}")
    public DetaliiReteta update(@PathVariable Long retetaId,
                                @PathVariable Long medicamentId,
                                @RequestBody @Valid DetaliiRetetaRequest req) {
        return service.update(retetaId, medicamentId, req);
    }

    @DeleteMapping("/{retetaId}/{medicamentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long retetaId, @PathVariable Long medicamentId) {
        service.delete(retetaId, medicamentId);
    }
}