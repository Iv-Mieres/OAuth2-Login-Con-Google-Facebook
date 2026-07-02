package com.prueba.login.user.api;

import com.prueba.login.user.application.UserService;
import com.prueba.login.user.api.dto.request.CreateUserRequest;
import com.prueba.login.user.api.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody CreateUserRequest userDtoReq){
        return new ResponseEntity<>(userService.saveUser(userDtoReq), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("/my_perfil")
    public ResponseEntity<UserResponse> getUserAuthenticated(Authentication authentication){
        return new ResponseEntity<>(userService.getUserAuthenticated(authentication.getPrincipal().toString()), HttpStatus.OK);
    }
}
