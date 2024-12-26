package com.example.DoAnCN.Service;

import com.example.DoAnCN.DTO.DTOConverter;
import com.example.DoAnCN.DTO.PaymentDetailsDTO;
import com.example.DoAnCN.DTO.QRCodeDTO;
import com.example.DoAnCN.Entity.*;
import com.example.DoAnCN.Repository.*;
import com.example.DoAnCN.Security.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private BookingsRepository bookingsRepository;
    @Autowired
    private TicketPricesRepository ticketRepository;
    @Autowired
    private DTOConverter dtoConverter;

    public PaymentDetails savePayment(PaymentDetails paymentDetails, String username, Long paymentMethodId, Long bookingId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PaymentMethods paymentMethod = paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new RuntimeException("Payment Method not found"));

        Bookings booking = bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        Destinations destination = booking.getDestination();

        TicketPrices ticketPrices = ticketRepository.findByDestinationId(destination.getId())
                .orElseThrow(() -> new RuntimeException("No ticket prices found for the given destination"));

        double totalPrice = (booking.getAdult_tickets() * ticketPrices.getAdult_price() +
                booking.getChild_tickets() * ticketPrices.getChild_price()) * booking.getDays();

        // Tạo mã hóa đơn sử dụng UUID
        String invoiceCode;
        do {
            invoiceCode = UUIDUtil.generateShortUUID();
        } while (paymentRepository.existsByInvoiceCode(invoiceCode));

        // Thiết lập thông tin thanh toán
        paymentDetails.setUser(user);
        paymentDetails.setPayment_mth(paymentMethod);
        paymentDetails.setBooking(booking);
        paymentDetails.setAmount(totalPrice);
        paymentDetails.setPayment_date(LocalDateTime.now());
        paymentDetails.setStatus("PENDING");
        paymentDetails.setInvoiceCode(invoiceCode);

        // Lưu thông tin thanh toán
        PaymentDetails savedPaymentDetail = paymentRepository.save(paymentDetails);

        // Kiểm tra và cập nhật trạng thái booking
        updateBookingStatusAfterPayment(savedPaymentDetail);

        return savedPaymentDetail;
    }

    public void updatePaymentDetailStatus(Long paymentDetailId, String status) {
        PaymentDetails paymentDetail = paymentRepository.findById(paymentDetailId)
                .orElseThrow(() -> new RuntimeException("PaymentDetail not found"));

        paymentDetail.setStatus(status);
        paymentRepository.save(paymentDetail);

        // Cập nhật trạng thái booking liên quan
        updateBookingStatusAfterPayment(paymentDetail);
    }

    private void updateBookingStatusAfterPayment(PaymentDetails paymentDetail) {
        if ("COMPLETED".equals(paymentDetail.getStatus())) {
            Bookings booking = paymentDetail.getBooking();
            booking.setStatus("CONFIRMED");
            bookingsRepository.save(booking);
        }
    }

    public Optional<PaymentDetailsDTO> findbyId(Long id){
        return paymentRepository.findById(id).map(dtoConverter::convertPaymentDetailsDTO);
    }

    public List<PaymentDetailsDTO> findAll(){
        return paymentRepository.findAll().stream().map(dtoConverter::convertPaymentDetailsDTO).collect(Collectors.toList());
    }
}



