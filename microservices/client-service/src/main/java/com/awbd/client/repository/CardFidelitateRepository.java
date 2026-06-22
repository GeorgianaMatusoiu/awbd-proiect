package com.awbd.client.repository;

import com.awbd.client.entity.CardFidelitate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardFidelitateRepository extends JpaRepository<CardFidelitate, Long> {

    boolean existsByClientId(Long clientId);
}