package com.example.DoAnCN.Controller;

import com.example.DoAnCN.DTO.RoleDTO;
import com.example.DoAnCN.DTO.UserDTO;
import com.example.DoAnCN.Entity.User;
import com.example.DoAnCN.Jwt.JwtUtil;
import com.example.DoAnCN.Repository.UserRepository;
import com.example.DoAnCN.Service.EmailService;
import com.example.DoAnCN.Service.RoleService;
import com.example.DoAnCN.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jdk.jfr.Frequency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/user")
    public ResponseEntity<UserDTO> getUserInfo(@RequestHeader("Authorization") String authHeader, HttpServletRequest request) {

        Exception jwtError = (Exception) request.getAttribute("jwtError");
        if(jwtError != null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        UserDTO userDTO = userService.getUserInfo(authHeader);
        if (userDTO != null){
            return ResponseEntity.ok(userDTO);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/list-user")
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestHeader("Authorization") String auth) {
        List<UserDTO> userDTOS = userService.getAllUsers(auth);
        if(userDTOS != null){
            return ResponseEntity.ok(userDTOS);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO upUserDTO, @RequestHeader("Authorization") String token) {
        Map<String, String> response = new HashMap<>();
        try{
            String username = jwtUtil.extractUsername(token.substring(7));
            userService.updateUser(username, upUserDTO);
            response.put("message", "User updated successfully");
            return ResponseEntity.ok(response);
        }catch (RuntimeException e){
            response.put("error", e.getMessage());
            return ResponseEntity.status(404).body(response);
        }catch (Exception e){
            response.put("error", "Failed to update user: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // Role

    @GetMapping("/roles")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.getAll();
        return ResponseEntity.ok(roles);
    }

    @PutMapping("/{userId}/roles")
    public ResponseEntity<?> updateRoleId(@PathVariable Long userId, @RequestBody List<RoleDTO> roles, @RequestHeader("Authorization") String token, HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();
        Exception jwtError = (Exception) request.getAttribute("jwtError");
        if (jwtError != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        try {
            roleService.updateUserRole(userId, roles);
            response.put("message", "Role updated successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(404).body(response);
        } catch (Exception e) {
            response.put("error", "Failed to update role: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PutMapping("/update-user/{username}")
    public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody UserDTO upUserDTO, @RequestHeader("Authorization") String token) {
        Map<String, String> response = new HashMap<>();
        try {
            String requestingUser = jwtUtil.extractUsername(token.substring(7));
            userService.updateUser(username, upUserDTO);
            response.put("message", "Role updated successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(404).body(response);
        } catch (Exception e) {
            response.put("error", "Failed to update role: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}

















