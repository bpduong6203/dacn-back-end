package com.example.DoAnCN.DTO;

import com.example.DoAnCN.Entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DTOConverter {

    public ActivityDTO convertActivityToDTO(Activity activity) {
        return ActivityDTO.builder()
                .id(activity.getId())
                .activity_name(activity.getActivity_name())
                .start_time(activity.getStart_time())
                .end_time(activity.getEnd_time())
                .itinerary_id(activity.getItinerary().getId())
                .build();
    }

    public ItineraryDTO convertItineraryToDTO(Itinerary itinerary) {
        List<ActivityDTO> activityDTOs = itinerary.getActivities().stream()
                .map(this::convertActivityToDTO)
                .collect(Collectors.toList());

        return ItineraryDTO.builder()
                .id(itinerary.getId())
                .start_date(itinerary.getStart_date())
                .end_date(itinerary.getEnd_date())
                .activities(activityDTOs)
                .destination_id(itinerary.getId())
                .build();
    }

    public TicketPricesDTO convertTicketPricesDTO(TicketPrices ticketPrices){
        return TicketPricesDTO.builder()
                .id(ticketPrices.getId())
                .adult_price(ticketPrices.getAdult_price())
                .child_price(ticketPrices.getChild_price())
                .created_at(ticketPrices.getCreated_at())
                .destination_id(ticketPrices.getDestination() != null ? ticketPrices.getDestination().getId() : null)
                .build();
    }

    //phương thức riêng chỉ dành cho destination
    public CityDTOs convertCityToDTO(City city) {
        ProvinceDTO provinceDTO = null;
        if (city.getProvince() != null) {
            provinceDTO = convertProvinceToDTO(city.getProvince());
        }
        return CityDTOs.builder()
                .id(city.getId())
                .name(city.getName())
                .province(provinceDTO)
                .build();
    }

    public ProvinceDTO convertProvinceToDTO(Province province) {
        return ProvinceDTO.builder()
                .id(province.getId())
                .name(province.getName())
                .country(province.getCountry())
                .build();
    }

    public DestinationsDTO convertDestinationsDTO(Destinations dest) {
        List<ItineraryDTO> itineraryDTOs = dest.getItineraries().stream()
                .map(this::convertItineraryToDTO)
                .collect(Collectors.toList());

        TicketPricesDTO ticketPricesDTO = null;
        if (dest.getTicketPrice() != null){
            ticketPricesDTO = convertTicketPricesDTO(dest.getTicketPrice());
        }

        CityDTOs cityDTOs = null;
        if(dest.getCity() != null){
            cityDTOs = convertCityToDTO(dest.getCity());
        }

        List<ReviewsDTO> reviewsDTOS = dest.getReviewsList().stream()
                .map(this::convertReviewsDTO).collect(Collectors.toList());

        return DestinationsDTO.builder()
                .id(dest.getId())
                .name(dest.getName())
                .description(dest.getDescription())
                .location(dest.getLocation())
                .created_at(dest.getCreated_at())
                .destinationImages(dest.getDestinationImages().stream()
                        .map(img -> DestinationImagesDTO.builder()
                                .id(img.getId())
                                .image_url(img.getImage_url())
                                .destination_id(img.getDestination().getId())
                                .build())
                        .collect(Collectors.toList()))
                .descriptionFile(dest.getDescriptionFile() != null ? DescriptionFile.builder()
                        .id(dest.getDescriptionFile().getId())
                        .fileName(dest.getDescriptionFile().getFileName())
                        .filePath(dest.getDescriptionFile().getFilePath())
                        .build() : null)
                .type(determineType(dest))
                .city(cityDTOs)
                .itineraries(itineraryDTOs)
                .ticketPrice(ticketPricesDTO)
                .reviewsList(reviewsDTOS)
                .build();
    }

    public String determineType(Destinations destination) {
        if ("Việt Nam".equalsIgnoreCase(destination.getCity().getProvince().getCountry())) {
            return "DOMESTIC";
        } else {
            return "INTERNATIONAL";
        }
    }

    public CityDTO convertCityDTO(City city){
        return CityDTO.builder()
                .id(city.getId())
                .name(city.getName())
                .province(city.getProvince().getId())
                .build();
    }

    public BookingsDTO convertBookingsDTO(Bookings bookings){
        return BookingsDTO.builder()
                .id(bookings.getId())
                .booking_date(bookings.getBooking_date())
                .adult_tickets(bookings.getAdult_tickets())
                .child_tickets(bookings.getChild_tickets())
                .status(bookings.getStatus())
                .days(bookings.getDays())
                .created_at(bookings.getCreated_at())
                .user_id(bookings.getUser().getId())
                .destination_id(bookings.getDestination().getId())
                .build();
    }

    public PaymentMethodsDTO convertPaymentMethodsDTO(PaymentMethods pmt){
        return PaymentMethodsDTO.builder()
                .id(pmt.getId())
                .method_name(pmt.getMethod_name())
                .build();
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

    public UserDTO convertUserDTO(User user){
        return UserDTO.builder()
                .id(user.getId())
                .fullname(user.getFullname())
                .build();
    }

    public ReviewsDTO convertReviewsDTO(Reviews reviews){

        UserDTO userDTO = null;
        if (reviews.getUser() != null){
            userDTO = convertUserDTO(reviews.getUser());
        }

        return ReviewsDTO.builder()
                .id(reviews.getId())
                .comment(reviews.getComment())
                .created_at(reviews.getCreated_at())
                .rating(reviews.getRating())
                .user(userDTO)
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

}
