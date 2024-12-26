package com.example.DoAnCN.Repository;

import com.example.DoAnCN.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional <User> findByUsername(String username);
    Optional <User> findByProviderAndProviderId(String provider, String providerId);
    Optional <User> findByEmail(String email);
}
