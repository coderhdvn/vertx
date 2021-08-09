package com.example.consumer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.RabbitMQConsumer;
import io.vertx.rabbitmq.RabbitMQOptions;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    RabbitMQOptions config = new RabbitMQOptions();
    config.setUri("amqp://guest:guest@localhost:5672");
    RabbitMQClient client = RabbitMQClient.create(vertx, config);
    
    client.start(asyncResult -> {
      if (asyncResult.succeeded()) {
        System.out.println("RabbitMQ successfully connected!");
        
        
        client.basicConsumer("someq", rabbitMQConsumerAsyncResult -> {
          if (rabbitMQConsumerAsyncResult.succeeded()) {
            System.out.println("RabbitMQ consumer created !");
            RabbitMQConsumer mqConsumer = rabbitMQConsumerAsyncResult.result();
            mqConsumer.handler(message -> {
              System.out.println("Got message: " + message.body().toString());
            });
          } else {
            rabbitMQConsumerAsyncResult.cause().printStackTrace();
          }
        });
        
        
      } else {
        System.out.println("Fail to connect to RabbitMQ " + asyncResult.cause().getMessage());
      }
    });
  }
}
