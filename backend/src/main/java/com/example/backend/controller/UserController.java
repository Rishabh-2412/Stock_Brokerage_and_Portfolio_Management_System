package com.example.backend.controller;

import com.example.backend.dto.UserDTO;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
 
import jakarta.validation.Valid;
import java.util.List;
 
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
 
    @Autowired
    private UserService userService;
 
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        UserDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }
 
    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        UserDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
 
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(userId, userDTO);
        return ResponseEntity.ok(updatedUser);
    }
 
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }
 
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
 
    @PutMapping("/{userId}/kyc-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateKYCStatus(@PathVariable Long userId, @RequestParam String status) {
        UserDTO updatedUser = userService.updateKYCStatus(userId, status);
        return ResponseEntity.ok(updatedUser);
    }
 
    @GetMapping("/{userId}/kyc-status")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> getKYCStatus(@PathVariable Long userId) {
        String status = userService.getKYCStatus(userId);
        return ResponseEntity.ok(status);
    }
 
    @PostMapping("/{userId}/change-password")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> changePassword(@PathVariable Long userId, @RequestParam String oldPassword, @RequestParam String newPassword) {
        userService.changePassword(userId, oldPassword, newPassword);
        return ResponseEntity.ok("Password changed successfully");
    }
}
