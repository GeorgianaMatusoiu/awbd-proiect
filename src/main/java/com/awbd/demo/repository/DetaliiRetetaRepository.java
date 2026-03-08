package com.awbd.demo.repository;

import com.awbd.demo.entity.DetaliiReteta;
import com.awbd.demo.entity.DetaliiRetetaId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetaliiRetetaRepository extends JpaRepository<DetaliiReteta, DetaliiRetetaId> {
}