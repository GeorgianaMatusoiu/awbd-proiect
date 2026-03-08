package com.awbd.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "profil_client")
public class ProfilClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profil")
    private Long id;

    @NotBlank(message = "Vaccinarile sunt obligatorii")
    @Column(nullable = false)
    private String vaccinari;

    @NotBlank(message = "Alergiile sunt obligatorii")
    @Column(nullable = false)
    private String alergie;

    @OneToOne
    @JoinColumn(name = "id_client", nullable = false, unique = true)
    private Client client;

    public ProfilClient() {
    }

    public Long getId() {
        return id;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}