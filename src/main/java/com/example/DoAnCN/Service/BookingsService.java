package com.example.DoAnCN.Service;

import com.example.DoAnCN.DTO.BookingsDTO;
import com.example.DoAnCN.DTO.DTOConverter;
import com.example.DoAnCN.Entity.Bookings;
import com.example.DoAnCN.Entity.Destinations;
import com.example.DoAnCN.Entity.TicketPrices;
import com.example.DoAnCN.Entity.User;
import com.example.DoAnCN.Repository.BookingsRepository;
import com.example.DoAnCN.Repository.DestRepository;
import com.example.DoAnCN.Repository.TicketPricesRepository;
import com.example.DoAnCN.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingsService {
    @Autowired
    private BookingsRepository bookingsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TicketPricesRepository ticketPricesRepository;
    @Autowired
    private DestRepository destRepository;
    @Autowired
    private DTOConverter dtoConverter;

    public Bookings saveBooking(Bookings bookings, String username, Long destId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User bot found"));
        Destinations destinations = destRepository.findById(destId)
                .orElseThrow(()-> new RuntimeException("Destination bot found"));
        bookings.setUser(user);
        bookings.setDestination(destinations);
        return bookingsRepository.save(bookings);
    }

    public Bookings updateBooking(Long id, BookingsDTO bookingsDTO, String username){
        Bookings existingBooking = bookingsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Destinations destination = destRepository.findById(bookingsDTO.getDestination_id())
                .orElseThrow(() -> new RuntimeException("Destination not found"));

        existingBooking.setBooking_date(LocalDateTime.now());
        existingBooking.setAdult_tickets(bookingsDTO.getAdult_tickets());
        existingBooking.setChild_tickets(bookingsDTO.getChild_tickets());
        existingBooking.setStatus(bookingsDTO.getStatus());
        existingBooking.setUser(user);
        existingBooking.setDays(bookingsDTO.getDays());
        existingBooking.setDestination(destination);

        return bookingsRepository.save(existingBooking);
    }

    public Optional<BookingsDTO> findById(Long id) {
        return bookingsRepository.findById(id).map(dtoConverter::convertBookingsDTO);
    }

    public List<BookingsDTO> findAll() {
        return bookingsRepository.findAll().stream().map(dtoConverter::convertBookingsDTO).collect(Collectors.toList());
    }

    public void deleteBooking(Long id) {
        bookingsRepository.deleteById(id);
    }
}
