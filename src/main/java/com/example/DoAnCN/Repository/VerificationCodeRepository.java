package com.example.DoAnCN.Repository;

import com.example.DoAnCN.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndCode(String email, String code);
    List<User> findByExpiresAtBefore(LocalDateTime now);
    void deleteByExpiresAtBefore(LocalDateTime now);
}
