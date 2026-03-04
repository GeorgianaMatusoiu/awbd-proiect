package com.awbd.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "farmacisti")
public class Farmacist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_farmacist")
    private Long id;

    @NotBlank(message = "Numele este obligatoriu")
    @Column(nullable = false)
    private String nume;

    @NotBlank(message = "Prenumele este obligatoriu")
    @Column(nullable = false)
    private String prenume;

    @PastOrPresent(message = "Data angajarii trebuie sa fie in trecut sau azi")
    @Column(name = "data_angajarii")
    private LocalDate dataAngajarii;

    @Size(max = 20, message = "Telefon prea lung")
    private String telefon;

    @Email(message = "Email invalid")
    @Column(unique = true)
    private String email;

    @DecimalMin(value = "0.0", message = "Salariul nu poate fi negativ")
    @Digits(integer = 6, fraction = 2, message = "Salariu invalid")
    private BigDecimal salariu;

    public Farmacist() {
    }

    public Long getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public LocalDate getDataAngajarii() {
        return dataAngajarii;
    }

    public void setDataAngajarii(LocalDate dataAngajarii) {
        this.dataAngajarii = dataAngajarii;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getSalariu() {
        return salariu;
    }

    public void setSalariu(BigDecimal salariu) {
        this.salariu = salariu;
    }
}