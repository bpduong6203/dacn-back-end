package com.example.DoAnCN.Service;

import com.example.DoAnCN.DTO.DTOConverter;
import com.example.DoAnCN.DTO.ReviewsDTO;
import com.example.DoAnCN.Entity.Destinations;
import com.example.DoAnCN.Entity.Reviews;
import com.example.DoAnCN.Entity.User;
import com.example.DoAnCN.Repository.DestRepository;
import com.example.DoAnCN.Repository.ReviewsRepository;
import com.example.DoAnCN.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewsService {
    @Autowired
    private ReviewsRepository reviewsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DestRepository destRepository;
    @Autowired
    private DTOConverter dtoConverter;

    public Reviews saveReviews(Reviews reviews, String username, Long destId) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Destinations destination = destRepository.findById(destId).orElseThrow(() -> new RuntimeException("Destination not found"));

        reviews.setUser(user);
        reviews.setDestination(destination);

        return reviewsRepository.save(reviews);
    }

//    public List<ReviewsDTO> findAll(){
//        return reviewsRepository.findAll().stream().map(this::convertDTO).collect(Collectors.toList());
//    }

    public List<ReviewsDTO> findByDestId(Long destinationId){
        return reviewsRepository.findByDestinationId(destinationId).stream().map(dtoConverter::convertReviewsDTO).collect(Collectors.toList());
    }

}
