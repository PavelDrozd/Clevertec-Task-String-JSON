package ru.clevertec.mapper.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.data.CustomerTestData;
import ru.clevertec.data.OrderTestData;
import ru.clevetec.entity.Customer;
import ru.clevetec.entity.Order;
import ru.clevetec.exception.JsonEmptyValidationException;
import ru.clevetec.exception.JsonNullValidationException;
import ru.clevetec.exception.JsonValidationException;
import ru.clevetec.exception.ObjectSerializeException;
import ru.clevetec.mapper.impl.StringJsonMapperImpl;

import java.text.SimpleDateFormat;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringJsonMapperImplTest {

    ObjectMapper objectMapper;

    StringJsonMapperImpl stringJsonMapperImpl;

    public StringJsonMapperImplTest() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        stringJsonMapperImpl = new StringJsonMapperImpl();
    }

    @Test
    public void toJsonShouldReturnExpectedJson() throws JsonProcessingException {
        // given
        Customer customer = CustomerTestData.builder().build().buildCustomer();
        String expected = objectMapper.writeValueAsString(customer);

        // when
        String actual = stringJsonMapperImpl.toJson(customer);

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void toJsonShouldThrowObjectSerializeException() {
        //when
        Exception exception = assertThrows(ObjectSerializeException.class, () -> stringJsonMapperImpl.toJson(null));
        String expectedMessage = "Object is null";
        String actualMessage = exception.getMessage();

        //then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void toObjectShouldReturnExpectedObject() throws JsonProcessingException {
        // given
        Customer customer = CustomerTestData.builder().build().buildCustomer();

        String json = objectMapper.writeValueAsString(customer);
        Object expected = objectMapper.readValue(json, Customer.class);

        // when
        Customer actual = stringJsonMapperImpl.toObject(json, Customer.class);

        // then
        assertEquals(expected, actual);
    }

    @Test
    public void toObjectShouldThrowJsonNullValidationException() {
        //when
        Exception exception = assertThrows(JsonNullValidationException.class,
                () -> stringJsonMapperImpl.toObject(null, Integer.class));
        String expectedMessage = "Json is null";
        String actualMessage = exception.getMessage();

        //then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void toObjectShouldThrowJsonEmptyValidationException() {
        //when
        Exception exception = assertThrows(JsonEmptyValidationException.class,
                () -> stringJsonMapperImpl.toObject(" ", Integer.class));
        String expectedMessage = "Json is empty";
        String actualMessage = exception.getMessage();

        //then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @ParameterizedTest
    @MethodSource("stringProvider")
    public void toObjectShouldThrowJsonValidationException(String str) throws JsonProcessingException {
        //given
        Order order = OrderTestData.builder().build().buildOrder();

        String json = objectMapper.writeValueAsString(order);
        String invalidJson = json.replace(str, "");

        //when
        Exception exception = assertThrows(JsonValidationException.class,
                () -> stringJsonMapperImpl.toObject(invalidJson, Order.class));
        String expectedMessage = "Json is not valid";
        String actualMessage = exception.getMessage();

        //then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    public static Stream<String> stringProvider() {
        return Stream.of("{", "}", "\"");
    }
}
