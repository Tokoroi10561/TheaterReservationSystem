package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "stages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Slf4j
public class Stage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "start_time", nullable = false)
	private LocalDateTime startTime;
	
	@Column(name = "end_time", nullable = false)
	private LocalDateTime endTime;
	
	@Column(name = "open_time", nullable = false)
	private LocalDateTime openTime;
	
	@Column(name = "capacity", nullable = false)
	private Integer capacity;
	
	@ManyToOne
	@JoinColumn(name = "show_id", nullable = false)
	private Long showId;
}
