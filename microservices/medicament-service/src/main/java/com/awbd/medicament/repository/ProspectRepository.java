package com.awbd.medicament.repository;

import com.awbd.medicament.entity.Prospect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProspectRepository extends JpaRepository<Prospect, Long> {
}