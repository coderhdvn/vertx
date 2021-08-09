package com.example.communicate;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class MainVerticle extends AbstractVerticle {

  EventBus eb;

  @Override
  public void start(Promise<Void> startPromise) throws Exception  {
    eb = vertx.eventBus();
    Router router = Router.router(vertx);
    router.route("/ping").handler(this::ping);
    
    vertx
    .createHttpServer()
    .requestHandler(router)
    .listen(8080, result -> {});
    vertx.deployVerticle(new Receiver());
  }

  private void ping(RoutingContext routingContext) {
    String msg = routingContext.request().getParam("param");
    eb.send("ping", msg);
    routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
    .end("Done");
  }
}
