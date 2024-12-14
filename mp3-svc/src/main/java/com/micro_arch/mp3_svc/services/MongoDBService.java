package com.micro_arch.mp3_svc.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSUploadStream;

@Service
public class MongoDBService {
  public MongoClient mongoClient;
  public GridFSBucket gridFSBucket;

  private String mongoUri;
  private String databaseName;

  public MongoDBService(String mongoUri, String databaseName) {
    try {
      this.mongoUri = mongoUri;
      this.databaseName = databaseName;

      this.mongoClient = MongoClients.create(this.mongoUri);
      System.out.println("Connected to MongoDB url: " + this.mongoUri);

      this.gridFSBucket = GridFSBuckets.create(this.mongoClient.getDatabase(this.databaseName));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void streamDownloadIntoTempFile(ObjectId fileId, File tempFile) throws IOException {
    GridFSDownloadStream downloadStream = this.gridFSBucket.openDownloadStream(fileId);
    try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = downloadStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }
    }
  }

  public Optional<String> uploadFile(File tmpFile, String fileName) {
    try (FileInputStream mp3InputStream = new FileInputStream(tmpFile)) {
      GridFSUploadStream uploadStream = gridFSBucket
          .openUploadStream(fileName);
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = mp3InputStream.read(buffer)) != -1) {
        uploadStream.write(buffer, 0, bytesRead);
      }
      uploadStream.close();

      String mp3Fid = uploadStream.getObjectId().toHexString();

      System.out.println("File uploaded to Mongo with ObjectId: " + mp3Fid);
      return Optional.of(mp3Fid);
    } catch (Exception e) {
      System.out.println("Error uploading file to MongoDB: " + e.getMessage());
      e.printStackTrace();
      return Optional.empty();
    }
  }

  public void updateDocument(ObjectId fileId, String mp3Fid) {
    try (MongoClient mongoClient = MongoClients.create(mongoUri)) {
      mongoClient.getDatabase("videos").getCollection("yourCollectionName")
          .updateOne(new org.bson.Document("_id", fileId),
              new org.bson.Document("$set", new org.bson.Document("mp3_fid", mp3Fid)));
    }
  }
}
