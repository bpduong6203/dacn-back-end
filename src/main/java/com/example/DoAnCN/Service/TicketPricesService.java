package com.example.DoAnCN.Service;

import com.example.DoAnCN.DTO.DTOConverter;
import com.example.DoAnCN.DTO.TicketPricesDTO;
import com.example.DoAnCN.Entity.Destinations;
import com.example.DoAnCN.Entity.TicketPrices;
import com.example.DoAnCN.Repository.DestRepository;
import com.example.DoAnCN.Repository.TicketPricesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TicketPricesService {

    @Autowired
    private TicketPricesRepository ticketPricesRepository;
    @Autowired
    private DestRepository destRepository;
    @Autowired
    private DTOConverter dtoConverter;

    public TicketPrices saveTicketPrices(Map<String, Object> ticketPricesData) {
        // Lấy destination_id từ dữ liệu JSON
        Long destinationId = ((Number) ticketPricesData.get("destination_id")).longValue();

        // Tìm destination từ ID
        Destinations destination = destRepository.findById(destinationId)
                .orElseThrow(() -> new RuntimeException("Destination not found"));

        // Tạo đối tượng TicketPrices từ dữ liệu JSON
        TicketPrices ticketPrices = new TicketPrices();
        ticketPrices.setAdult_price(((Number) ticketPricesData.get("adult_price")).doubleValue());
        ticketPrices.setChild_price(((Number) ticketPricesData.get("child_price")).doubleValue());
        ticketPrices.setDestination(destination);

        // Lưu TicketPrices vào cơ sở dữ liệu
        return ticketPricesRepository.save(ticketPrices);
    }

    public TicketPrices updateTicketPrices(Long id, TicketPrices ticketPricesDetails) {
        TicketPrices ticketPrices = ticketPricesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TicketPrices not found"));

        ticketPrices.setAdult_price(ticketPricesDetails.getAdult_price());
        ticketPrices.setChild_price(ticketPricesDetails.getChild_price());

        // Không thay đổi trường destination nếu nó không có trong dữ liệu gửi lên
        if (ticketPricesDetails.getDestination() != null) {
            Long destinationId = ticketPricesDetails.getDestination().getId();
            Destinations destination = destRepository.findById(destinationId)
                    .orElseThrow(() -> new RuntimeException("Destination not found"));
            ticketPrices.setDestination(destination);
        }

        return ticketPricesRepository.save(ticketPrices);
    }

//    public void deleteTicketPrices(Long id) {
//        ticketPricesRepository.deleteById(id);
//    }

    public Optional<TicketPricesDTO> findById(Long id) {
        return ticketPricesRepository.findById(id)
                .map((dtoConverter::convertTicketPricesDTO));
    }
}



