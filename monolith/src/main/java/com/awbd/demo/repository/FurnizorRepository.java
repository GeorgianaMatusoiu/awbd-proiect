package com.awbd.demo.repository;

import com.awbd.demo.entity.Furnizor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FurnizorRepository extends JpaRepository<Furnizor, Long> {
}