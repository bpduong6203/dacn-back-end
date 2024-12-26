package com.example.DoAnCN.DTO;

import com.example.DoAnCN.Entity.City;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ProvinceDTO {
    private Long id;
    private String name;
    private String country;
    private List<City> cities;
}
