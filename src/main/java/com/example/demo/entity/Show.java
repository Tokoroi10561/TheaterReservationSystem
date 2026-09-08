package com.example.demo.entity;

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

import com.example.demo.constant.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "shows")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Slf4j
public class Show {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "title", nullable = false, length = 100)
	private String title;
	
	@Column(name = "place", nullable = true, length = 100)
	private String place;
	
	@Column(name = "flyer_url", nullable = true, length = 255)
	private String flyerImageUrl;
	
	@ManyToOne
	@JoinColumn(name = "troupe_id", nullable = false)
	private Long troupeId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "payment_method", nullable = false)
	private PaymentMethod paymentMethod;
	
	@OneToMany(mappedBy = "show")
	private List<Stage> stages;
	
	@OneToMany(mappedBy = "show")
	private List<TicketType> ticketTypes;
	
	@OneToMany(mappedBy = "show")
	private List<Staff> staffs;
}
