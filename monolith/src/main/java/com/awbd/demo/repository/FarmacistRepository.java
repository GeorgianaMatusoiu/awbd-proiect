
package com.awbd.demo.repository;

import com.awbd.demo.entity.Farmacist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmacistRepository extends JpaRepository<Farmacist, Long> {

    boolean existsByEmail(String email);

}