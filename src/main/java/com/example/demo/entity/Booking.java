package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.example.demo.constant.BookingStatus;
import com.example.demo.constant.PaymentMethod;
import com.example.demo.constant.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Booking {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name", nullable = false, length = 100)
	private String name;
	
	@Column(name = "email", nullable = false, length = 100)
	private String email;
	
	@Column(name = "phone", nullable = true, length = 20)
	private String phone;
	
	@Column(name = "address", nullable = true, length = 255)
	private String address;
	
	@Column(name = "remarks", nullable = true, length = 1000)
	private String remarks;
	
	@Column(name = "token", nullable = false, unique = true, length = 255)
	private String token;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "payment_method", nullable = false)
	private PaymentMethod paymentMethod;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "booking_status", nullable = false)
	private BookingStatus bookingStatus;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "payment_status", nullable = false)
	private PaymentStatus paymentStatus;
	
	
	@ManyToOne
	@JoinColumn(name = "stage_id", nullable = false)
	private Stage stage;
	
	@ManyToOne
	@JoinColumn(name = "staff_id", nullable = true)
	private Staff staff;
	
	
	@OneToMany(mappedBy = "booking")
	private List<BookingDetails> bookingDetails;
}
