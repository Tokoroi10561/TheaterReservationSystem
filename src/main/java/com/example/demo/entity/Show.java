package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
import com.example.demo.constant.ShowOption;
import com.example.demo.constant.ShowStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "shows")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Show {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "title", nullable = false, length = 100)
	private String title;
	
	@Column(name = "place", nullable = true, length = 100)
	private String place;
	
	@Column(name = "flyer_file_name", nullable = true, length = 255)
	private String flyerFileName;
	
	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;
	
	@ManyToOne
	@JoinColumn(name = "troupe_id", nullable = false)
	private Troupe troupe;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "payment_method", nullable = false)
	private PaymentMethod paymentMethod;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "show_status", nullable = false)
	private ShowStatus showStatus;
	
	@OneToMany(mappedBy = "show")
	private List<Stage> stages;
	
	@OneToMany(mappedBy = "show")
	private List<TicketType> ticketTypes;
	
	@OneToMany(mappedBy = "show")
	private List<Staff> staffs;
	
	@ElementCollection(targetClass = ShowOption.class)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "show_opts",
		    joinColumns = @JoinColumn(name = "show_id"))
	@Column(name = "option_name")
	private Set<ShowOption> options;
}
