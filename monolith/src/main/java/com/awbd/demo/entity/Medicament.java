package com.awbd.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "medicamente")
public class Medicament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_medicament")
    private Long id;

    @NotBlank(message = "Denumirea este obligatorie")
    @Column(nullable = false)
    private String denumire;

    @NotNull(message = "Data expirarii este obligatorie")
    @Column(name = "data_expirare", nullable = false)
    private LocalDate dataExpirare;

    @NotNull(message = "Pretul este obligatoriu")
    @DecimalMin(value = "0.0", message = "Pretul nu poate fi negativ")
    @Digits(integer = 6, fraction = 2, message = "Pret invalid")
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal pret;

    @NotNull(message = "Prospectul este obligatoriu")
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_prospect", nullable = false)
    private Prospect prospect;

    @NotNull(message = "Furnizorul este obligatoriu")
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_furnizor", nullable = false)
    private Furnizor furnizor;

    @NotNull(message = "Categoria este obligatorie")
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_categorie", nullable = false)
    private CategorieMedicament categorie;

    public Medicament() {
    }

    public Long getId() {
        return id;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public LocalDate getDataExpirare() {
        return dataExpirare;
    }

    public void setDataExpirare(LocalDate dataExpirare) {
        this.dataExpirare = dataExpirare;
    }

    public BigDecimal getPret() {
        return pret;
    }

    public void setPret(BigDecimal pret) {
        this.pret = pret;
    }

    public Prospect getProspect() {
        return prospect;
    }

    public void setProspect(Prospect prospect) {
        this.prospect = prospect;
    }

    public Furnizor getFurnizor() {
        return furnizor;
    }

    public void setFurnizor(Furnizor furnizor) {
        this.furnizor = furnizor;
    }

    public CategorieMedicament getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieMedicament categorie) {
        this.categorie = categorie;
    }
}