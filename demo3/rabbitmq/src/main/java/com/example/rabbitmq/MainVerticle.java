package com.example.rabbitmq;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.rabbitmq.RabbitMQOptions;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.core.json.JsonObject;

public class MainVerticle extends AbstractVerticle {

  public void start() throws Exception {
    RabbitMQOptions config = new RabbitMQOptions();
    config.setUri("amqp://guest:guest@localhost:5672");
    RabbitMQClient client = RabbitMQClient.create(vertx, config);

    JsonObject jsonconfig = new JsonObject();
    jsonconfig.put("x-message-ttl", 10_000L);

    client.start(asyncResult -> {
      if (asyncResult.succeeded()) {
        System.out.println("RabbitMQ successfully connected!");
        client.queueDeclare("someq", true, false, true, jsonconfig, queueResult -> {
          if (queueResult.succeeded()) {
            System.out.println("Queue declared!");
            Buffer message = Buffer.buffer("hello MQ");
            client.basicPublish("", "someq", message, pubResult -> {
              if (pubResult.succeeded()) {
                System.out.println("Message published !");
              } else {
                pubResult.cause().printStackTrace();
              }
            });
          } else {
            System.err.println("Queue failed to be declared!");
            queueResult.cause().printStackTrace();
          }
        });
      } else {
        System.out.println("Fail to connect to RabbitMQ " + asyncResult.cause().getMessage());
      }
    });
  }
}
