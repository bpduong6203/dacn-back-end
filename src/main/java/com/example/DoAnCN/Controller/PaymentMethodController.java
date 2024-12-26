package com.example.DoAnCN.Controller;

import com.example.DoAnCN.Service.PaymentMethodService;
import com.example.DoAnCN.Entity.PaymentMethods;
import com.example.DoAnCN.DTO.PaymentMethodsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @PostMapping("/create")
    public ResponseEntity<PaymentMethods> createPaymentMethod(@RequestBody PaymentMethods paymentMethod) {
        PaymentMethods createdPaymentMethod = paymentMethodService.savePayment(paymentMethod);
        return ResponseEntity.ok(createdPaymentMethod);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable Long id) {
        paymentMethodService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodsDTO> getPaymentMethodById(@PathVariable Long id) {
        Optional<PaymentMethodsDTO> paymentMethodDTO = paymentMethodService.findById(id);
        return paymentMethodDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PaymentMethodsDTO>> getAllPaymentMethods() {
        List<PaymentMethodsDTO> paymentMethods = paymentMethodService.findAll();
        return ResponseEntity.ok(paymentMethods);
    }
}
