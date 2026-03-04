package com.awbd.demo.repository;

import com.awbd.demo.entity.Reteta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetetaRepository extends JpaRepository<Reteta, Long> {
}