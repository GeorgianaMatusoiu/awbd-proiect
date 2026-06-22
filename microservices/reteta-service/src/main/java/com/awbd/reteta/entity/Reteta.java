package com.awbd.reteta.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "retete")
public class Reteta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nr_reteta")
    private Long id;

    @NotNull(message = "Data tiparirii este obligatorie")
    @Column(name = "data_tiparire", nullable = false)
    private LocalDate dataTiparire;

    @NotNull(message = "clientId este obligatoriu")
    @Column(name = "id_client", nullable = false)
    private Long clientId;

    @NotNull(message = "farmacistId este obligatoriu")
    @Column(name = "id_farmacist", nullable = false)
    private Long farmacistId;

    public Reteta() {
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDataTiparire() {
        return dataTiparire;
    }

    public void setDataTiparire(LocalDate dataTiparire) {
        this.dataTiparire = dataTiparire;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getFarmacistId() {
        return farmacistId;
    }

    public void setFarmacistId(Long farmacistId) {
        this.farmacistId = farmacistId;
    }
}