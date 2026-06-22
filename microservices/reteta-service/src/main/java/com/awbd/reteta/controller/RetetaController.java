package com.awbd.reteta.controller;

import com.awbd.reteta.dto.RetetaRequest;
import com.awbd.reteta.entity.Reteta;
import com.awbd.reteta.service.RetetaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/retete")
public class RetetaController {

    private final RetetaService service;

    public RetetaController(RetetaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reteta create(@RequestBody @Valid RetetaRequest req) {
        return service.create(req);
    }

    @GetMapping
    public Page<Reteta> getAll(Pageable pageable) {
        return service.getAll(pageable);
    }

    @GetMapping("/{id}")
    public Reteta getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Reteta update(@PathVariable Long id, @RequestBody @Valid RetetaRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}