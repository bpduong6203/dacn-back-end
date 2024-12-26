package com.example.DoAnCN.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "destinations")
public class Destinations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    @PrePersist
    protected void onCreate(){
        created_at = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DestinationImages> destinationImages;

    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reviews> reviewsList;

    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Wishlist> wishlistList;

    @OneToMany(mappedBy = "destination", fetch = FetchType.LAZY)
    private List<Bookings> bookingsList;

    @OneToOne(mappedBy = "destination", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private TicketPrices ticketPrice;

    @OneToOne(mappedBy = "destination", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DescriptionFile descriptionFile;

    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Itinerary> itineraries;
}
