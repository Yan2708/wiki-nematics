package com.wn.utils;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.util.Collector;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Deserializer<T> implements DeserializationSchema<T> {
    private final Class<T> targetClass;

    public Deserializer(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public void open(InitializationContext context) throws Exception {
        DeserializationSchema.super.open(context);
    }

    @Override
    public T deserialize(byte[] bytes) throws IOException {
        String message = new String(bytes, StandardCharsets.UTF_8);
        System.out.println(message);
        return Mapper.getInstance().readValue(message, targetClass);
    }

    @Override
    public boolean isEndOfStream(T t) {
        return false;
    }

    @Override
    public TypeInformation<T> getProducedType() {
        return TypeInformation.of(targetClass);
    }
}