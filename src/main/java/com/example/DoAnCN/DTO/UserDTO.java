package com.example.DoAnCN.DTO;

import com.example.DoAnCN.Entity.PaymentDetails;
import com.example.DoAnCN.Entity.Wishlist;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String fullname;
    private String username;
    private String email;
    private String provider;
    private String providerId;
    private String address;
    private LocalDateTime dateYear;
    private String avata;
    private String phone;
    Set<RoleDTO> roles;
    private List<PaymentDetailsDTO> paymentDetails;
    private List<WishlistDTO> wishlistList;
}
