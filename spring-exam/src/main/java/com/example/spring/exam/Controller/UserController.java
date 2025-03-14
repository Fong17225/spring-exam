// src/main/java/com/example/controller/UserController.java
package com.example.spring.exam.Controller;


import com.example.spring.exam.Repository.UserRepository;
import com.example.spring.exam.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Create
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (userRepository.existsByName(user.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username '" + user.getName() + "' already exists");
        }
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    // Read all
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Search by name
    @GetMapping("/search")
    public List<User> searchUsers(@RequestParam("name") String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    // Read one
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            if (!existingUser.getName().equals(userDetails.getName())
                    && userRepository.existsByName(userDetails.getName())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Username '" + userDetails.getName() + "' already exists");
            }
            existingUser.setName(userDetails.getName());
            existingUser.setAge(userDetails.getAge());
            existingUser.setSalary(userDetails.getSalary());
            return ResponseEntity.ok(userRepository.save(existingUser));
        }
        return ResponseEntity.notFound().build();
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}