package com.awbd.demo.repository;

import com.awbd.demo.entity.Prospect;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProspectRepository extends JpaRepository<Prospect, Long> {
}