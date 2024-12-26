package com.example.DoAnCN.DTO;

import com.example.DoAnCN.Entity.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class DestinationsDTO {
    private Long id;
    private String name;
    private String description;
    private DescriptionFile descriptionFile;
    private String location;
    private LocalDateTime created_at;
    private List<DestinationImagesDTO> destinationImages;
    private String type;
    private CityDTOs city;
    private List<ItineraryDTO> itineraries;
    private TicketPricesDTO ticketPrice;
    private List<ReviewsDTO> reviewsList;
}
