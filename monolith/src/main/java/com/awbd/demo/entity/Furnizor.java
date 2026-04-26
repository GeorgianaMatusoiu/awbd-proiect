package com.awbd.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "furnizori")
public class Furnizor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_furnizor")
    private Long id;

    @NotBlank(message = "Numele este obligatoriu")
    @Column(nullable = false)
    private String nume;

    @NotBlank(message = "Adresa este obligatorie")
    @Column(nullable = false)
    private String adresa;

    @NotBlank(message = "Orasul este obligatoriu")
    @Column(nullable = false)
    private String oras;

    @NotBlank(message = "Tara este obligatorie")
    @Column(nullable = false)
    private String tara;

    @Size(max = 20, message = "Telefon prea lung")
    private String telefon;

    public Furnizor() {
    }

    public Long getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getOras() {
        return oras;
    }

    public void setOras(String oras) {
        this.oras = oras;
    }

    public String getTara() {
        return tara;
    }

    public void setTara(String tara) {
        this.tara = tara;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
}