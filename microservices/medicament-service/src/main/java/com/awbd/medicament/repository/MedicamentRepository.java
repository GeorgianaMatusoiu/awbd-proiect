package com.awbd.medicament.repository;

import com.awbd.medicament.entity.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentRepository extends JpaRepository<Medicament, Long> {
}