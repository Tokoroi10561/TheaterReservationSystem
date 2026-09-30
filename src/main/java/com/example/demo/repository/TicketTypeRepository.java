package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.TicketType;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {

	@Query("SELECT t FROM TicketType t WHERE t.stage.id = :showId AND t.id = :id")
	Optional<TicketType> findByStageId(Long stageId);
	
	@Query("SELECT t FROM TicketType t WHERE t.show.id = :showId")
	List<TicketType> findAllTicketTypeByShowId(Long showId);
}
