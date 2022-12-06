package com.example.appcommunicationcompany.controller;

import com.example.appcommunicationcompany.entity.User;
import com.example.appcommunicationcompany.payload.GenericResponse;
import com.example.appcommunicationcompany.payload.UserDto;
import com.example.appcommunicationcompany.payload.UserLogin;
import com.example.appcommunicationcompany.repository.UserRepository;
import com.example.appcommunicationcompany.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public UserDto singUp(@RequestBody UserDto userDto) {
        return authService.register(userDto);
    }

    @GetMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@RequestParam String email, @RequestParam String emailCode) {
        GenericResponse response = authService.verifyEmail(email, emailCode);
        return ResponseEntity.status(response.isSuccess() ? 201 : 409).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLogin userLogin) {
        GenericResponse login = authService.login(userLogin);
        return ResponseEntity.status(login.isSuccess() ? 201 : 409).body(login);

    }

}






























