package com.example.DoAnCN.Controller;

import com.example.DoAnCN.Entity.QRCode;
import com.example.DoAnCN.QRcode.QRCodeRequest;
import com.example.DoAnCN.Service.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/qrcode")
public class QRCodeController {

    @Autowired
    private QRCodeService qrCodeService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createQRCode(@RequestBody QRCodeRequest qrCodeRequest) {
        try {
            // Gọi dịch vụ để tạo mã QR với paymentId và bankId
            String qrCodeUrl = qrCodeService.createQRCode(qrCodeRequest.getPaymentId(), qrCodeRequest.getBankId());

            Map<String, String> response = new HashMap<>();
            response.put("qrCodeUrl", qrCodeUrl);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error creating QR code: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<QRCode> qrImg(@PathVariable Long id){
        QRCode qr = qrCodeService.qrCodeImg(id).orElseThrow(() -> new RuntimeException("QR code not found"));
        return ResponseEntity.ok(qr);
    }
}
