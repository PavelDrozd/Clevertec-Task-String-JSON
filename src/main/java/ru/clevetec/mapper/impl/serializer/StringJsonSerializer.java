package ru.clevetec.mapper.impl.serializer;

import ru.clevetec.exception.ObjectSerializeException;
import ru.clevetec.mapper.impl.JavaTimeFormatter;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StringJsonSerializer {

    private static final String LEFT_BRACE = "{";
    private static final String RIGHT_BRACE = "}";
    private static final String LEFT_SQUARE = "[";
    private static final String RIGHT_SQUARE = "]";
    private static final String DOUBLE_QUOTE = "\"";
    private static final String COLON = ":";
    private static final String COMMA = ",";

    private final JavaTimeFormatter formatter;

    private static boolean writeNulls = true;

    public StringJsonSerializer() {
        formatter = new JavaTimeFormatter();
    }

    public String serialize(Object o) {
        if (o == null) {
            throw new ObjectSerializeException("Object is null.");
        }
        StringBuilder jsonBuilder = new StringBuilder();
        processJson(o, jsonBuilder);
        return cleanJson(jsonBuilder.toString());
    }

    public void setWriteNulls(boolean bool) {
        writeNulls = bool;
    }

    private void processJson(Object o, StringBuilder jsonBuilder) {
        if (o instanceof List<?>) {
            processList(o, jsonBuilder);
        } else if (o instanceof Map<?, ?>) {
            processMap(o, jsonBuilder);
        } else {
            processClass(o, jsonBuilder);
        }
    }

    private void processList(Object o, StringBuilder jsonBuilder) {
        List<?> objects = (List<?>) o;
        jsonBuilder.append(LEFT_SQUARE);
        for (Object object : objects) {
            processJson(object, jsonBuilder);
            jsonBuilder.append(COMMA);
        }
        jsonBuilder.append(RIGHT_SQUARE);
    }

    private void processMap(Object o, StringBuilder jsonBuilder) {
        Map<?, ?> map = (Map<?, ?>) o;
        jsonBuilder.append(LEFT_BRACE);
        map.forEach((k, v) -> {
            processValue(k, jsonBuilder);
            jsonBuilder.append(COLON);
            processValue(v, jsonBuilder);
            jsonBuilder.append(COMMA);
        });
        jsonBuilder.append(RIGHT_BRACE);
    }

    private void processClass(Object o, StringBuilder jsonBuilder) {
        jsonBuilder.append(LEFT_BRACE);
        Class<?> clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            jsonBuilder.append(DOUBLE_QUOTE)
                    .append(field.getName())
                    .append(DOUBLE_QUOTE)
                    .append(COLON);
            try {
                Object value = field.get(o);
                checkValue(jsonBuilder, value);
                processValue(value, jsonBuilder);
            } catch (IllegalAccessException e) {
                throw new ObjectSerializeException(e);
            }
            if (i < fields.length - 1) {
                jsonBuilder.append(COMMA);
            }
        }
        jsonBuilder.append(RIGHT_BRACE);
    }

    private static void checkValue(StringBuilder jsonBuilder, Object value) {
        if (value == null && writeNulls) {
            jsonBuilder.append("null");
            jsonBuilder.append(COMMA);
        }
    }

    private void processValue(Object value, StringBuilder jsonBuilder) {
        switch (Objects.requireNonNull(value).getClass().getSimpleName()) {
            case "String", "UUID" -> jsonBuilder.append(DOUBLE_QUOTE).append(value).append(DOUBLE_QUOTE);
            case "Byte", "Short", "Integer", "Long", "Float", "Double", "Character", "Boolean",
                    "BigInteger", "BigDecimal" -> jsonBuilder.append(value);
            case "LocalTime", "LocalDate", "LocalDateTime", "OffsetTime", "OffsetDateTime", "ZonedDateTime" ->
                    jsonBuilder.append(DOUBLE_QUOTE).append(formatter.formatObjectDate(value)).append(DOUBLE_QUOTE);
            default -> processJson(value, jsonBuilder);
        }
    }

    private String cleanJson(String json) {
        return json.replace(",]", "]")
                .replace(",}", "}");
    }
}
