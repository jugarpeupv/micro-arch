package com.micro_arch.mp3_svc.config;

import com.micro_arch.mp3_svc.services.MongoDBService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Value("${spring.data.mongodb.database}")
  private String databaseName;

  @Value("${spring.data.mongodb.uri}")
  private String mongoUri;

  @Bean
  MongoDBService mongoDBService() {
    return new MongoDBService(mongoUri, databaseName);
  }
}
