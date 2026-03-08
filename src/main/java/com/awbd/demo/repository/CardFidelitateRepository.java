package com.awbd.demo.repository;

import com.awbd.demo.entity.CardFidelitate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardFidelitateRepository extends JpaRepository<CardFidelitate, Long> {

    boolean existsByClientId(Long clientId);
}