package com.awbd.demo.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class RetetaRequest {

    @NotNull(message = "dataTiparire este obligatorie")
    private LocalDate dataTiparire;

    @NotNull(message = "clientId este obligatoriu")
    private Long clientId;

    @NotNull(message = "farmacistId este obligatoriu")
    private Long farmacistId;

    public RetetaRequest() {}

    public LocalDate getDataTiparire() { return dataTiparire; }
    public void setDataTiparire(LocalDate dataTiparire) { this.dataTiparire = dataTiparire; }

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public Long getFarmacistId() { return farmacistId; }
    public void setFarmacistId(Long farmacistId) { this.farmacistId = farmacistId; }
}