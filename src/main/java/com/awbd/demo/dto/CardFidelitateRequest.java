package com.awbd.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CardFidelitateRequest {

    @NotBlank(message = "Nivelul este obligatoriu")
    private String nivel;

    @NotNull(message = "Punctele sunt obligatorii")
    @Min(value = 0, message = "Punctele nu pot fi negative")
    private Integer puncte;

    @NotNull(message = "clientId este obligatoriu")
    private Long clientId;

    public CardFidelitateRequest() {
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public Integer getPuncte() {
        return puncte;
    }

    public void setPuncte(Integer puncte) {
        this.puncte = puncte;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}