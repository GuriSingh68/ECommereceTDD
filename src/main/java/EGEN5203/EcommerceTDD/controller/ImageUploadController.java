package EGEN5203.EcommerceTDD.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/upload")
public class ImageUploadController {

    private static final String UPLOAD_DIR = "uploads/"; // Directory to save uploaded images

    @PostMapping(value = "/image", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadImage(@RequestPart("file") MultipartFile file) {
        try {
            // Create the uploads directory if it doesn't exist
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Save the file to the server
            Path filePath = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.write(filePath, file.getBytes());

            // Return the URL of the uploaded image
            String imageUrl = "/uploads/" + file.getOriginalFilename();
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
    }
}