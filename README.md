# Задание:

1. Создать любой gradle проект
2. Проект должен быть совместим с java 17
3. Придерживаться GitFlow: master -&gt; develop -&gt; feature/fix
4. Разработать библиотеку, которая будет формировать на основе Java
   класса json и обратно
5. Использовать рефлексию
6. Предусмотреть возможную вложенность объектов (рекурсия),
   смотрите приложение I
7. Покрыть код unit tests (можно использовать jackson/gson)
8. Использовать lombok
9. Заполнить и отправить форму

### Приложение I

public class Product {
private UUID id;
private String name;
private Double price;
}

public class Order {
private UUID id;
private List&lt;Product&gt; products;
private OffsetDateTime createDate;
}

public class Customer {
private UUID id;
private String firstName;
private String lastName;
private LocalDate dateBirth;
private List<Order> orders;
}

# Литература:

### String

https://medium.com/@abhiroop.nray/understanding-string-in-java-31519b8d7b9c

### String - Core Java

https://gyansetu-java.gitbook.io/core-java/string

### Json

https://medium.com/@stasonmars/введение-в-json-c798d2723107
https://www.hostinger.com/tutorials/what-is-json

### Reflection

https://www.geeksforgeeks.org/reflection-in-java/
https://www.oracle.com/java/technologies/javareflection.html
https://www.baeldung.com/java-reflection