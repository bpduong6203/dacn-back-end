package com.example.DoAnCN.Controller;

import com.example.DoAnCN.DTO.ReviewsDTO;
import com.example.DoAnCN.Entity.Reviews;
import com.example.DoAnCN.Jwt.JwtUtil;
import com.example.DoAnCN.Service.ReviewsService;
import com.example.DoAnCN.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/review")
public class ReviewsController {
    @Autowired
    private ReviewsService reviewsService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<?> createReview(@RequestBody Map<String, Object> reviewData, @RequestHeader("Authorization") String token) {
        try {
            String username = jwtUtil.extractUsername(token.substring(7));

            Reviews reviews = new Reviews();
            if (reviewData.containsKey("rating")) {
                reviews.setRating((Integer) reviewData.get("rating"));
            }
            if (reviewData.containsKey("comment")) {
                reviews.setComment((String) reviewData.get("comment"));
            }

            Long destId = Long.valueOf((Integer) reviewData.get("destinationId"));

            Reviews save = reviewsService.saveReviews(reviews, username, destId);
            return ResponseEntity.ok(save);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating reviews: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ReviewsDTO>> getAll(@PathVariable Long id) {
        List<ReviewsDTO> reviews = reviewsService.findByDestId(id);
        return ResponseEntity.ok(reviews);
    }
}

