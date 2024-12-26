package com.example.DoAnCN.DTO;

import com.example.DoAnCN.Entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewsDTO {
    private Long id;
    private Integer rating;
    private String comment;
    private LocalDateTime created_at;
    private UserDTO user;
}
