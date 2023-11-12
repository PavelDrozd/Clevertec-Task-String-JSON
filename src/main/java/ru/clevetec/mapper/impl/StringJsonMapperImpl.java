package ru.clevetec.mapper.impl;

import ru.clevetec.mapper.impl.deserializer.StringJsonDeserializer;
import ru.clevetec.mapper.impl.serializer.StringJsonSerializer;

public final class StringJsonMapperImpl implements StringJsonMapper {

    private final StringJsonDeserializer deserializer;

    private final StringJsonSerializer serializer;

    public StringJsonMapperImpl() {
        deserializer = new StringJsonDeserializer();
        serializer = new StringJsonSerializer();
    }

    @Override
    public String toJson(Object o) {
        return serializer.serialize(o);
    }

    @Override
    public <T> T toObject(String json, Class<T> classOfT) {
        return deserializer.deserialize(json, classOfT);
    }

    public void setWriteNulls(boolean bool) {
        serializer.setWriteNulls(bool);
    }
}
