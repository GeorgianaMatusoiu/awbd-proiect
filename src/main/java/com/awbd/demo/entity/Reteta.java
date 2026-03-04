package com.awbd.demo.entity;

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

    @NotNull(message = "Clientul este obligatoriu")
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @NotNull(message = "Farmacistul este obligatoriu")
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_farmacist", nullable = false)
    private Farmacist farmacist;

    public Reteta() {}

    public Long getId() { return id; }

    public LocalDate getDataTiparire() { return dataTiparire; }
    public void setDataTiparire(LocalDate dataTiparire) { this.dataTiparire = dataTiparire; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public Farmacist getFarmacist() { return farmacist; }
    public void setFarmacist(Farmacist farmacist) { this.farmacist = farmacist; }
}