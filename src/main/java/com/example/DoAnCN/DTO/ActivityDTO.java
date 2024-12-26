package com.example.DoAnCN.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityDTO {
    private Long id;
    private String activity_name;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
    private Long itinerary_id;
}
