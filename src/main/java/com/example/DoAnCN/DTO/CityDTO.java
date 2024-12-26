package com.example.DoAnCN.DTO;

import com.example.DoAnCN.Entity.Province;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CityDTO {
    private Long id;
    private String name;
    private Long province;
}
