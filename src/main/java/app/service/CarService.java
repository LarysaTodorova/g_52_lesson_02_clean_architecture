package app.service;

import app.domain.Car;

import java.math.BigDecimal;
import java.util.List;

public interface CarService {

    Car save(Car car);

    List<Car> getAll();

    Car getById(Long id);

    BigDecimal getCarAveragePrice();
}
