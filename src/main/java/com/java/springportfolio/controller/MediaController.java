package com.java.springportfolio.controller;

import com.java.springportfolio.dto.ImageDeleteRequest;
import com.java.springportfolio.service.MediaService;
import com.java.springportfolio.util.DtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {

    private final DtoValidator dtoValidator;
    private final MediaService mediaService;

    @PostMapping("/image/upload")
    public ResponseEntity<String> saveImage(@RequestParam("file") MultipartFile file) {
        dtoValidator.validateMultipartFile(file);
        return new ResponseEntity<>(mediaService.saveImageToServer(file), HttpStatus.OK);
    }

    @PostMapping("/image/delete")
    public ResponseEntity<String> deleteImage(@RequestBody ImageDeleteRequest imageDeleteRequest) {
        dtoValidator.validateImageDeleteRequest(imageDeleteRequest);
        mediaService.deleteImageFromServer(imageDeleteRequest);
        return new ResponseEntity<>("The images have been successfully deleted!", HttpStatus.OK);
    }
}
