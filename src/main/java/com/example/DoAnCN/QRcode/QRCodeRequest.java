package com.example.DoAnCN.QRcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class QRCodeRequest {
    private Long paymentId;
    private Long bankId;
}

