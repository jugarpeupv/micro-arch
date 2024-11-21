package com.micro_arch.mp3_svc.controller;

import com.micro_arch.mp3_svc.util.Mp4ToMp3Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

import java.io.File;
import java.io.IOException;

/**
 * MyController
 */
@RestController
public class MyController {

    @PostMapping("/convert")
    public ResponseEntity<byte[]> convertMp4ToMp3(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            // Save the uploaded file to a temporary location
            File tempMp4File = File.createTempFile("temp", ".mp4");
            file.transferTo(tempMp4File);

            // Convert the MP4 file to MP3
            File tempMp3File = File.createTempFile("temp", ".mp3");
            Mp4ToMp3Converter.convertMp4ToMp3(tempMp4File, tempMp3File);

            // Read the converted MP3 file into a byte array
            byte[] mp3Bytes = java.nio.file.Files.readAllBytes(tempMp3File.toPath());

            // Clean up temporary files
            tempMp4File.delete();
            tempMp3File.delete();

            // Return the MP3 file as a response
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=converted.mp3");
            return new ResponseEntity<>(mp3Bytes, headers, HttpStatus.OK);

        } catch (IOException | EncoderException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
