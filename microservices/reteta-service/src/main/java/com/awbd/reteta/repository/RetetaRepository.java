package com.awbd.reteta.repository;

import com.awbd.reteta.entity.Reteta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetetaRepository extends JpaRepository<Reteta, Long> {
}