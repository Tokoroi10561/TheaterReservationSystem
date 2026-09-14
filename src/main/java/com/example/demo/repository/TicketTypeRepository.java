package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.TicketType;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {

}
