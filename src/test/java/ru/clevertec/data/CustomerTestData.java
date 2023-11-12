package ru.clevertec.data;

import lombok.Builder;
import lombok.Data;
import ru.clevetec.entity.Customer;
import ru.clevetec.entity.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder(setterPrefix = "with")
public class CustomerTestData {

    @Builder.Default
    private UUID uuid = UUID.fromString("1ef4ea52-aa15-4778-ac41-89df4d368cff");

    @Builder.Default
    private String firstName = "Aleksandr";

    @Builder.Default
    private String lastName = "Petrov";

    @Builder.Default
    private LocalDate dateBirth = LocalDate.of(1983, 7, 11);

    @Builder.Default
    private List<Order> orders = List.of(OrderTestData.builder().build().buildOrder());

    public Customer buildCustomer() {
        return new Customer(uuid, firstName, lastName, dateBirth, orders);
    }
}
