package com.awbd.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "detalii_retete")
public class DetaliiReteta {

    @EmbeddedId
    private DetaliiRetetaId id;

    @ManyToOne(optional = false)
    @MapsId("retetaId")
    @JoinColumn(name = "nr_reteta", nullable = false)
    private Reteta reteta;

    @ManyToOne(optional = false)
    @MapsId("medicamentId")
    @JoinColumn(name = "cod_medicament", nullable = false)
    private Medicament medicament;

    @NotNull(message = "Pretul este obligatoriu")
    @DecimalMin(value = "0.0", message = "Pretul nu poate fi negativ")
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal pret;

    @NotNull(message = "Cantitatea este obligatorie")
    @Min(value = 1, message = "Cantitatea trebuie sa fie de cel putin 1")
    @Column(nullable = false)
    private Integer cantitate;

    public DetaliiReteta() {
    }

    public DetaliiRetetaId getId() {
        return id;
    }

    public void setId(DetaliiRetetaId id) {
        this.id = id;
    }

    public Reteta getReteta() {
        return reteta;
    }

    public void setReteta(Reteta reteta) {
        this.reteta = reteta;
    }

    public Medicament getMedicament() {
        return medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }

    public BigDecimal getPret() {
        return pret;
    }

    public void setPret(BigDecimal pret) {
        this.pret = pret;
    }

    public Integer getCantitate() {
        return cantitate;
    }

    public void setCantitate(Integer cantitate) {
        this.cantitate = cantitate;
    }
}