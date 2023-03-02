package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.entity.User;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    private final UserDetailsService userDetailsService;

    public UserRestController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping()
    public ResponseEntity<User> getUser(@AuthenticationPrincipal UserDetails userDetails) {
        UserDetails user = userDetailsService.loadUserByUsername(userDetails.getUsername());
        return new ResponseEntity<>((User) user, HttpStatus.OK);
    }
}

