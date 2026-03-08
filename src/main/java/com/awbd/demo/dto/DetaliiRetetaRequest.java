package com.awbd.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class DetaliiRetetaRequest {

    @NotNull(message = "retetaId este obligatoriu")
    private Long retetaId;

    @NotNull(message = "medicamentId este obligatoriu")
    private Long medicamentId;

    @NotNull(message = "Pretul este obligatoriu")
    @DecimalMin(value = "0.0", message = "Pretul nu poate fi negativ")
    private BigDecimal pret;

    @NotNull(message = "Cantitatea este obligatorie")
    @Min(value = 1, message = "Cantitatea trebuie sa fie de cel putin 1")
    private Integer cantitate;

    public DetaliiRetetaRequest() {
    }

    public Long getRetetaId() {
        return retetaId;
    }

    public void setRetetaId(Long retetaId) {
        this.retetaId = retetaId;
    }

    public Long getMedicamentId() {
        return medicamentId;
    }

    public void setMedicamentId(Long medicamentId) {
        this.medicamentId = medicamentId;
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