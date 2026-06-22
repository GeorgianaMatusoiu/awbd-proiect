package com.awbd.client.repository;

import com.awbd.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    boolean existsByCnp(String cnp);

}