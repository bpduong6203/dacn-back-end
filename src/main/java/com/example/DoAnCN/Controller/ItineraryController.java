package com.example.DoAnCN.Controller;

import com.example.DoAnCN.DTO.ItineraryDTO;
import com.example.DoAnCN.Entity.Activity;
import com.example.DoAnCN.Entity.Destinations;
import com.example.DoAnCN.Entity.Itinerary;
import com.example.DoAnCN.Service.DestService;
import com.example.DoAnCN.Service.ItineraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/itineraries")
public class ItineraryController {
    @Autowired
    private ItineraryService itineraryService;
    @Autowired
    private DestService destService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> create(@RequestBody Itinerary itinerary) {
        Map<String, String> response = new HashMap<>();
        try {
            Destinations destination = destService.DestById(itinerary.getDestination().getId())
                    .orElseThrow(() -> new RuntimeException("Destination not found"));
            itinerary.setDestination(destination);

            for (Activity activity : itinerary.getActivities()) {
                activity.setItinerary(itinerary);
            }
            itineraryService.saveItinerary(itinerary);
            response.put("message", "Hành trình đã được lưu thành công.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("message", "Điểm đến không tồn tại.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("message", "Có lỗi xảy ra trong quá trình lưu hành trình.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Itinerary itineraryDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            Itinerary itinerary = itineraryService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Itinerary not found"));

            itinerary.setStart_date(itineraryDetails.getStart_date());
            itinerary.setEnd_date(itineraryDetails.getEnd_date());

            Itinerary updatedItinerary = itineraryService.saveItinerary(itinerary);
            response.put("message", "Itinerary updated successfully");
            response.put("itinerary", updatedItinerary);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(404).body(response);
        } catch (Exception e) {
            response.put("error", "Failed to update itinerary: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) {
        itineraryService.deleteItinerary(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItineraryDTO> getById(@PathVariable Long id){
        ItineraryDTO itinerary = itineraryService.findByids(id)
                .orElseThrow(() -> new RuntimeException("Itinerary not found"));
        return ResponseEntity.ok(itinerary);
    }
}
