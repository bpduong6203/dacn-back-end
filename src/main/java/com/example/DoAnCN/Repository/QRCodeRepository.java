package com.example.DoAnCN.Repository;

import com.example.DoAnCN.Entity.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QRCodeRepository extends JpaRepository<QRCode, Long> {
}
