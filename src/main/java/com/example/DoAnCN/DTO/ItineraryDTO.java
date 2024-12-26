package com.example.DoAnCN.DTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ItineraryDTO {
    private Long id;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private List<ActivityDTO> activities;
    private Long destination_id;
}
