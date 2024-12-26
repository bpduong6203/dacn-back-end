package com.example.DoAnCN.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payment_details")
public class PaymentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "payment_date")
    private LocalDateTime payment_date;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime created_at;
    @PrePersist
    protected void onCreate(){
        created_at = LocalDateTime.now();
    }

    @Column(name = "invoice_code")
    private String invoiceCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethods payment_mth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "paymentDetails", fetch = FetchType.LAZY)
    private List<QRCode> qrCodes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Bookings booking;
}
