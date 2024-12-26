package com.example.DoAnCN.Controller;

import com.example.DoAnCN.DTO.BookingsDTO;
import com.example.DoAnCN.DTO.DTOConverter;
import com.example.DoAnCN.Entity.Bookings;
import com.example.DoAnCN.Entity.User;
import com.example.DoAnCN.Jwt.JwtUtil;
import com.example.DoAnCN.Service.BookingsService;
import com.example.DoAnCN.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingsController {
    @Autowired
    private BookingsService bookingsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private DTOConverter dtoConverter;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Map<String, Object> bookingData, @RequestHeader("Authorization") String token) {
        String username = jwtUtil.extractUsername(token.substring(7));

        Bookings bookings = new Bookings();

        // Tạo đối tượng Bookings từ dữ liệu yêu cầu
        bookings.setBooking_date(LocalDateTime.parse((String) bookingData.get("booking_date")));
        bookings.setAdult_tickets((Integer) bookingData.get("adult_tickets"));
        bookings.setChild_tickets((Integer) bookingData.get("child_tickets"));
        bookings.setStatus((String) bookingData.get("status"));
        bookings.setDays((Integer) bookingData.get("days"));

        // Lấy các ID từ dữ liệu yêu cầu
        Long destinationId = ((Number) bookingData.get("destination_id")).longValue();

        Bookings savedBooking = bookingsService.saveBooking(bookings, username, destinationId);
        BookingsDTO bookingsDTO = dtoConverter.convertBookingsDTO(savedBooking);
        return ResponseEntity.ok(bookingsDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingsDTO> getById(@PathVariable Long id) {
        BookingsDTO booking = bookingsService.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/list")
    public ResponseEntity<List<BookingsDTO>> getAll() {
        List<BookingsDTO> bookings = bookingsService.findAll();
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody BookingsDTO bookingsDTO, @RequestHeader("Authorization") String token) {
        Map<String, Object> response = new HashMap<>();
        try {
            String username = jwtUtil.extractUsername(token.substring(7));
            Bookings updatedBooking = bookingsService.updateBooking(id, bookingsDTO, username);
            BookingsDTO updatedBookingDTO = dtoConverter.convertBookingsDTO(updatedBooking);
            response.put("message", "Booking updated successfully");
            response.put("booking", updatedBookingDTO);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(404).body(response);
        } catch (Exception e) {
            response.put("error", "Failed to update booking: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookingsService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}


