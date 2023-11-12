package ru.clevertec.data;

import lombok.Builder;
import lombok.Data;
import ru.clevetec.entity.Product;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder(setterPrefix = "with")
public class ProductTestData {

    @Builder.Default
    private UUID id = UUID.fromString("f04255d2-64fa-40c3-a040-156fd40bbe17");

    @Builder.Default
    private String name = "Tomato";

    @Builder.Default
    private BigDecimal price = BigDecimal.valueOf(2.13);

    public Product buildProduct() {
        return new Product(id, name, price);
    }
}
