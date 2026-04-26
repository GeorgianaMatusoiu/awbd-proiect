package com.awbd.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "categorii_medicamente")
public class CategorieMedicament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categorie")
    private Long id;

    @NotNull(message = "Stocul este obligatoriu")
    @Min(value = 0, message = "Stocul nu poate fi negativ")
    private Integer stoc;

    @NotNull(message = "Temperatura este obligatorie")
    private Integer temperatura;

    public CategorieMedicament() {
    }

    public Long getId() {
        return id;
    }

    public Integer getStoc() {
        return stoc;
    }

    public void setStoc(Integer stoc) {
        this.stoc = stoc;
    }

    public Integer getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Integer temperatura) {
        this.temperatura = temperatura;
    }
}