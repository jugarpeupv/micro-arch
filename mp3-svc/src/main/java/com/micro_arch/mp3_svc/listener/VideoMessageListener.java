package com.micro_arch.mp3_svc.listener;

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

    //@Value("${spring.data.mongodb.uri}")
    //private String mongoUri;

    @RabbitListener(queues = "video")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);

        //// Assuming the message contains the ObjectId of the file in MongoDB
        //ObjectId fileId = new ObjectId(message);
        //
        //// Retrieve the file from MongoDB
        //try (MongoClient mongoClient = MongoClients.create(mongoUri)) {
        //    GridFSBucket gridFSBucket = GridFSBuckets.create(mongoClient.getDatabase("videos"));
        //    GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(fileId);
        //
        //    File tempMp4File = File.createTempFile("tempMp4_", ".mp4");
        //    try (FileOutputStream outputStream = new FileOutputStream(tempMp4File)) {
        //        byte[] buffer = new byte[1024];
        //        int bytesRead;
        //        while ((bytesRead = downloadStream.read(buffer)) != -1) {
        //            outputStream.write(buffer, 0, bytesRead);
        //        }
        //    }
        //
        //    // Convert the MP4 file to MP3
        //    File tempMp3File = File.createTempFile("tempMp3_", ".mp3");
        //    Mp4ToMp3Converter.convertMp4ToMp3(tempMp4File, tempMp3File);
        //
        //    // Clean up temporary files
        //    tempMp4File.delete();
        //    tempMp3File.delete();

        //} catch (IOException | EncoderException e) {
        //    e.printStackTrace();
        //}
    }
}
