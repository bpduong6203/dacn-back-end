package com.example.DoAnCN.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "qr_code")
public class QRCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "qr_code_url")
    private String qr_code_url;

    @Column(name = "created_at")
    private LocalDateTime created_at;
    @PrePersist
    protected void onCreate(){
        created_at = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Banks bank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_details_id")
    private PaymentDetails paymentDetails;
}
