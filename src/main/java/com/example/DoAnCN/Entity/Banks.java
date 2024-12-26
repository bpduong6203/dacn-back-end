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
@Table(name = "banks")
public class Banks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "recipient_name")
    private String recipientName;

    @Column(name = "recipient_account_number")
    private String recipientAccountNumber;

    @Column(name = "recipient_bank")
    private String recipientBank;

    @Column(name = "ma_dinh_danh")
    private String maDinhDanh;

    @Column(name = "bank_id")
    private String bankId;

    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QRCode> qrCodes;
}
