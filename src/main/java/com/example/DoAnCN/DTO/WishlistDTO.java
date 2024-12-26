package com.example.DoAnCN.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistDTO {
    private Long id;
    private LocalDateTime created_at;
    private Long user_id;
    private Long destination_id;
}
