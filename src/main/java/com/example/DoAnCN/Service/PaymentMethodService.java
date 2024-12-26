package com.example.DoAnCN.Service;

import com.example.DoAnCN.DTO.DTOConverter;
import com.example.DoAnCN.DTO.PaymentMethodsDTO;
import com.example.DoAnCN.Entity.PaymentMethods;
import com.example.DoAnCN.Repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentMethodService {
    @Autowired
    private PaymentMethodRepository paymentRepository;
    @Autowired
    private DTOConverter dtoConverter;

    public PaymentMethods savePayment(PaymentMethods s){
        return paymentRepository.save(s);
    }

    public void deletePayment(Long id){
        paymentRepository.deleteById(id);
    }

    public Optional<PaymentMethodsDTO> findById (Long id){
        return paymentRepository.findById(id)
                .map((dtoConverter::convertPaymentMethodsDTO));
    }

    public List<PaymentMethodsDTO> findAll(){
        return paymentRepository.findAll().stream().map(dtoConverter::convertPaymentMethodsDTO).collect(Collectors.toList());
    }
}
