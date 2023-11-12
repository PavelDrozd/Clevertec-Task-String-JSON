package ru.clevetec.mapper.impl.deserializer;

import ru.clevetec.exception.JsonDeserializeException;
import ru.clevetec.exception.JsonEmptyValidationException;
import ru.clevetec.exception.JsonNullValidationException;
import ru.clevetec.exception.JsonValidationException;
import ru.clevetec.mapper.impl.JavaTimeFormatter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringJsonDeserializer {

    private static final String JSON_VALIDATION_REGEX =
            "[{\\[]([,:{}\\[\\]0-9.\\-+Eaeflnr-u \\n\\r\\t]|\".*?\")+[}\\]]";

    private final JsonStringParser jsonStringParser;

    private final JavaTimeFormatter timeFormatter;

    public StringJsonDeserializer() {
        jsonStringParser = new JsonStringParser();
        timeFormatter = new JavaTimeFormatter();
    }

    public <T> T deserialize(String json, Class<T> classOfT) {
        checkJson(json);
        Map<Object, Object> jsonMap = jsonStringParser.toJsonMap(json);

        return processClass(classOfT, jsonMap);

    }

    private void checkJson(String json) {
        if (json == null) {
            throw new JsonNullValidationException("Json is null.");
        }
        if (json.isEmpty() || json.isBlank()) {
            throw new JsonEmptyValidationException("Json is empty.");
        }
        Pattern pattern = Pattern.compile(JSON_VALIDATION_REGEX);
        Matcher matcher = pattern.matcher(json);
        if (!matcher.matches()) {
            throw new JsonValidationException("Json is not valid: " + json);
        }
    }

    private <T> T processClass(Class<T> classOfT, Map<Object, Object> jsonMap) {
        try {
            T t = classOfT.getDeclaredConstructor().newInstance();

            Field[] fields = classOfT.getDeclaredFields();
            for (Field field : fields) {
                processField(field, t, jsonMap);
            }
            return t;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new JsonDeserializeException(e);
        }


    }

    private <T> void processField(Field field, T t, Map<Object, Object> jsonMap) throws IllegalAccessException {
        field.setAccessible(true);
        String fieldName = field.getName();
        Type fieldType = field.getGenericType();
        Object value;
        if (List.class.isAssignableFrom(field.getType())) {
            List<?> jsonObjects = (List<?>) jsonMap.get(fieldName);
            value = processList(fieldType, jsonObjects);
        } else if (Map.class.isAssignableFrom(field.getType())) {
            List<?> jsonObjects = (List<?>) jsonMap.get(fieldName);
            value = processMap(fieldType, jsonObjects);
        } else {
            value = processType(fieldName, fieldType, jsonMap);
        }
        field.set(t, value);
    }

    @SuppressWarnings("unchecked")
    private List<?> processList(Type fieldType, List<?> jsonObjects) {
        List<Object> objects = new ArrayList<>();
        if (fieldType instanceof ParameterizedType parameterizedType) {
            Type listGeneticType = parameterizedType.getActualTypeArguments()[0];
            for (Object object : jsonObjects) {
                Map<Object, Object> objectMap = (Map<Object, Object>) object;
                if (listGeneticType instanceof Class<?> clazz) {
                    objects.add(processClass(clazz, objectMap));
                }
            }
        }
        return objects;
    }

    @SuppressWarnings("unchecked")
    private Map<Object, Object> processMap(Type fieldType, List<?> jsonObjects) {
        Map<Object, Object> objectsMap = new HashMap<>();
        if (fieldType instanceof ParameterizedType parameterizedType) {
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            Type mapValueGeneticType = typeArguments[1];
            for (Object object : jsonObjects) {
                Map<Object, Object> objectMap = (Map<Object, Object>) object;
                objectMap.forEach((k, v) -> {
                    if (mapValueGeneticType instanceof Class<?> clazz) {
                        Object value = processClass(clazz, (Map<Object, Object>) v);
                        objectsMap.put(k, value);
                    }
                });
            }
        }
        return objectsMap;
    }

    private Object processType(String name, Type type, Map<Object, Object> jsonMap) {
        Object object = jsonMap.get(name);
        if (object == null) {
            throw new JsonValidationException("Not found object of type: " + type + " in json");
        }
        if (type.equals(String.class)) {
            return object.toString();
        } else if (type.equals(Byte.class)) {
            return Byte.parseByte(object.toString());
        } else if (type.equals(Short.class)) {
            return Short.parseShort(object.toString());
        } else if (type.equals(Integer.class)) {
            return Integer.parseInt(object.toString());
        } else if (type.equals(Long.class)) {
            return Long.parseLong(object.toString());
        } else if (type.equals(Float.class)) {
            return Float.parseFloat(object.toString());
        } else if (type.equals(Double.class)) {
            return Double.parseDouble(object.toString());
        } else if (type.equals(Character.class)) {
            return object.toString().charAt(0);
        } else if (type.equals(Boolean.class)) {
            return Boolean.parseBoolean(object.toString());
        } else if (type.equals(LocalTime.class) || type.equals(LocalDate.class) || type.equals(LocalDateTime.class)
                   || type.equals(OffsetTime.class) || type.equals(OffsetDateTime.class)
                   || type.equals(ZonedDateTime.class)) {
            return timeFormatter.getObjectDate(object, type);
        } else if (type.equals(UUID.class)) {
            return UUID.fromString(object.toString());
        } else if (type.equals(BigInteger.class)) {
            return new BigInteger(object.toString());
        } else if (type.equals(BigDecimal.class)) {
            return new BigDecimal(object.toString());
        } else {
            if (type instanceof Class<?> clazz)
                return processClass(clazz, jsonMap);
        }
        throw new JsonDeserializeException("Can't process type: " + type + " with name: " + name);
    }


}
