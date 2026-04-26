package com.awbd.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "prospecte")
public class Prospect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prospect")
    private Long id;

    @NotBlank(message = "Afectiunile sunt obligatorii")
    @Column(nullable = false)
    private String afectiuni;

    @NotBlank(message = "Administrarea este obligatorie")
    @Column(nullable = false)
    private String administrare;

    public Prospect() {
    }

    public Long getId() {
        return id;
    }

    public String getAfectiuni() {
        return afectiuni;
    }

    public void setAfectiuni(String afectiuni) {
        this.afectiuni = afectiuni;
    }

    public String getAdministrare() {
        return administrare;
    }

    public void setAdministrare(String administrare) {
        this.administrare = administrare;
    }
}