package com.example.DoAnCN.DTO;

import com.example.DoAnCN.Entity.QRCode;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDetailsDTO {
    private Long id;
    private Double amount;
    private LocalDateTime payment_date;
    private String status;
    private String invoiceCode;
    private LocalDateTime created_at;
    private Long payment_method_id;
    private Long user_id;
    private Long booking_id;
}
