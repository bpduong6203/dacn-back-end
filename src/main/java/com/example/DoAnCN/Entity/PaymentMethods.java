package com.example.DoAnCN.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payment_methods")
public class PaymentMethods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "method_name")
    private String method_name;

    @OneToMany(mappedBy = "payment_mth", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PaymentDetails> paymentDetails;
}
