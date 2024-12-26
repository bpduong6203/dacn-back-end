package com.example.DoAnCN.DTO;

import com.example.DoAnCN.Entity.PaymentDetails;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class BookingsDTO {
    private Long id;
    private LocalDateTime booking_date;
    private Integer adult_tickets;
    private Integer child_tickets;
    private String status;
    private Integer days;
    private LocalDateTime created_at;
    private Long user_id;
    private Long destination_id;
    private Long ticket_price_id;
}
