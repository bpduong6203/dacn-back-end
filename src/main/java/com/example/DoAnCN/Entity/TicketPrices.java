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
@Table(name = "ticket_prices")
public class TicketPrices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "adult_price")
    private Double adult_price;

    @Column(name = "child_price")
    private Double child_price;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    @PrePersist
    protected void onCreate(){
        created_at = LocalDateTime.now();
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id")
    private Destinations destination;
}
