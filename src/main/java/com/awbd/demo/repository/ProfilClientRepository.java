package com.awbd.demo.repository;

import com.awbd.demo.entity.ProfilClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilClientRepository extends JpaRepository<ProfilClient, Long> {

    boolean existsByClientId(Long clientId);
}