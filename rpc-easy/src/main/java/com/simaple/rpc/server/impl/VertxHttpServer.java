package com.simaple.rpc.server.impl;

import com.simaple.rpc.handler.HttpServerHandler;
import com.simaple.rpc.handler.VertxHttpServerHandler;
import com.simaple.rpc.model.User;
import com.simaple.rpc.registry.LocalRegistry;
import com.simaple.rpc.serializer.impl.JdkSerializer;
import com.simaple.rpc.server.HttpServer;
import com.simaple.rpc.service.UserService;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VertxHttpServer implements HttpServer
{
    @Override
    public void doStart(int port)
    {
        Vertx                         vertx  = Vertx.vertx();
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        Handler<HttpServerRequest> handler =
            new VertxHttpServerHandler<>(new JdkSerializer());

        handler = new HttpServerHandler();

        server.requestHandler(handler);

        server.listen(port, req -> {
            if (req.succeeded()) {
                log.info("Server is now listening on port " + port);
            } else {
                log.error("http error, req:{}", req);
            }
        });
    }

    public static void main(String[] args)
    {
        LocalRegistry.register(UserService.class.getName(), User.class);

        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
