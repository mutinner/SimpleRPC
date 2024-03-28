package com.simaple.rpc.handler;

import com.simaple.rpc.model.RpcRequest;
import com.simaple.rpc.model.RpcResponse;
import com.simaple.rpc.registry.LocalRegistry;
import com.simaple.rpc.serializer.Serializer;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VertxHttpServerHandler<T extends Serializer>
    implements Handler<HttpServerRequest>
{
    Serializer serializer = null;

    public VertxHttpServerHandler(Serializer s) { serializer = s; }

    @Override
    public void handle(HttpServerRequest request)
    {
        request.bodyHandler(body -> {
            byte[] bytes          = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (Exception e) {
                log.error("rpcRequest: {}", e);
            }

            RpcResponse rpcResponse = new RpcResponse();
            if (Objects.isNull(rpcRequest)) {
                rpcResponse.setMessage("rpcRequest is null.");
                doResponse(request, rpcResponse, serializer);
                return;
            }

            try {
                Class<?> implClass =
                    LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(
                    rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(
                    Class.forName(implClass.getName()), rpcRequest.getArgs());

                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("success");
            } catch (Exception e) {
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
        });
    }

    void doResponse(HttpServerRequest request,
                    RpcResponse       rpcResponse,
                    Serializer        serializer)
    {
        HttpServerResponse response =
            request.response().putHeader("content-type", "application/json");
        try {
            byte[] serialized = serializer.serialize(rpcResponse);
            response.end(Buffer.buffer(serialized));
        } catch (IOException e) {
            log.error("doResponse error", e);
            response.end(Buffer.buffer());
        }
    }
}
