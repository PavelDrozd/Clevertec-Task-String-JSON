package ru.clevertec.data;

import lombok.Builder;
import lombok.Data;
import ru.clevetec.entity.Order;
import ru.clevetec.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.UUID;

@Data
@Builder(setterPrefix = "with")
public class OrderTestData {

    @Builder.Default
    private UUID id = UUID.fromString("03f32fa5-94e0-4e5a-bea4-00c0a1974750");

    @Builder.Default
    private Map<String, Product> products = Map.of(
            "first", ProductTestData.builder().build().buildProduct(),
            "second", ProductTestData.builder()
                    .withId(UUID.fromString("b7bd909e-1fdd-4ee9-9447-dc0e09a79e89"))
                    .withName("Apple")
                    .withPrice(BigDecimal.valueOf(1000))
                    .build().buildProduct());
    @Builder.Default
    private OffsetDateTime createDate = OffsetDateTime.of(
            LocalDate.of(2022, 1, 1),
            LocalTime.of(12, 24, 0),
            ZoneOffset.UTC);

    public Order buildOrder() {
        return new Order(id, products, createDate);
    }
}
