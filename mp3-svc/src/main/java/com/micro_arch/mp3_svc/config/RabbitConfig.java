package com.micro_arch.mp3_svc.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

  @Bean
  RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
    return new RabbitTemplate(connectionFactory);
  }
}
