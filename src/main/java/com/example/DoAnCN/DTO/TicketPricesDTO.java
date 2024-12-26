package com.example.DoAnCN.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class TicketPricesDTO {
    private Long id;
    private Double adult_price;
    private Double child_price;
    private LocalDateTime created_at;
    private Long destination_id;
}
