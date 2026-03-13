package app.controller;

import app.domain.Car;
import app.service.CarService;

import java.math.BigDecimal;
import java.util.List;

public class CarController {

    private final CarService service;

    public CarController(CarService service) {
        this.service = service;
    }

    public Car save(String brand, int year, double price) {
        Car car = new Car(brand, year, new BigDecimal(price));
        return service.save(car);
    }

    public List<Car> getAll() {
        return service.getAll();
    }

    public Car getById(Long id) {
        return service.getById(id);
    }

    public BigDecimal getAveragePrice() {
        return service.getCarAveragePrice();
    }
}
