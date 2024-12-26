package com.example.DoAnCN.Service;

import com.example.DoAnCN.Entity.Banks;
import com.example.DoAnCN.Repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class BankService {
    @Autowired
    private BankRepository bankRepository;

    public Banks saveBanks(Banks banks){
        return bankRepository.save(banks);
    }

    public Optional<Banks> finById(Long id){
        return bankRepository.findById(id);
    }

    public void deleteBank(Long id) {
        bankRepository.deleteById(id);
    }

    public List<Banks> findAll(){
        return bankRepository.findAll();
    }
}
