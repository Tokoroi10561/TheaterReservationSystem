package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.example.demo.constant.BookingStatus;
import com.example.demo.constant.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Slf4j
public class Booking {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name", nullable = false, length = 100)
	private String name;
	
	@Column(name = "email", nullable = false, unique = true, length = 100)
	private String email;
	
	@Column(name = "phone", nullable = false, length = 20)
	private String phone;
	
	@Column(name = "address", nullable = true, length = 255)
	private String address;
	
	@Column(name = "remarks", nullable = true, length = 1000)
	private String remarks;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "booking_status", nullable = false)
	private BookingStatus bookingStatus;
	
	@Column(name = "token", nullable = false, unique = true, length = 255)
	private String token;
	
	@ManyToOne
	@JoinColumn(name = "stage_id", nullable = false)
	private Long stageId;
	
	@ManyToOne
	@JoinColumn(name = "ticket_type_id", nullable = false)
	private Long ticketTypeId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "payment_method", nullable = false)
	private PaymentMethod paymentMethod;
	
	@ManyToOne
	@JoinColumn(name = "staff_id", nullable = false)
	private Long staffId;
	
	@OneToOne
	@JoinColumn(name = "booking_details_id", nullable = false)
	private Long bookingDetailsId;
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
}
