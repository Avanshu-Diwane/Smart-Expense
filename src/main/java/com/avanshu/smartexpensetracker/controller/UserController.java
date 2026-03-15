package com.avanshu.smartexpensetracker.controller;

import com.avanshu.smartexpensetracker.DTO.LoginRequest;
import com.avanshu.smartexpensetracker.DTO.SignupDTO;
import com.avanshu.smartexpensetracker.config.JwtFilter;
import com.avanshu.smartexpensetracker.entity.User;
import com.avanshu.smartexpensetracker.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }



    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupDTO.SignupRequest request) {

        if(userService.emailExists(request.getEmail())){
            return ResponseEntity.badRequest().body("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        userService.saveUser(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = userService.login(request);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id,
                           @RequestBody User user) {
        return userService.updateUser(id, user);

    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();

    }

}