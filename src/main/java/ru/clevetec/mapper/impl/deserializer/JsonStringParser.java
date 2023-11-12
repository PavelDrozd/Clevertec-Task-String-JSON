package ru.clevetec.mapper.impl.deserializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonStringParser {

    private static final String EMPTY = "";
    private static final String COLON = ":";
    private static final String LEFT_SQUARE = "[";
    private static final String RIGHT_SQUARE = "]";
    private static final String SYMBOLS = "@#@";
    private static final String COLON_AND_LEFT_BRACE_AND_DOUBLE_QUOTE = ":{\"";
    private static final String COMMA_AND_DOUBLE_QUOTE = ",\"";
    private static final String RIGHT_BRACE_AND_COMMA = "},";
    private static final String DOUBLE_RIGHT_BRACES = "}}";
    private static final String SYMBOLS_AND_DOUBLE_QUOTE = "@#@\"";
    private static final String RIGHT_BRACE_AND_SYMBOLS = "}@#@";
    private static final String REGEX_RIGHT_BRACE_AND_SYMBOLS_AND_LEFT_BRACE = "}@#@\\{";
    private static final String REGEX_COLONS_AND_DOUBLE_QUOTES = "\":|:\"";
    private static final String REGEX_CLEAN_JSON = "[\"\\[\\]{}]";


    protected Map<Object, Object> toJsonMap(String json) {
        Map<Object, Object> jsonMap = new HashMap<>();
        String replacedJson = replaceJsonCommas(json);
        parseJson(replacedJson, jsonMap);
        return jsonMap;
    }

    private String replaceJsonCommas(String json) {
        return json.replace(COMMA_AND_DOUBLE_QUOTE, SYMBOLS_AND_DOUBLE_QUOTE)
                .replace(RIGHT_BRACE_AND_COMMA, RIGHT_BRACE_AND_SYMBOLS);
    }

    private void parseJson(String json, Map<Object, Object> jsonMap) {
        int firstListSquare = json.indexOf(LEFT_SQUARE);
        int firstMapBrace = json.indexOf(COLON_AND_LEFT_BRACE_AND_DOUBLE_QUOTE);

        if (firstListSquare < firstMapBrace && firstListSquare > 0) {
            processList(json, jsonMap);
        } else if (json.contains(COLON_AND_LEFT_BRACE_AND_DOUBLE_QUOTE)) {
            processMap(json, jsonMap);
        } else {
            processLine(json, jsonMap);
        }
    }

    private void processList(String json, Map<Object, Object> jsonMap) {
        int firstListLeftSquare = json.indexOf(LEFT_SQUARE);

        String firstSubstring = json.substring(0, firstListLeftSquare);
        String otherSubstring = json.substring(firstListLeftSquare + 1);

        int firstListRightSquare = otherSubstring.indexOf(RIGHT_SQUARE);
        int lastListRightSquare = otherSubstring.indexOf(RIGHT_SQUARE);

        String secondSubstring;
        String lastSubstring;

        if (firstListLeftSquare < firstListRightSquare && firstListRightSquare != lastListRightSquare) {
            secondSubstring = otherSubstring.substring(0, firstListLeftSquare);
            lastSubstring = otherSubstring.substring(firstListLeftSquare + 1);
        } else {
            secondSubstring = otherSubstring.substring(0, lastListRightSquare);
            lastSubstring = otherSubstring.substring(lastListRightSquare + 1);
        }

        String listName = processFirstSubstringAndGetName(firstSubstring, jsonMap);

        List<Object> objects = new ArrayList<>();
        processListSecondSubstring(secondSubstring, objects);

        jsonMap.put(listName, objects);
        parseJson(lastSubstring, jsonMap);

    }

    private void processMap(String json, Map<Object, Object> jsonMap) {
        int firstMapLeftBrace = json.indexOf(COLON_AND_LEFT_BRACE_AND_DOUBLE_QUOTE);

        String firstSubstring = json.substring(0, firstMapLeftBrace);
        String otherSubstring = json.substring(firstMapLeftBrace + 1);

        int firstMapRightBraces = otherSubstring.indexOf(DOUBLE_RIGHT_BRACES);
        int lastMapRightBraces = otherSubstring.indexOf(DOUBLE_RIGHT_BRACES);

        String secondSubstring;
        String lastSubstring;

        if (firstMapLeftBrace < firstMapRightBraces && firstMapRightBraces != lastMapRightBraces) {
            secondSubstring = otherSubstring.substring(0, firstMapLeftBrace);
            lastSubstring = otherSubstring.substring(firstMapLeftBrace + 1);
        } else if (firstMapRightBraces < 0 && firstMapLeftBrace > 0) {
            secondSubstring = otherSubstring;
            lastSubstring = EMPTY;
        } else {
            secondSubstring = otherSubstring.substring(0, lastMapRightBraces);
            lastSubstring = otherSubstring.substring(lastMapRightBraces + 1);
        }

        String name = processFirstSubstringAndGetName(firstSubstring, jsonMap);
        processMapSecondSubstring(jsonMap, secondSubstring, name);
        parseJson(lastSubstring, jsonMap);
    }

    private String processFirstSubstringAndGetName(String substring, Map<Object, Object> jsonMap) {
        String[] jsonParts = substring.split(SYMBOLS);
        for (int i = 0; i < jsonParts.length - 1; i++) {
            parseJson(jsonParts[i], jsonMap);
        }
        return cleanLine(jsonParts[jsonParts.length - 1])
                .replace(COLON, EMPTY);
    }

    private void processListSecondSubstring(String substring, List<Object> objects) {
        String[] jsonParts = substring.split(REGEX_RIGHT_BRACE_AND_SYMBOLS_AND_LEFT_BRACE);
        for (String jsonPart : jsonParts) {
            Map<Object, Object> objectMap = new HashMap<>();
            parseJson(jsonPart, objectMap);
            objects.add(objectMap);
        }
    }

    private void processMapSecondSubstring(Map<Object, Object> jsonMap, String secondSubstring, String name) {
        if (secondSubstring.contains(COLON_AND_LEFT_BRACE_AND_DOUBLE_QUOTE)) {
            List<Object> objects = new ArrayList<>();
            String[] jsonParts = secondSubstring.split(RIGHT_BRACE_AND_SYMBOLS);
            for (String jsonPart : jsonParts) {
                Map<Object, Object> objectMap = new HashMap<>();
                parseJson(jsonPart, objectMap);
                objects.add(objectMap);
            }
            jsonMap.put(name, objects);
        } else {
            Map<Object, Object> objectMap = new HashMap<>();
            parseJson(secondSubstring, objectMap);
            jsonMap.put(name, objectMap);
        }
    }

    private void processLine(String json, Map<Object, Object> jsonMap) {
        String[] jsonSplit = json.split(SYMBOLS);
        for (String line : jsonSplit) {
            if (checkLine(line)) {
                continue;
            }
            String[] lineSplit = line.split(REGEX_COLONS_AND_DOUBLE_QUOTES);
            String key = cleanLine(lineSplit[0]);
            String value = cleanLine(lineSplit[1]);
            jsonMap.put(key, value);
        }
    }

    private boolean checkLine(String line) {
        String cleaned = cleanLine(line);
        return cleaned.isEmpty() || cleaned.isBlank();
    }

    private String cleanLine(String json) {
        return json.replaceAll(REGEX_CLEAN_JSON, EMPTY);
    }
}
