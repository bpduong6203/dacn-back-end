package com.example.DoAnCN.Service;

import com.example.DoAnCN.DTO.ActivityDTO;
import com.example.DoAnCN.DTO.DTOConverter;
import com.example.DoAnCN.DTO.ItineraryDTO;
import com.example.DoAnCN.Entity.Activity;
import com.example.DoAnCN.Entity.Itinerary;
import com.example.DoAnCN.Repository.ItineraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItineraryService {
    @Autowired
    private ItineraryRepository itineraryRepository;

    @Autowired
    private DTOConverter dtoConverter;

    public Itinerary saveItinerary(Itinerary itinerary){
        return itineraryRepository.save(itinerary);
    }

    public Optional<Itinerary> findById(Long id){
        return itineraryRepository.findById(id);
    }

    public Optional<ItineraryDTO> findByids(Long id){
        return itineraryRepository.findById(id).map(dtoConverter::convertItineraryToDTO);
    }

    public Itinerary updateItinerary(Itinerary itinerary) {
        return itineraryRepository.save(itinerary);
    }

    public void deleteItinerary(Long id) {
        itineraryRepository.deleteById(id);
    }

    public List<Itinerary> findAll() {
        return itineraryRepository.findAll();
    }

}
