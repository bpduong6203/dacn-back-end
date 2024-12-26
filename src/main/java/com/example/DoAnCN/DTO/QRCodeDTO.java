package com.example.DoAnCN.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class QRCodeDTO {
    private Long id;
    private String qr_code_url;
    private LocalDateTime created_at;
    private Long bank_id;
    private Long payment_details_id;
}
