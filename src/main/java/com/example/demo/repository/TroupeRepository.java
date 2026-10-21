package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Troupe;

public interface TroupeRepository extends JpaRepository<Troupe, Long> {
	
	Optional<Troupe> findByEmail(String email);
}
