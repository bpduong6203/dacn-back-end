package com.example.DoAnCN.Controller;

import com.example.DoAnCN.Auth.AuthenticationRequest;
import com.example.DoAnCN.DTO.UserDTO;
import com.example.DoAnCN.Entity.Role;
import com.example.DoAnCN.Entity.User;
import com.example.DoAnCN.Jwt.JwtUtil;
import com.example.DoAnCN.Repository.RoleRepository;
import com.example.DoAnCN.Repository.UserRepository;
import com.example.DoAnCN.Service.EmailService;
import com.example.DoAnCN.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
            final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails);
            UserDTO user = userService.getUserInfo("Bearer " + jwt);

            if (user == null || user.getRoles() == null) {
                throw new RuntimeException("User roles not found");
            }

            response.put("success", true);
            response.put("message", "Login successful");
            response.put("jwt", jwt);
            response.put("role", user.getRoles());
            response.put("email", user.getEmail());
            response.put("userId", user.getId());
            response.put("fullname", user.getFullname());
            response.put("phone", user.getPhone());

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            response.put("success", false);
            response.put("message", "Sai tên người dùng hoặc mật khẩu");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (AuthenticationException e) {
            response.put("success", false);
            response.put("message", "Quyền truy cập bị từ chối");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Kiểm tra xem tên người dùng và mật khẩu có bị thiếu không
            if (user.getUsername() == null || user.getUsername().isEmpty() ||
                    user.getPassword() == null || user.getPassword().isEmpty() ||
                    user.getEmail() == null || user.getEmail().isEmpty()) {
                response.put("success", false);
                response.put("message", "Username, email, and password are required.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 400 Bad Request
            }

            // Kiểm tra xem tên người dùng đã tồn tại hay chưa
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                response.put("success", false);
                response.put("message", "Username is already taken.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // 409 Conflict
            }

            // Kiểm tra xem email đã tồn tại hay chưa
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                response.put("success", false);
                response.put("message", "Email is already registered.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // 409 Conflict
            }

            // Mã hóa mật khẩu
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // Lấy vai trò người dùng
            Role userRole = roleRepository.findByName("USER");
            if (userRole == null) {
                response.put("success", false);
                response.put("message", "User role not found.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 500 Internal Server Error
            }

            // Thêm vai trò cho người dùng
            user.getRoles().add(userRole);

            // Lưu người dùng
            userRepository.save(user);

            // Gửi email chào mừng
            emailService.sendWelcomeEmail(user);

            response.put("success", true);
            response.put("message", "User registered successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error registering user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 500 Internal Server Error
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        jwtUtil.invalidateToken(token);
        SecurityContextHolder.clearContext();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required");
        }
        try {
            emailService.sendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent successfully");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with email " + email + " not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send verification code: " + e.getMessage());
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        if (email == null || email.isEmpty() || code == null || code.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email and code are required");
        }
        try {
            boolean verified = emailService.verifyCode(email, code);
            if (verified) {
                User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
                String token = jwtUtil.generateToken(user);
                Map<String, String> response = new HashMap<>();
                response.put("message", "Verification successful");
                response.put("token", token);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification code");
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with email " + email + " not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to verify code: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request, @RequestHeader("Authorization") String token) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");
        if ( email == null || email.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password is required");
        }
        try {
            String tokenEmail = jwtUtil.extractUsername(token.substring(7));
            User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            if (!tokenEmail.equals(user.getUsername())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token for the given email");
            }

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return ResponseEntity.ok("Password reset successfully");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to reset password: " + e.getMessage());
        }
    }

    // đăng nhập google rất rất nhiều bug 403
    @PostMapping("/google")
    public ResponseEntity<Map<String, Object>> authenticateWithGoogle(@RequestBody Map<String, String> googleData) {
        String provider = "google";
        String providerUserId = googleData.get("providerUserId");
        String email = googleData.get("email");
        String fullname = googleData.get("fullname");

        Optional<User> optionalUser = userRepository.findByProviderAndProviderId(provider, providerUserId);
        User user;
        if (optionalUser.isEmpty()) {
            user = User.builder()
                    .email(email)
                    .fullname(fullname)
                    .provider(provider)
                    .providerId(providerUserId)
                    .build();
            Role userRole = roleRepository.findByName("USER");
            user.getRoles().add(userRole);
            user = userRepository.save(user);
        }else {
            user = optionalUser.get();
        }


        final UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Login successful");
        response.put("jwt", jwt);
        response.put("name", user.getFullname());
        response.put("email", user.getEmail());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        boolean isValid = jwtUtil.isTokenValid(token);
        return ResponseEntity.ok(Map.of("valid", isValid));
    }
}



//@PostMapping("/google")
//public ResponseEntity<Map<String, Object>> authenticateWithGoogle(@RequestBody Map<String, String> googleData) {
//    String token = googleData.get("token");
//
//    // Xác thực token của Google
//    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
//            .setAudience(Collections.singletonList(CLIENT_ID))
//            .build();
//
//    GoogleIdToken idToken = null;
//    try {
//        idToken = verifier.verify(token);
//    } catch (GeneralSecurityException | IOException e) {
//        e.printStackTrace();
//    }
//
//    if (idToken != null) {
//        Payload payload = idToken.getPayload();
//        String email = payload.getEmail();
//        String fullname = (String) payload.get("name");
//
//        Optional<User> optionalUser = userRepository.findByEmail(email);
//        User user;
//        if (optionalUser.isEmpty()) {
//            user = User.builder()
//                    .email(email)
//                    .fullname(fullname)
//                    .provider("google")
//                    .providerId(payload.getSubject())
//                    .build();
//            Role userRole = roleRepository.findByName("USER");
//            user.getRoles().add(userRole);
//            user = userRepository.save(user);
//        } else {
//            user = optionalUser.get();
//        }
//
//        final UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
//        final String jwt = jwtUtil.generateToken(userDetails);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", true);
//        response.put("message", "Login successful");
//        response.put("jwt", jwt);
//        response.put("name", user.getFullname());
//        response.put("email", user.getEmail());
//        response.put("role", user.getRoles()); // Bao gồm role trong phản hồi
//
//        return ResponseEntity.ok(response);
//    } else {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Invalid Google token"));
//    }
//}
