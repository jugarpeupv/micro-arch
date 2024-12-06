package com.micro_arch.mp3_svc.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.micro_arch.mp3_svc.util.Mp4ToMp3Converter;

import ws.schild.jave.EncoderException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/api")
public class Mp3SvcController {

  public static void readFileInChunks(String filePath, int bufferSize) throws IOException {
    try (InputStream inputStream = new FileInputStream(filePath);
        FileChannel fileChannel = ((FileInputStream) inputStream).getChannel()) {

      ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
      while (fileChannel.read(buffer) > 0) {
        buffer.flip();
        // Process the buffer here
        // For example, you can convert it to a byte array
        byte[] chunk = new byte[buffer.remaining()];
        buffer.get(chunk);
        // Do something with the chunk
        System.out.println("Read chunk of size: " + chunk.length);
        System.out.println("Read chunk " + chunk.toString());
        buffer.clear();
      }
    }
  }

  @PostMapping("/convert")
  public ResponseEntity<byte[]> convertMp4ToMp3(@RequestParam("file") MultipartFile file) {
    if (file.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Path tempMp4Path = null;
    Path tempMp3Path = null;

    try {
      // Save the uploaded file to a temporary location
      // tempMp4Path = Files.createTempFile("tempmp4_", ".mp4");
      // try (OutputStream os = Files.newOutputStream(tempMp4Path)) {
      // os.write(file.getBytes());
      // }

      // byte[] bytes = file.getBytes();

      File uploadedFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
      file.transferTo(uploadedFile);

      // File tempMp4File = tempMp4Path.toFile();
      // System.out.println("Temporary MP4 file created at: " +
      // tempMp4File.getAbsolutePath());

      // Convert the MP4 file to MP3
      tempMp3Path = Files.createTempFile("tempMp3_", ".mp3");
      File tempMp3File = tempMp3Path.toFile();
      System.out.println("Temporary MP3 file created at: " + tempMp3File.getAbsolutePath());

      Mp4ToMp3Converter.convertMp4ToMp3(uploadedFile, tempMp3File);


      int bufferSize = 1024 * 1024; // 1 MB buffer size
      readFileInChunks(uploadedFile.getAbsolutePath(), bufferSize);


      byte[] mp3Bytes = Files.readAllBytes(tempMp3Path);

      // Clean up temporary files
      // Files.delete(tempMp4Path);
      Files.delete(tempMp3Path);

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
