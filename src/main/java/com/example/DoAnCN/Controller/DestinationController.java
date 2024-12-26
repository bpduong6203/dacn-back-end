package com.example.DoAnCN.Controller;

import com.example.DoAnCN.DTO.DestinationsDTO;
import com.example.DoAnCN.Entity.DestinationImages;
import com.example.DoAnCN.Entity.Destinations;
import com.example.DoAnCN.Service.DestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/dest")
public class DestinationController {
    @Autowired
    private DestService destService;
    @PostMapping("/create")
    public ResponseEntity<Destinations> createDest(@RequestBody Destinations destinations){
        Destinations save = destService.saveDest(destinations);
        return ResponseEntity.ok(save);
    }

    @PostMapping("/upload-file/{destinationId}")
    public ResponseEntity<String> uploadDescriptionFile(@PathVariable Long destinationId,
                                                        @RequestPart("descriptionFile") MultipartFile descriptionFile) throws IOException {
        destService.saveDescriptionFile(destinationId, descriptionFile);
        return ResponseEntity.ok("File uploaded successfully");
    }



    @PutMapping("/update")
    public ResponseEntity<Destinations> updateDest(@RequestBody Destinations destinations){
        Destinations updatedDest = destService.updateDest(destinations);
        return ResponseEntity.ok(updatedDest);
    }

    @GetMapping("/list")
    public ResponseEntity<List<DestinationsDTO>> getAllDest(){
        List<DestinationsDTO> dest = destService.getAllDest();
        return ResponseEntity.ok(dest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinationsDTO> getDetailsDest(@PathVariable Long id) {
        DestinationsDTO dest = destService.detailsDestById(id);
        return ResponseEntity.ok(dest);
    }


    //thêm mới ảnh có thể dùng làm up mới vào danh sách
    @PostMapping("/img")
    public ResponseEntity<List<DestinationImages>> createImg(@RequestBody List<DestinationImages> destImg) {
        try {
            List<DestinationImages> savedImages = destService.createImages(destImg, true);
            return ResponseEntity.ok(savedImages);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    //có thể không cần dùng nếu chỉ cần xóa và thêm mới
//    @PutMapping("/img/update")
//    public ResponseEntity<DestinationImages> updateImg(@RequestBody DestinationImages destImg){
//        try {
//            DestinationImages updatedImg = destService.updateImage(destImg, false);
//            return ResponseEntity.ok(updatedImg);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            return ResponseEntity.badRequest().build();
//        }
//    }


    //xóa ảnh
    @DeleteMapping("/img/{id}")
    public ResponseEntity<Void> deleteImg(@PathVariable Long id) {
        try {
            destService.deleteImg(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // phân chia trong nước và ngoài nước
    @GetMapping("/domestic")
    public ResponseEntity<List<DestinationsDTO>> getDomesticDestinations() {
        List<DestinationsDTO> destinationDTOs = destService.findDomesticDestinations();
        return ResponseEntity.ok(destinationDTOs);
    }

    @GetMapping("/international")
    public ResponseEntity<List<DestinationsDTO>> getInternationalDestinations() {
        List<DestinationsDTO> destinationDTOs = destService.findInternationalDestinations();
        return ResponseEntity.ok(destinationDTOs);
    }
}

