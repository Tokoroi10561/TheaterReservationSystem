package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Show;

public interface ShowRepository extends JpaRepository<Show, Long> {

	Optional<Show> findById(Long id);
	
	@Query("SELECT s FROM show s WHERE s.deletedAt IS NULL AND s.troupe.id = :troupeId")
	List<Show> findAll(Long troupeId);
	@Query("SELECT s FROM Show s WHERE s.title LIKE %:title%")
	List<Show>findByTitle(String title);
}
