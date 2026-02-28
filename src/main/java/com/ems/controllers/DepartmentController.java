package com.ems.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ems.entities.Department;
import com.ems.services.DepartmentService;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;
	
	@PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody Department department){
		
		Department savedDepartment=departmentService.saveDepartment(department);
		return ResponseEntity.ok(savedDepartment);
	}
	
	@GetMapping
	public ResponseEntity<List<Department>> getAllDepartments(){
		return ResponseEntity.ok(departmentService.getAllDepartments());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteDepartment(@PathVariable Integer id){
		
		departmentService.deleteDepartment(id);
		return ResponseEntity.noContent().build();
	}


}
