package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    ResponseEntity<Object> registerUser(@RequestBody UserDto userDto) {
        if (userService.existsByUsername(userDto.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already taken!");
        }
        if (userService.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already taken!");
        }
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setUpdatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userService.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }
}
