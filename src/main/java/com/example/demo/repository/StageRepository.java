package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Stage;

@Repository
public interface StageRepository extends JpaRepository<Stage, Long> {
	@Query("SELECT s FROM Stage s ORDER BY s.show.id, s.startTime")
	List<Stage> findByShowId(Long showId);
}
