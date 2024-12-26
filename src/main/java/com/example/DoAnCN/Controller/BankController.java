package com.example.DoAnCN.Controller;

import com.example.DoAnCN.Entity.Banks;
import com.example.DoAnCN.Service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/banks")
public class BankController {
    @Autowired
    private BankService bankService;

    @PostMapping("/create")
    public ResponseEntity<Banks> create(@RequestBody Banks banks) {
        Banks save = bankService.saveBanks(banks);
        return ResponseEntity.ok(save);
    }

    // Hiển thị danh sách các ngân hàng
    @GetMapping("/list")
    public ResponseEntity<List<Banks>> getAll() {
        List<Banks> banks = bankService.findAll();
        return ResponseEntity.ok(banks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Banks banks) {
        Map<String, Object> response = new HashMap<>();
        try {
            Banks bank = bankService.finById(id)
                    .orElseThrow(() -> new RuntimeException("Banks not found"));
            bank.setName(banks.getName());
            bank.setRecipientName(banks.getRecipientName());
            bank.setRecipientBank(banks.getRecipientBank());
            bank.setRecipientAccountNumber(banks.getRecipientAccountNumber());
            Banks update = bankService.saveBanks(bank);
            response.put("message", "Bank updated successfully");
            response.put("bank", update);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(404).body(response);
        } catch (Exception e) {
            response.put("error", "Failed to update bank: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bankService.deleteBank(id);
        return ResponseEntity.noContent().build();
    }

    // Hiển thị thông tin chi tiết của một ngân hàng
    @GetMapping("/{id}")
    public ResponseEntity<Banks> getById(@PathVariable Long id) {
        Banks bank = bankService.finById(id)
                .orElseThrow(() -> new RuntimeException("Banks not found"));
        return ResponseEntity.ok(bank);
    }


}
