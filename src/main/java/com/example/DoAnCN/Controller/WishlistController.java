package com.example.DoAnCN.Controller;

import com.example.DoAnCN.DTO.WishlistDTO;
import com.example.DoAnCN.Entity.User;
import com.example.DoAnCN.Entity.Wishlist;
import com.example.DoAnCN.Jwt.JwtUtil;
import com.example.DoAnCN.Service.UserService;
import com.example.DoAnCN.Service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wish")
public class WishlistController {
    @Autowired
    private WishlistService wishlistService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Map<String, Object> wishData, @RequestHeader("Authorization") String token) {
        try {
            String username = jwtUtil.extractUsername(token.substring(7));

            // Tạo đối tượng Wishlist từ dữ liệu nhận được
            Wishlist wishlist = new Wishlist();
            Long destId = Long.valueOf((Integer) wishData.get("destinationId"));

            // Lưu wishlist và trả về phản hồi
            Wishlist savedWishlist = wishlistService.saveWish(wishlist, username, destId);
            return ResponseEntity.ok(savedWishlist);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating wishlist: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<WishlistDTO>> getWish(@RequestHeader("Authorization") String token){
        try {
            String username = jwtUtil.extractUsername(token.substring(7));
            User user = userService.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
            List<WishlistDTO> wish = wishlistService.findByUserId(user.getId());
            return ResponseEntity.ok(wish);
        }catch (Exception e){
            return ResponseEntity.status(500).body(List.of());
        }
    }

    @DeleteMapping("/delete/{destinationId}")
    public ResponseEntity<?> deleteWish(@PathVariable Long destinationId, @RequestHeader("Authorization") String token) {
        try {
            String username = jwtUtil.extractUsername(token.substring(7));
            boolean isDeleted = wishlistService.deleteWish(destinationId, username);
            if (isDeleted) {
                return ResponseEntity.ok("Wishlist item deleted successfully.");
            } else {
                return ResponseEntity.status(404).body("Wishlist item not found or you're not authorized to delete it.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting wishlist item: " + e.getMessage());
        }
    }

    @GetMapping("/check/{id}")
    public ResponseEntity<?> checkWish(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            String username = jwtUtil.extractUsername(token.substring(7));
            User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
            boolean isLiked = wishlistService.isWishlistItemLiked(id, user.getId());
            return ResponseEntity.ok(Collections.singletonMap("liked", isLiked));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("liked", false));
        }
    }
}

