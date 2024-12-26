package com.example.DoAnCN.Service;

import com.example.DoAnCN.DTO.PaymentDetailsDTO;
import com.example.DoAnCN.DTO.RoleDTO;
import com.example.DoAnCN.DTO.UserDTO;
import com.example.DoAnCN.DTO.WishlistDTO;
import com.example.DoAnCN.Entity.PaymentDetails;
import com.example.DoAnCN.Entity.User;
import com.example.DoAnCN.Entity.Wishlist;
import com.example.DoAnCN.Jwt.JwtUtil;
import com.example.DoAnCN.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public User updateUser(String username, UserDTO upUserDTO) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullname(upUserDTO.getFullname());
        user.setAddress(upUserDTO.getAddress());
        user.setDateYear(upUserDTO.getDateYear());
        user.setAvata(upUserDTO.getAvata());
        user.setPhone(upUserDTO.getPhone());

        return userRepository.save(user);
    }

    public PaymentDetailsDTO convertPaymentDetailsDTO(PaymentDetails paymentDetails){
        return PaymentDetailsDTO.builder()
                .id(paymentDetails.getId())
                .amount(paymentDetails.getAmount())
                .payment_date(paymentDetails.getPayment_date())
                .payment_method_id(paymentDetails.getPayment_mth().getId())
                .booking_id(paymentDetails.getBooking().getId())
                .user_id(paymentDetails.getUser().getId())
                .created_at(paymentDetails.getCreated_at())
                .status(paymentDetails.getStatus())
                .invoiceCode(paymentDetails.getInvoiceCode())
                .build();
    }

    public WishlistDTO convertWishlistDTO(Wishlist wishlist){
        return WishlistDTO.builder()
                .id(wishlist.getId())
                .created_at(wishlist.getCreated_at())
                .destination_id(wishlist.getDestination().getId())
                .user_id(wishlist.getUser().getId())
                .build();
    }

    public UserDTO getUserInfo(String authHearder) {
        if(authHearder != null && authHearder.startsWith("Bearer ")) {
            String jwt = authHearder.substring(7);
            String username = jwtUtil.extractUsername(jwt);
            Optional<User> userOptional = userRepository.findByUsername(username);
            if(userOptional.isPresent()){
                User user = userOptional.get();

                List<PaymentDetailsDTO> paymentDetailsDTOS = user.getPaymentDetails()
                        .stream().map(this::convertPaymentDetailsDTO).collect(Collectors.toList());

                List<WishlistDTO> wishlistDTOS = user.getWishlistList()
                        .stream().map(this::convertWishlistDTO).collect(Collectors.toList());

                return UserDTO.builder()
                        .id(user.getId()).fullname(user.getFullname())
                        .username(user.getUsername()).email(user.getEmail())
                        .provider(user.getProvider()) .providerId(user.getProviderId())
                        .address(user.getAddress()) .dateYear(user.getDateYear())
                        .avata(user.getAvata()) .phone(user.getPhone())
                        .roles(user.getRoles().stream().map(role -> new RoleDTO(role.getId(), role.getName())).collect(Collectors.toSet()))
                        .paymentDetails(paymentDetailsDTOS)
                        .wishlistList(wishlistDTOS)
                        .build();
            }
        }
        return null;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<UserDTO> getAllUsers(String auth) {
        if (auth != null && auth.startsWith("Bearer ")) {
            String jwt = auth.substring(7);
            String username = jwtUtil.extractUsername(jwt);
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                List<User> users = userRepository.findAll();
                return users.stream().map(user -> UserDTO.builder()
                        .id(user.getId()) .fullname(user.getFullname())
                        .username(user.getUsername()) .email(user.getEmail())
                        .provider(user.getProvider()) .providerId(user.getProviderId())
                        .address(user.getAddress()) .dateYear(user.getDateYear())
                        .avata(user.getAvata()) .phone(user.getPhone())
                        .roles(user.getRoles().stream().map(role -> new RoleDTO(role.getId(), role.getName())).collect(Collectors.toSet()))
                        .build()
                ).collect(Collectors.toList());
            }
        }
        return null;
    }
}
