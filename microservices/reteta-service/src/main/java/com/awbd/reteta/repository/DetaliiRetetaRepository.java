package com.awbd.reteta.repository;

import com.awbd.reteta.entity.DetaliiReteta;
import com.awbd.reteta.entity.DetaliiRetetaId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetaliiRetetaRepository extends JpaRepository<DetaliiReteta, DetaliiRetetaId> {
}