package com.example.DoAnCN.Service;

import com.example.DoAnCN.Entity.*;
import com.example.DoAnCN.QRcode.QRCodeGenerator;
import com.example.DoAnCN.Repository.BankRepository;
import com.example.DoAnCN.Repository.PaymentRepository;
import com.example.DoAnCN.Repository.QRCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class QRCodeService {

    private final String ROOT_DIR = "src/main/resources/static/images";

    @Autowired
    private QRCodeRepository qrCodeRepository;

    @Autowired
    private BankRepository banksRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public String createQRCode(Long paymentId, Long bankId) throws Exception {
        Optional<PaymentDetails> paymentOpt = paymentRepository.findById(paymentId);
        if (paymentOpt.isEmpty()) {
            throw new IllegalArgumentException("PaymentDetails with id " + paymentId + " not found");
        }
        PaymentDetails paymentDetails = paymentOpt.get();

        Optional<Banks> bankOpt = banksRepository.findById(bankId);
        if (bankOpt.isEmpty()) {
            throw new IllegalArgumentException("Bank with id " + bankId + " not found");
        }
        Banks bank = bankOpt.get();

        if (paymentDetails == null || bank == null) {
            throw new IllegalArgumentException("PaymentDetails or Banks cannot be null");
        }

        String qrContent = generateFinalQRCodeContent(paymentDetails, bank);
        String qrCodeFilePath = saveQRCodeImage(qrContent, paymentDetails.getId(), bank.getId());

        QRCode qrCode = QRCode.builder()
                .qr_code_url(qrCodeFilePath)
                .bank(bank)
                .paymentDetails(paymentDetails)
                .build();

        qrCodeRepository.save(qrCode);
        return qrCodeFilePath;
    }

    private String generateEMVCoQRContent(PaymentDetails paymentDetails, Banks bank) {
        // Định dạng EMVCo cho mã QR thanh toán
        String amount = String.format("%,d", Math.round(paymentDetails.getAmount()));
        int amountLength = amount.length();

        String maHoaDon = paymentDetails.getInvoiceCode();

        String maDau = "00020101021";
        String dinhDanh = "0010A00000072701";
        String xacThuc = "0208QRIBFTTA530370454";

        return  maDau + bank.getMaDinhDanh() + dinhDanh + bank.getBankId() +
                bank.getRecipientAccountNumber() + xacThuc + String.format("%02d", amountLength) +
                amount + "5802VN62" + "260822" + maHoaDon + "6304";
    }

    private String calculateCRC(String data) {
        int crc = 0xFFFF; // Giá trị ban đầu của CRC

        for (int i = 0; i < data.length(); i++) {
            crc ^= data.charAt(i) << 8;

            for (int j = 0; j < 8; j++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ 0x1021;
                } else {
                    crc <<= 1;
                }
            }
        }

        return String.format("%04X", crc & 0xFFFF);
    }

    private String generateFinalQRCodeContent(PaymentDetails paymentDetails, Banks bank) {
        String qrContent = generateEMVCoQRContent(paymentDetails, bank);
        String crc = calculateCRC(qrContent);
        return qrContent + crc;
    }

    private String saveQRCodeImage(String qrContent, Long paymentId, Long bankId) throws Exception {
        File rootDir = new File(ROOT_DIR);
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }

        File destDir = new File(rootDir,"bank" + bankId);
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        String fileName = "qr_" + paymentId + "_" + System.currentTimeMillis() + ".png";
        Path filePath = Paths.get(destDir.getPath(), fileName);

        QRCodeGenerator.generateQRCodeImage(qrContent, 350, 350, filePath.toString());

        return "/images/" + "bank" + bankId + "/" + fileName;
    }

    public Optional<QRCode> qrCodeImg(Long Id){
        return qrCodeRepository.findById(Id);
    }
}

