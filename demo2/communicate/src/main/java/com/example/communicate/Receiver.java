package com.example.communicate;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;

public class Receiver extends AbstractVerticle {
  EventBus eb;
  
  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    System.out.println("deployed");
    eb = vertx.eventBus();
    eb.consumer("ping", message -> {
      System.out.println("param:"+message.body());
    });
  }

}
