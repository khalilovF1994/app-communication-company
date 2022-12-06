package com.example.appcommunicationcompany.controller;

import com.example.appcommunicationcompany.payload.UserDto;
import com.example.appcommunicationcompany.repository.UserRepository;
import com.example.appcommunicationcompany.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/update")
    public UserDto create(@RequestBody UserDto userDto) {
        return userService.update(userDto);

    }


}
