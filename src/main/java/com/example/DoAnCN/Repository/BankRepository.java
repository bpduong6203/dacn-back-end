package com.example.DoAnCN.Repository;

import com.example.DoAnCN.Entity.Banks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Banks, Long> {
}
