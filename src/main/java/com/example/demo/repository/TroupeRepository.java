package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Troupe;

public interface TroupeRepository extends JpaRepository<Troupe, Long> {
	
	List<Troupe> findByEmail(String email);
}
