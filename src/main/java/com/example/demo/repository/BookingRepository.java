package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.constant.BookingStatus;
import com.example.demo.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {
	
	Optional<Booking> findById(Long id);
	Optional<Booking> findByToken(String token);
	
	@Query("SELECT b FROM Booking b WHERE b.stage.id = :stageId AND b.bookingStatus = :bookingStatus ORDER BY b.createdAt DESC")
	List<Booking> findByStageId(Long stageId, BookingStatus bookingStatus);
	List<Booking> findByStaffId(Long staffId);
	List<Booking> findByBookingDetailsId(Long bookingDetailsId);
	}
