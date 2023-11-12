package ru.clevetec.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Customer {

    private UUID uuid;

    private String firstName;

    private String lastName;

    private LocalDate dateBirth;

    private List<Order> orders;
}
