package com.example.DoAnCN.Repository;

import com.example.DoAnCN.Entity.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentDetails, Long> {
    boolean existsByInvoiceCode(String invoiceCode);
}
