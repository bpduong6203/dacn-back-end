package com.example.DoAnCN.Service;

import com.example.DoAnCN.Entity.User;
import com.example.DoAnCN.Repository.UserRepository;
import com.example.DoAnCN.Repository.VerificationCodeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class VerificationService {

    @Autowired
    private VerificationCodeRepository otpRepository;

    @Transactional
    public String generateVerificationCode(String email) {
        Optional<User> userOptional = otpRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String code = String.format("%06d", new Random().nextInt(1000000));
            user.setCode(code);
            user.setExpiresAt(LocalDateTime.now().plusMinutes(2));  // Mã có hiệu lực trong 2 phút
            otpRepository.save(user);
            return code;
        } else {
            throw new UsernameNotFoundException("User with email " + email + " not found");
        }
    }

    @Transactional
    public boolean verifyCode(String email, String code) {
        Optional<User> userOptional = otpRepository.findByEmailAndCode(email, code);
        if (userOptional.isPresent() && userOptional.get().getExpiresAt().isAfter(LocalDateTime.now())) {
            User user = userOptional.get();
            user.setCode(null);  // Xóa mã sau khi xác thực thành công
            user.setExpiresAt(null);
            otpRepository.save(user);
            return true;
        }
        return false;
    }

    @Scheduled(fixedRate = 60000)  // Kiểm tra và xóa mã hết hạn mỗi 1 phút
    @Transactional
    public void removeExpiredOtps() {
        LocalDateTime now = LocalDateTime.now();
        List<User> users = otpRepository.findByExpiresAtBefore(now);
        for (User user : users) {
            user.setCode(null);
            user.setExpiresAt(null);
            otpRepository.save(user);
        }
    }
}
