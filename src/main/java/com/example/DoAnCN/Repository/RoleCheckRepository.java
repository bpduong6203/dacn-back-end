package com.example.DoAnCN.Repository;

import com.example.DoAnCN.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleCheckRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(String name);
}
