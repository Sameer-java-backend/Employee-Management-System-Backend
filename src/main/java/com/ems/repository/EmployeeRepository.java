package com.ems.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ems.entities.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer>{

	Optional<Employee> findByEmail(String email); 
	
}
