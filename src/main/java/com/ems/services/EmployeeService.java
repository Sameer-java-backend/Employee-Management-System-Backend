package com.ems.services;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ems.entities.Employee;
import com.ems.repository.EmployeeRepository;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //Save a new Employee
    public Employee saveEmployee(Employee employee) {
        // Password ko encrypt karna zaroori hai security ke liye
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        return employeeRepository.save(employee);
    }

    // 2. Get all Employees (For Admin to see list)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // 3. Get Employee by ID (For viewing profile)
    public Optional<Employee> getEmployeeById(Integer id) {
        return employeeRepository.findById(id);
    }

    // 4. Update an Employee
    public Employee updateEmployee(Integer id, Employee updatedEmployee) {
    	
        Optional<Employee> existingEmployee = employeeRepository.findById(id);
        
        if (existingEmployee.isPresent()) {
            Employee employee = existingEmployee.get();
            employee.setName(updatedEmployee.getName());
            employee.setEmail(updatedEmployee.getEmail());
            employee.setCity(updatedEmployee.getCity());
            employee.setSalary(updatedEmployee.getSalary());
            employee.setDepartment(updatedEmployee.getDepartment());
           
            return employeeRepository.save(employee);
        }
        return null; 
    }

    // 5. Delete Employee
    public void deleteEmployee(Integer id) {
        employeeRepository.deleteById(id);
    }
    
    public Employee getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email).orElse(null);
    }
}