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
public class RpcRequest implements Serializable
{
    private String serviceName;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] args;
}
