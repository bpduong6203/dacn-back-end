package com.example.DoAnCN.DTO;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class DestinationImagesDTO {
    private Long id;
    private String image_url;
    private Long destination_id;
}
