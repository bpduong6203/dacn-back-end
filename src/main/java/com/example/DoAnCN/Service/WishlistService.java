package com.example.DoAnCN.Service;

import com.example.DoAnCN.DTO.DTOConverter;
import com.example.DoAnCN.DTO.WishlistDTO;
import com.example.DoAnCN.Entity.Destinations;
import com.example.DoAnCN.Entity.User;
import com.example.DoAnCN.Entity.Wishlist;
import com.example.DoAnCN.Repository.DestRepository;
import com.example.DoAnCN.Repository.UserRepository;
import com.example.DoAnCN.Repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WishlistService {
    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DestRepository destRepository;
    @Autowired
    private DTOConverter dtoConverter;

    public Wishlist saveWish(Wishlist wishlist, String username, Long destId){
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User bot found"));
        Destinations destinations = destRepository.findById(destId)
                .orElseThrow(()-> new RuntimeException("Destination bot found"));
        wishlist.setUser(user);
        wishlist.setDestination(destinations);
        return wishlistRepository.save(wishlist);
    }

    public List<WishlistDTO> findByUserId(Long userId){
        return wishlistRepository.findByUserId(userId).stream().map(dtoConverter::convertWishlistDTO).collect(Collectors.toList());
    }

    public boolean deleteWish(Long destinationId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Optional<Wishlist> wishlist = wishlistRepository.findByUserIdAndDestinationId(user.getId(), destinationId);
        if (wishlist.isPresent()) {
            wishlistRepository.deleteById(wishlist.get().getId());
            return true;
        }
        return false;
    }

    public boolean isWishlistItemLiked(Long destinationId, Long userId) {
        return wishlistRepository.findByUserIdAndDestinationId(userId, destinationId).isPresent();
    }

//    public Optional<WishlistDTO> findById(Long id){
//        return wishlistRepository.findById(id).map(this::convertDTO);
//    }
//
//    public List<WishlistDTO> findAll(){
//        return wishlistRepository.findAll().stream().map(this::convertDTO).collect(Collectors.toList());
//    }
}
