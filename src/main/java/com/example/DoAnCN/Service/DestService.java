
package com.example.DoAnCN.Service;

import com.example.DoAnCN.DTO.*;
import com.example.DoAnCN.Entity.*;
import com.example.DoAnCN.Repository.DestImgRepository;
import com.example.DoAnCN.Repository.DestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DestService {
    @Autowired
    private DestRepository destRepository;

    @Autowired
    DestImgRepository destImgRepository;

    @Autowired
    private DTOConverter dtoConverter;

    private final String ROOT_DIR = "src/main/resources/static";

    // trong nuoc
    public List<DestinationsDTO> findDomesticDestinations() {
        List<Destinations> destinations = destRepository.findByCity_Province_Country("Việt Nam");
        return destinations.stream().map(dtoConverter::convertDestinationsDTO).collect(Collectors.toList());
    }

    //ngoai nuoc
    public List<DestinationsDTO> findInternationalDestinations() {
        List<Destinations> destinations = destRepository.findByCity_Province_CountryNot("Việt Nam");
        return destinations.stream().map(dtoConverter::convertDestinationsDTO).collect(Collectors.toList());
    }

    public List<DestinationsDTO> getAllDest() {
        return destRepository.findAll().stream().map(dtoConverter::convertDestinationsDTO).collect(Collectors.toList());
    }

    //lưu file word
    public void saveDescriptionFile(Long destinationId, MultipartFile file) throws IOException {
        Destinations destination = destRepository.findById(destinationId)
                .orElseThrow(() -> new IllegalArgumentException("Destination not found"));
        if (destination.getDescriptionFile() != null) {
            File oldFile = new File(ROOT_DIR + destination.getDescriptionFile().getFilePath());
            if (oldFile.exists()) {
                oldFile.delete();
            }
        }

        if (!file.isEmpty()) {
            File uploadDir = new File(ROOT_DIR, "uploads/" + destinationId);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir.getPath(), fileName);
            Files.write(filePath, file.getBytes());
            DescriptionFile descriptionFile = new DescriptionFile();
            descriptionFile.setFileName(fileName);
            descriptionFile.setFilePath("/uploads/" + destinationId + "/" + fileName);
            descriptionFile.setDestination(destination);
            destination.setDescriptionFile(descriptionFile);
            destRepository.save(destination);
        }
    }

    // Chi tiết 1 dest
    public DestinationsDTO detailsDestById(Long id) {
        Destinations dest = destRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Destination not found"));
        return dtoConverter.convertDestinationsDTO(dest);
    }

    public Optional<Destinations> DestById(Long id) {
        return destRepository.findById(id);
    }

    public Destinations saveDest(Destinations destinations) {
        return destRepository.save(destinations);
    }

    public Destinations updateDest(Destinations newDest) {
        Destinations existingDest = destRepository.findById(newDest.getId())
                .orElseThrow(() -> new IllegalArgumentException("Destination not found"));

        // Cập nhật các trường cơ bản
        existingDest.setName(newDest.getName());
        existingDest.setDescription(newDest.getDescription());
        existingDest.setLocation(newDest.getLocation());

        if (newDest.getCity() != null){
            existingDest.setCity(newDest.getCity());
        }

        // Không cập nhật hoặc xóa ảnh và file mô tả nếu chúng không thay đổi
        if (newDest.getDescriptionFile() != null) {
            existingDest.setDescriptionFile(newDest.getDescriptionFile());
        }

        if (newDest.getDestinationImages() != null) {
            existingDest.setDestinationImages(newDest.getDestinationImages());
        }

        return destRepository.save(existingDest);
    }

    //Image
    public void saveImage(DestinationImages destImg, Destinations destinations) throws IOException {
        File rootDir = new File(ROOT_DIR, "images");
        if (!rootDir.exists()) {
            rootDir.mkdirs();
        }

        File destDir = new File(rootDir, String.valueOf(destinations.getId()));
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        String fileName = "img" + System.currentTimeMillis() + ".jpg";
        Path filePath = Paths.get(destDir.getPath(), fileName);

        String base64Image = destImg.getImage_url().split(",")[1];
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        BufferedImage image = ImageIO.read(bis);

        if (image == null) {
            throw new IOException("Image decoding failed");
        }

        ImageIO.write(image, "jpg", filePath.toFile());

        String imageUrl = "/images/" + destinations.getId() + "/" + fileName;
        destImg.setImage_url(imageUrl.replace("\\", "/"));
        destImg.setDestination(destinations);
    }

    public List<DestinationImages> createImages(List<DestinationImages> destImgs, boolean isNew) throws IOException {
        for (DestinationImages destImg : destImgs) {
            Destinations destinations = destRepository.findById(destImg.getDestination().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Destination not found"));

            if (!isNew) {
                DestinationImages existingImg = destImgRepository.findById(destImg.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Image not found"));
                existingImg.setImage_url(destImg.getImage_url());
                existingImg.setDestination(destinations);
            }

            saveImage(destImg, destinations);
        }
        return destImgRepository.saveAll(destImgs);
    }

    public DestinationImages updateImage(DestinationImages destImg, boolean isNew) throws IOException {
        Destinations destinations = destRepository.findById(destImg.getDestination().getId())
                .orElseThrow(() -> new IllegalArgumentException("Destination not found"));

        if (!isNew) {
            DestinationImages existingImg = destImgRepository.findById(destImg.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Image not found"));
            existingImg.setImage_url(destImg.getImage_url());
            existingImg.setDestination(destinations);
        }

        saveImage(destImg, destinations);
        return destImgRepository.save(destImg);
    }

    public void deleteImg(Long id) {
        DestinationImages img = destImgRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Image not found"));
        File file = new File(ROOT_DIR + img.getImage_url());
        if (file.exists()) {
            file.delete();
        }
        destImgRepository.delete(img);
    }
}