package com.example.DoAnCN.DTO;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CityDTOs {
    private Long id;
    private String name;
    private ProvinceDTO province;
}
