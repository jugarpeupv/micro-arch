package com.micro_arch.mp3_svc.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro_arch.mp3_svc.interfaces.VideoMessage;
import com.micro_arch.mp3_svc.util.Mp4ToMp3Converter;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ws.schild.jave.EncoderException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class VideoMessageListener {

  @Value("${spring.data.mongodb.uri}")
  private String mongoUri;

  @RabbitListener(queues = "video")
  public void receiveMessage(String message) {
    System.out.println("Received message: " + message);

    // Parse the JSON message
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      VideoMessage videoMessage = objectMapper.readValue(message, VideoMessage.class);
      System.out.println("Parsed message: " + videoMessage.getVideo_id() + ", " + videoMessage.getMp3_fid() + ", "
          + videoMessage.getUsername());

      // Assuming the message contains the ObjectId of the file in MongoDB
      ObjectId fileId = new ObjectId(videoMessage.getVideo_id());

      // Retrieve the file from MongoDB
      try (MongoClient mongoClient = MongoClients.create(mongoUri)) {
        GridFSBucket gridFSBucket = GridFSBuckets.create(mongoClient.getDatabase("videos"));
        GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(fileId);

        File tempMp4File = File.createTempFile("tempMp4_", ".mp4");
        try (FileOutputStream outputStream = new FileOutputStream(tempMp4File)) {
          byte[] buffer = new byte[1024];
          int bytesRead;
          while ((bytesRead = downloadStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
          }
        }

        // Convert the MP4 file to MP3
        File tempMp3File = File.createTempFile("tempMp3_", ".mp3");
        Mp4ToMp3Converter.convertMp4ToMp3(tempMp4File, tempMp3File);

        // Define the path to save the MP3 file in the root of the project
        String projectRoot = System.getProperty("user.dir");
        File outputMp3File = new File(projectRoot, videoMessage.getVideo_id() + ".mp3");

        // Move the temporary MP3 file to the desired location
        if (tempMp3File.renameTo(outputMp3File)) {
          System.out.println("MP3 file saved to: " + outputMp3File.getAbsolutePath());
        } else {
          System.err.println("Failed to save MP3 file to: " + outputMp3File.getAbsolutePath());
        }

        // Clean up temporary files
        tempMp4File.delete();
        tempMp3File.delete();

      } catch (IOException | EncoderException e) {
        e.printStackTrace();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
