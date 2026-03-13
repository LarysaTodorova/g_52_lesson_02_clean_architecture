package app.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Car {

    private Long id;
    private String brand;
    private int year;
    private BigDecimal price;

    public Car(String brand, int year, BigDecimal price) {
        this.brand = brand;
        this.year = year;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return year == car.year && Objects.equals(id, car.id) && Objects.equals(brand, car.brand) && Objects.equals(price, car.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brand, year, price);
    }

    @Override
    public String toString() {
        return String.format("Car: id - %d, brand - %s, year - %d, price - %s.",
                id, brand, year, price);
    }
}
