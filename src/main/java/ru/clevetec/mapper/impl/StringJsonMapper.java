package ru.clevetec.mapper.impl;

public interface StringJsonMapper {

    String toJson(Object o);

    <T> T toObject(String json, Class<T> classOfT);
}
