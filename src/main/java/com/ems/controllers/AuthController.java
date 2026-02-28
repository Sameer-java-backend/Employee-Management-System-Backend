package com.ems.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ems.dto.AuthRequest;
import com.ems.entities.Employee;
import com.ems.security.JWTUtils;
import com.ems.services.EmployeeService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
    private EmployeeService employeeService;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    // 1. REGISTER USER
    @PostMapping("/signup")
    public ResponseEntity<Employee> signUp(@RequestBody Employee employee){
        return ResponseEntity.ok(employeeService.saveEmployee(employee));
    }

    // 2. LOGIN USER
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest){
        
        // A. Pehle check karo user sahi hai ya nahi (AuthenticationManager)
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (Exception e) {
            // Agar password galat hua toh error aayega
            return ResponseEntity.status(401).body("Invalid Email or Password");
        }

        // B. Agar sahi hai, toh User details nikalo (Token banane ke liye)
        // Note: Hum seedha DB se nikal rahe hain kyunki auth successful ho chuka hai
        var user = employeeService.getEmployeeByEmail(authRequest.getEmail()); 
        // C. Token generate karo
        String token = jwtUtils.generateToken(user);

        return ResponseEntity.ok(token);
    }
	
}
