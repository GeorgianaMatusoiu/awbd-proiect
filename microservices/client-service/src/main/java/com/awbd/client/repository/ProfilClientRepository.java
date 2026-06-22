package com.awbd.client.repository;

import com.awbd.client.entity.ProfilClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilClientRepository extends JpaRepository<ProfilClient, Long> {

    boolean existsByClientId(Long clientId);
}