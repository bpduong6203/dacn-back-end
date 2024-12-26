package com.example.DoAnCN.DTO;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodsDTO {
    private Long id;
    private String method_name;
}
