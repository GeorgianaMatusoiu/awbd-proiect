package com.awbd.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DetaliiRetetaId implements Serializable {

    @Column(name = "nr_reteta")
    private Long retetaId;

    @Column(name = "cod_medicament")
    private Long medicamentId;

    public DetaliiRetetaId() {
    }

    public DetaliiRetetaId(Long retetaId, Long medicamentId) {
        this.retetaId = retetaId;
        this.medicamentId = medicamentId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetaliiRetetaId that)) return false;
        return Objects.equals(retetaId, that.retetaId) &&
                Objects.equals(medicamentId, that.medicamentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(retetaId, medicamentId);
    }
}