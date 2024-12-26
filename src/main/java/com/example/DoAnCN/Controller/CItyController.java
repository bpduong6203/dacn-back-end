package com.example.DoAnCN.Controller;

import com.example.DoAnCN.DTO.CityDTO;
import com.example.DoAnCN.DTO.DTOConverter;
import com.example.DoAnCN.Entity.City;
import com.example.DoAnCN.Service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/city")
public class CItyController {
    @Autowired
    private CityService cityService;

    @Autowired
    private DTOConverter dtoConverter;

    @PostMapping("/create")
    public ResponseEntity<City> create(@RequestBody City city) {
        City save = cityService.saveCity(city);
        return ResponseEntity.ok(save);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody City citis) {
        City city = cityService.updateCity(id)
                .orElseThrow(() -> new RuntimeException("City not found"));
        city.setName(citis.getName());
        city.setProvince(citis.getProvince());
        City update = cityService.saveCity(city);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "City updated successfully");
        response.put("city", dtoConverter.convertCityDTO(update));

        return ResponseEntity.ok(response);
    }

    // Hiển thị thông tin chi tiết
    @GetMapping("/{id}")
    public ResponseEntity<CityDTO> getById(@PathVariable Long id) {
        CityDTO city = cityService.findById(id)
                .orElseThrow(() -> new RuntimeException("City not found"));
        return ResponseEntity.ok(city);
    }

    // Hiển thị danh sách
    @GetMapping("/list")
    public ResponseEntity<List<CityDTO>> getAll() {
        List<CityDTO> cities = cityService.findAll();
        return ResponseEntity.ok(cities);
    }
}



