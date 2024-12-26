package com.example.DoAnCN.Service;

import com.example.DoAnCN.Entity.User;
import com.example.DoAnCN.Jwt.JwtUtil;
import com.example.DoAnCN.Repository.UserRepository;
import com.example.DoAnCN.Repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private VerificationService verificationService;
    @Autowired
    private VerificationCodeRepository vftRepository;
    @Autowired
    private JwtUtil jwtUtil;

    public User findByEmail(String email) {
        return vftRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public void sendVerificationCode(String email) {
        User user = vftRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("User with email " + email + " not found");
        }

        String verificationCode = verificationService.generateVerificationCode(email);
        user.setCode(verificationCode);
        vftRepository.save(user);

        String emailContent = "<html><body>" +
                "<h1>Mã xác minh của bạn</h1>" +
                "<p>Để đặt lại mật khẩu, hãy sử dụng mã xác minh dưới đây:</p>" +
                "<h2>" + verificationCode + "</h2>" +
                "<p>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!</p>" +
                "<p>Trân trọng,<br/>Hệ thống của chúng tôi</p>" +
                "</body></html>";

        sendEmail(user.getEmail(),  verificationCode + " is Verification Code", emailContent);
    }

    @Transactional
    public boolean verifyCode(String email, String code) {
        User user = vftRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("User with email " + email + " not found");
        }

        String storedCode = user.getCode();
        if (storedCode != null && storedCode.equals(code)) {
            // Xóa mã xác nhận sau khi đã xác thực thành công
            user.setCode(null);
            user.setExpiresAt(null);
            vftRepository.save(user);
            return true;
        }

        return false;
    }

    @Transactional
    public void sendWelcomeEmail(User user) {
        String emailContent = "<html><body>" +
                "<h1>Chào mừng " + user.getUsername() + "!</h1>" +
                "<p>Cảm ơn bạn đã đăng ký tài khoản tại dịch vụ đặt vé du lịch của chúng tôi.</p>" +
                "<p>Chúng tôi rất vui mừng chào đón bạn và hy vọng rằng bạn sẽ có những trải nghiệm tuyệt vời cùng chúng tôi.</p>" +
                "<p>Đừng ngần ngại liên hệ với chúng tôi nếu bạn có bất kỳ câu hỏi hoặc cần hỗ trợ.</p>" +
                "<p>Trân trọng,<br/>Đội ngũ Travel</p>" +
                "</body></html>";

        sendEmail(user.getEmail(), "Chào mừng đến với dịch vụ đặt vé du lịch", emailContent);
    }

    private void sendEmail(String to, String subject, String content) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);

            try {
                helper.setFrom("hethongvellrel@gmail.com", "Travel");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            helper.setText(content, true);
            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}








//package com.example.DoAnCN.Service;
//
//import com.example.DoAnCN.Entity.User;
//import com.example.DoAnCN.Jwt.JwtUtil;
//import com.example.DoAnCN.Repository.UserRepository;
//import com.example.DoAnCN.Repository.VerificationCodeRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//
//import java.io.UnsupportedEncodingException;
//
//@Service
//public class EmailService {
//    @Autowired
//    private JavaMailSender emailSender;
//    @Autowired
//    private VerificationService verificationService;
//    @Autowired
//    private VerificationCodeRepository vftRepository;
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    public User findByEmail(String email) {
//        return vftRepository.findByEmail(email).orElse(null);
//    }
//
//    @Transactional
//    public void sendVerificationCode(String email) {
//        User user = vftRepository.findByEmail(email).orElse(null);
//        if (user == null) {
//            throw new UsernameNotFoundException("User with email " + email + " not found");
//        }
//
//        String verificationCode = verificationService.generateVerificationCode(email);
//        user.setCode(verificationCode);
//        vftRepository.save(user);
//
//        String emailContent = "Your verification code is: " + verificationCode;
//        sendEmail(user.getEmail(), "" + verificationCode + " is verification code", emailContent);
//    }
//
//    @Transactional
//    public boolean verifyCode(String email, String code) {
//        User user = vftRepository.findByEmail(email).orElse(null);
//        if (user == null) {
//            throw new UsernameNotFoundException("User with email " + email + " not found");
//        }
//
//        String storedCode = user.getCode();
//        if (storedCode != null && storedCode.equals(code)) {
//            // Xóa mã xác nhận sau khi đã xác thực thành công
//            user.setCode(null);
//            user.setExpiresAt(null);
//            vftRepository.save(user);
//            return true;
//        }
//
//        return false;
//    }
//
//    private void sendEmail(String to, String subject, String content) {
//        MimeMessage message = emailSender.createMimeMessage();
//        try {
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//            helper.setTo(to);
//            helper.setSubject(subject);
//
//            try{
//                helper.setFrom("hethongvellrel@gmail.com", "Travel");
//            }catch (UnsupportedEncodingException e){
//                e.printStackTrace();
//            }
//
//            String htmlContent = "<html><body>" +
//                    "<h1>Mã xác minh của bạn</h1>" +
//                    "<p>Để đặt lại mật khẩu, hãy sử dụng mã xác minh dưới đây:</p>" +
//                    "<h2>" + content + "</h2>" +
//                    "<p>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!</p>" +
//                    "<p>Trân trọng,<br/>Hệ thống của chúng tôi</p>" +
//                    "</body></html>";
//
//            helper.setText(htmlContent, true);
//            emailSender.send(message);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }
//}
