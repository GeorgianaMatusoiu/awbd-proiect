package com.awbd.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProfilClientRequest {

    @NotBlank(message = "Vaccinarile sunt obligatorii")
    private String vaccinari;

    @NotBlank(message = "Alergia este obligatorie")
    private String alergie;

    @NotNull(message = "clientId este obligatoriu")
    private Long clientId;

    public ProfilClientRequest() {
    }

    public String getVaccinari() {
        return vaccinari;
    }

    public void setVaccinari(String vaccinari) {
        this.vaccinari = vaccinari;
    }

    public String getAlergie() {
        return alergie;
    }

    public void setAlergie(String alergie) {
        this.alergie = alergie;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}