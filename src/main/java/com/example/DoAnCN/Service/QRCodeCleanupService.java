package com.example.DoAnCN.Service;

import com.example.DoAnCN.Entity.QRCode;
import com.example.DoAnCN.Repository.QRCodeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QRCodeCleanupService {
    //thời gian xóa mã ảnh qr code
    @Value("${qr.code.expiration.minutes:1}")
    private int expirationMinutes;

    private final QRCodeRepository qrCodeRepository;

    public QRCodeCleanupService(QRCodeRepository qrCodeRepository) {
        this.qrCodeRepository = qrCodeRepository;
    }

    @Scheduled(fixedRate = 120000)
    public void cleanupExpiredQRCodes() throws Exception{
        List<QRCode> expiredQRCodes = qrCodeRepository.findAll().stream()
                .filter(qrCode -> ChronoUnit.MINUTES.between(qrCode.getCreated_at(), LocalDateTime.now()) >= expirationMinutes)
                .collect(Collectors.toList());
        for (QRCode qrCode : expiredQRCodes){
            Path path = Paths.get("src/main/resources/static/images", qrCode.getQr_code_url().substring("/images/".length()));
            Files.deleteIfExists(path);
            qrCodeRepository.delete(qrCode);
        }
    }
}
