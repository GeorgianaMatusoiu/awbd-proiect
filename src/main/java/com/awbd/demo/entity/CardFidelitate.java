package com.awbd.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "card_fidelitate")
public class CardFidelitate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_card")
    private Long id;

    @NotBlank(message = "Nivelul este obligatoriu")
    @Column(nullable = false)
    private String nivel;

    @NotNull(message = "Punctele sunt obligatorii")
    @Min(value = 0, message = "Punctele nu pot fi negative")
    @Column(nullable = false)
    private Integer puncte;

    @OneToOne
    @JoinColumn(name = "id_client", nullable = false, unique = true)
    private Client client;

    public CardFidelitate() {
    }

    public Long getId() {
        return id;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}