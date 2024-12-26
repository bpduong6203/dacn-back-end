package com.example.DoAnCN.Controller;

import com.example.DoAnCN.DTO.DTOConverter;
import com.example.DoAnCN.DTO.PaymentDetailsDTO;
import com.example.DoAnCN.Entity.PaymentDetails;
import com.example.DoAnCN.Jwt.JwtUtil;
import com.example.DoAnCN.Repository.PaymentRepository;
import com.example.DoAnCN.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private DTOConverter dtoConverter;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Map<String, Object> paymentData, @RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractUsername(token.substring(7));

        Long paymentMethodId = ((Number) paymentData.get("paymentMethodId")).longValue();
        Long bookingId = ((Number) paymentData.get("bookingId")).longValue();

        PaymentDetails paymentDetails = new PaymentDetails();

        PaymentDetails savedPayment = paymentService.savePayment(paymentDetails, username, paymentMethodId, bookingId);
        PaymentDetailsDTO paymentDetailsDTO = dtoConverter.convertPaymentDetailsDTO(savedPayment);

        return ResponseEntity.ok(paymentDetailsDTO);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updatePaymentDetailStatus(@PathVariable Long id, @RequestBody Map<String, String> status) {
        paymentService.updatePaymentDetailStatus(id, status.get("status"));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDetailsDTO> getById(@PathVariable Long id){
        PaymentDetailsDTO pay = paymentService.findbyId(id)
                .orElseThrow(()-> new RuntimeException("Not found"));
        return ResponseEntity.ok(pay);
    }

    @GetMapping("/list")
    public ResponseEntity<List<PaymentDetailsDTO>> getAll(){
        List<PaymentDetailsDTO> pay = paymentService.findAll();
        return ResponseEntity.ok(pay);
    }
}

