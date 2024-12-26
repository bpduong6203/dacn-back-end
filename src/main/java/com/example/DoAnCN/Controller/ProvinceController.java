package com.example.DoAnCN.Controller;

import com.example.DoAnCN.DTO.ProvinceDTO;
import com.example.DoAnCN.Entity.Banks;
import com.example.DoAnCN.Entity.Province;
import com.example.DoAnCN.Service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/province")
public class ProvinceController {
    @Autowired
    private ProvinceService provinceService;

    @PostMapping("/create")
    public ResponseEntity<Province> create(@RequestBody Province province) {
        Province save = provinceService.saveProvince(province);
        return ResponseEntity.ok(save);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Province provinces) {
        Map<String, Object> response = new HashMap<>();
        try {
            Province province = provinceService.updateById(id)
                    .orElseThrow(() -> new RuntimeException("Provinces not found"));
            province.setName(provinces.getName());
            province.setCountry(provinces.getCountry());
            Province update = provinceService.saveProvince(province);
            response.put("message", "Province updated successfully");
            response.put("province", update);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(404).body(response);
        } catch (Exception e) {
            response.put("error", "Failed to update province: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        provinceService.deleteProvince(id);
//        return ResponseEntity.noContent().build();
//    }

    // Hiển thị thông tin chi tiết
    @GetMapping("/{id}")
    public ResponseEntity<ProvinceDTO> getById(@PathVariable Long id) {
        ProvinceDTO province = provinceService.findById(id)
                .orElseThrow(() -> new RuntimeException("Province not found"));
        return ResponseEntity.ok(province);
    }

    // Hiển thị danh sách
    @GetMapping("/list")
    public ResponseEntity<List<ProvinceDTO>> getAll() {
        List<ProvinceDTO> province = provinceService.findAll();
        return ResponseEntity.ok(province);
    }
}
