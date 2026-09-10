package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.constant.BookingStatus;
import com.example.demo.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
	
	//後でnameとemailをキーワード検索できるようにしたい
	@Query("SELECT b FROM Booking b WHERE b.name LIKE %:name%")
	List<Booking> findByName(String name);
	
	@Query("SELECT b FROM Booking b WHERE b.email LIKE %:email%")
	List<Booking> findByEmail(String email);
	
	@Query("SELECT b FROM Booking b WHERE b.stageId = :stageId AND b.bookingStatus = :status ORDER BY b.bookingStatus DESC ")
	List<Booking> findByStageId(Long stageId, BookingStatus status);
	
	List<Booking> findByStageIdAndTicketTypeIdAndStatus(Long stageId, Long ticketTypeId, BookingStatus status);
	
	List<Booking> findByToken(String token);
	
	List<Booking> findByStageId(Long stageId);
	
	List<Booking> findByStaffId(Long staffId);
}
