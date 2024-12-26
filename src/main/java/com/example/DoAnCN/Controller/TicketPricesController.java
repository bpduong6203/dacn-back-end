package com.example.DoAnCN.Controller;

import com.example.DoAnCN.DTO.DTOConverter;
import com.example.DoAnCN.DTO.TicketPricesDTO;
import com.example.DoAnCN.Entity.TicketPrices;
import com.example.DoAnCN.Service.TicketPricesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ticketprices")
public class TicketPricesController {

    @Autowired
    private TicketPricesService ticketPricesService;
    @Autowired
    private DTOConverter dtoConverter;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Map<String, Object> ticketPricesData) {
        TicketPrices savedTicketPrices = ticketPricesService.saveTicketPrices(ticketPricesData);
        TicketPricesDTO save = dtoConverter.convertTicketPricesDTO(savedTicketPrices);
        return ResponseEntity.ok(save);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody TicketPrices ticketPricesDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            TicketPrices update = ticketPricesService.updateTicketPrices(id, ticketPricesDetails);
            response.put("message", "Ticket price updated successfully");
            response.put("ticketPrice", update);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(404).body(response);
        } catch (Exception e) {
            response.put("error", "Failed to update ticket price: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        ticketPricesService.deleteTicketPrices(id);
//        return ResponseEntity.noContent().build();
//    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketPricesDTO> getById(@PathVariable Long id) {
        TicketPricesDTO ticketPrices = ticketPricesService.findById(id)
                .orElseThrow(() -> new RuntimeException("TicketPrices not found"));
        return ResponseEntity.ok(ticketPrices);
    }
}


