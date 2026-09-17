package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Long, Staff> {

	@Query("SELECT s FROM Staff s")
	List<Staff> findAllStaff();
}
