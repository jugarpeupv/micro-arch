package com.micro_arch.mp3_svc.listener;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro_arch.mp3_svc.interfaces.VideoMessage;
import com.micro_arch.mp3_svc.services.MongoDBService;
import com.micro_arch.mp3_svc.util.Mp4ToMp3Converter;

import ws.schild.jave.EncoderException;

@Component
public class VideoMessageListener {

  private final RabbitTemplate rabbitTemplate;
  private final ObjectMapper objectMapper;
  private final MongoDBService mongoDBService;

  public VideoMessageListener(RabbitTemplate rabbitTemplate, ObjectMapper mapper, MongoDBService mongoDBService) {
    this.objectMapper = mapper;
    this.rabbitTemplate = rabbitTemplate;
    this.mongoDBService = mongoDBService;
  }

  @RabbitListener(queues = "video")
  public void receiveMessage(String message) {
    System.out.println("Received message: " + message);

    try {
      VideoMessage videoMessage = objectMapper.readValue(message, VideoMessage.class);
      System.out.println("Parsed message: " + videoMessage.getVideo_id() + ", " + videoMessage.getMp3_fid() + ", "
          + videoMessage.getUsername());

      // Download mp3 file from MongoDB using GridFS
      ObjectId fileId = new ObjectId(videoMessage.getVideo_id());
      File tempMp4File = File.createTempFile("tempMp4_", ".mp4");
      this.mongoDBService.streamDownloadIntoTempFile(fileId, tempMp4File);

      // Convert the MP4 file to MP3
      File tempMp3File = File.createTempFile("tempMp3_", ".mp3");
      Mp4ToMp3Converter.convertMp4ToMp3(tempMp4File, tempMp3File);

      // Upload the MP3 file to MongoDB using GridFS
      String fileName = videoMessage.getVideo_id() + ".mp3";
      Optional<String> result = this.mongoDBService.uploadFile(tempMp3File, fileName);
      result.ifPresentOrElse(
          objectId -> videoMessage.setMp3_fid(objectId),
          () -> {
          });

      // Publish a message to the rabbit mq "mp3" queue
      String mp3Message = objectMapper.writeValueAsString(videoMessage);
      rabbitTemplate.convertAndSend("mp3", mp3Message);
      System.out.println("Published message to 'mp3' queue: " + mp3Message);

      // Clean up temporary files
      tempMp4File.delete();
      tempMp3File.delete();
    } catch (IOException | EncoderException e) {
      e.printStackTrace();
    }
  }
}
