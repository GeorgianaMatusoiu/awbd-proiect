package com.awbd.demo.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicamentRequest {

    @NotBlank(message = "Denumirea este obligatorie")
    private String denumire;

    @NotNull(message = "Data expirarii este obligatorie")
    private LocalDate dataExpirare;

    @NotNull(message = "Pretul este obligatoriu")
    @DecimalMin(value = "0.0", message = "Pretul nu poate fi negativ")
    private BigDecimal pret;

    @NotNull(message = "prospectId este obligatoriu")
    private Long prospectId;

    @NotNull(message = "furnizorId este obligatoriu")
    private Long furnizorId;

    @NotNull(message = "categorieId este obligatoriu")
    private Long categorieId;

    public MedicamentRequest() {
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

    public Long getProspectId() {
        return prospectId;
    }

    public void setProspectId(Long prospectId) {
        this.prospectId = prospectId;
    }

    public Long getFurnizorId() {
        return furnizorId;
    }

    public void setFurnizorId(Long furnizorId) {
        this.furnizorId = furnizorId;
    }

    public Long getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(Long categorieId) {
        this.categorieId = categorieId;
    }
}