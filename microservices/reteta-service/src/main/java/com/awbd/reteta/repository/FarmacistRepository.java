package com.awbd.reteta.repository;

import com.awbd.reteta.entity.Farmacist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmacistRepository extends JpaRepository<Farmacist, Long> {

    boolean existsByEmail(String email);
}