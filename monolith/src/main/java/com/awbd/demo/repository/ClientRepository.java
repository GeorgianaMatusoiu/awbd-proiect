package com.awbd.demo.repository;

import com.awbd.demo.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    boolean existsByCnp(String cnp);

}