package com.prueba.login.controller;

import com.prueba.login.dto.request.CustomerUserDtoReq;
import com.prueba.login.dto.response.CustomerUserDtoRes;
import com.prueba.login.service.ICustomerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private ICustomerUserService userService;

    @PostMapping("/register")
    public ResponseEntity<CustomerUserDtoRes> registerUser(@RequestBody CustomerUserDtoReq userDtoReq){
        return new ResponseEntity<>(userService.saveUser(userDtoReq), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerUserDtoRes> getUserById(@PathVariable Long id){
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("/my_perfil")
    public ResponseEntity<CustomerUserDtoRes> getUserAuthenticated(Authentication authentication){
        return new ResponseEntity<>(userService.getUserAuthenticated(authentication.getPrincipal().toString()), HttpStatus.OK);
    }

}
