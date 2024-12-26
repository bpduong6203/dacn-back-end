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
@Table(name = "bookings")
public class Bookings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_date")
    private LocalDateTime booking_date;

    @Column(name = "adult_tickets")
    private Integer adult_tickets;

    @Column(name = "child_tickets")
    private Integer child_tickets;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime created_at;
    @PrePersist
    protected void onCreate(){
        created_at = LocalDateTime.now();
    }

    @Column(name = "days")
    private Integer days;

    @OneToMany(mappedBy = "booking", fetch = FetchType.LAZY)
    private List<PaymentDetails> paymentDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id")
    private Destinations destination;
}
