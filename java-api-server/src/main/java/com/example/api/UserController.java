package com.example.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class UserController {

    // This is our simple, in-memory "database".
    private final Map<String, Map<String, User>> studentDatabases = new ConcurrentHashMap<>();

    // Endpoint 1: /create_user/{student_id}
    @PostMapping("/comp2000/coursework/create_user/{student_id}")
    public ResponseEntity<Map<String, String>> createUser(
            @PathVariable String student_id,
            @RequestBody User newUser) {

        studentDatabases.putIfAbsent(student_id, new ConcurrentHashMap<>());
        Map<String, User> userDb = studentDatabases.get(student_id);

        if (userDb.containsKey(newUser.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("detail", "Username already exists"));
        }

        userDb.put(newUser.getUsername(), newUser);
        System.out.println("User created: " + newUser.getUsername() + " for student " + student_id);
        return ResponseEntity.ok(Map.of("message", "User created successfully"));
    }

    // Endpoint 4: /read_user/{student_id}/{username}
    // CORRECTED METHOD SIGNATURE HERE
    @GetMapping("/comp2000/coursework/read_user/{student_id}/{username}")
    public ResponseEntity<?> readUser(
            @PathVariable String student_id,
            @PathVariable String username) {

        Map<String, User> userDb = studentDatabases.get(student_id);

        if (userDb == null || !userDb.containsKey(username)) {
            System.out.println("User not found: " + username + " for student " + student_id);
            // This error response is now valid
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("detail", "User not found"));
        }

        User user = userDb.get(username);
        System.out.println("User read: " + user.getUsername());
        
        Map<String, User> response = new HashMap<>();
        response.put("user", user);

        return ResponseEntity.ok(response);
    }
}