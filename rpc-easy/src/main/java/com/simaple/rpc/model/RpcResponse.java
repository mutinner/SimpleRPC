package com.simaple.rpc.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse implements Serializable
{
    private Object data;

    private Class<?> dataType;

    private String message;

    private Exception exception;
}
