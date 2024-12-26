package com.example.DoAnCN.DTO;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class BanksDTO {
    private Long id;
    private String name;
    private String recipientName;
    private String recipientAccountNumber;
    private String recipientBank;
}
