package com.awbd.demo.controller;

import com.awbd.demo.dto.CardFidelitateRequest;
import com.awbd.demo.entity.CardFidelitate;
import com.awbd.demo.service.CardFidelitateService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carduri")
public class CardFidelitateController {

    private final CardFidelitateService service;

    public CardFidelitateController(CardFidelitateService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardFidelitate create(@RequestBody @Valid CardFidelitateRequest req) {
        return service.create(req);
    }

    @GetMapping
    public List<CardFidelitate> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public CardFidelitate getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public CardFidelitate update(@PathVariable Long id, @RequestBody @Valid CardFidelitateRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}