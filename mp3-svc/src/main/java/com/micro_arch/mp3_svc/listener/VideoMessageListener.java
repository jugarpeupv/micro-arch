//package com.micro_arch.mp3_svc.listener;
//
//import com.micro_arch.mp3_svc.util.Mp4ToMp3Converter;
//import com.mongodb.client.gridfs.GridFSBucket;
//import com.mongodb.client.gridfs.GridFSBuckets;
//import com.mongodb.client.gridfs.model.GridFSUploadOptions;
//import com.mongodb.client.model.Filters;
//import org.bson.types.ObjectId;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.gridfs.GridFsTemplate;
//import org.springframework.stereotype.Component;
//import ws.schild.jave.EncoderException;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.file.Files;
//
//@Component
//public class VideoMessageListener {
//
//    @Autowired
//    private GridFsTemplate gridFsTemplate;
//
//    @RabbitListener(queues = "videos")
//    public void receiveMessage(String videoId) {
//        try {
//            // Retrieve the .mp4 video from MongoDB
//            ObjectId objectId = new ObjectId(videoId);
//            File tempMp4File = File.createTempFile("temp", ".mp4");
//            try (FileOutputStream fos = new FileOutputStream(tempMp4File)) {
//                gridFsTemplate.getResource(objectId).getInputStream().transferTo(fos);
//            }
//
//            // Convert the .mp4 video to .mp3
//            File tempMp3File = File.createTempFile("temp", ".mp3");
//            Mp4ToMp3Converter.convertMp4ToMp3(tempMp4File, tempMp3File);
//
//            // Store the .mp3 file back in MongoDB
//            byte[] mp3Bytes = Files.readAllBytes(tempMp3File.toPath());
//            try (ByteArrayInputStream mp3InputStream = new ByteArrayInputStream(mp3Bytes)) {
//                GridFSBucket gridFSBucket = GridFSBuckets.create(gridFsTemplate.getDb());
//                GridFSUploadOptions options = new GridFSUploadOptions().metadata(new org.bson.Document("contentType", "audio/mpeg"));
//                gridFSBucket.uploadFromStream(videoId + ".mp3", mp3InputStream, options);
//            }
//
//            // Clean up temporary files
//            Files.deleteIfExists(tempMp4File.toPath());
//            Files.deleteIfExists(tempMp3File.toPath());
//
//        } catch (IOException | EncoderException e) {
//            e.printStackTrace();
//        }
//    }
//}
