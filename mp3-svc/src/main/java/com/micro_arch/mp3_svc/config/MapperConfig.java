package com.micro_arch.mp3_svc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class MapperConfig {

  @Bean
  ObjectMapper mapper() {
    return new ObjectMapper();
  }

}
