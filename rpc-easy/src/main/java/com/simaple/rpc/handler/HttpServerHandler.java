package com.simaple.rpc.handler;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;

public class HttpServerHandler implements Handler<HttpServerRequest>
{

    @Override
    public void handle(HttpServerRequest event)
    {
        event.bodyHandler(body -> {
            int a = 1;
            int b = 1;
        });
    }
}
