package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Show;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
	
	@Query("SELECT s FROM Show s WHERE s.title LIKE %:title%")
	List<Show>findByTitle(String title);
	
	List<Show> findByTroupeId(Long troupeId);
}
