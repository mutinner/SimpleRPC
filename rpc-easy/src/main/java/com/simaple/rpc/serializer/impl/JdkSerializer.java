package com.simaple.rpc.serializer.impl;

import com.simaple.rpc.serializer.Serializer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import lombok.Data;

@Data
public class JdkSerializer implements Serializer
{

    @Override
    public <T> byte[] serialize(T object) throws IOException
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream    objectOutputStream =
            new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        return outputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException
    {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream    objectInputStream =
            new ObjectInputStream(inputStream);
        try {
            return (T)objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            objectInputStream.close();
        }
    }
}
