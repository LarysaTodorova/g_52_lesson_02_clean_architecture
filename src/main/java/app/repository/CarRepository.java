package app.repository;

import app.domain.Car;

import java.util.List;

public interface CarRepository {

    Car save(Car car);

    List<Car> getAll();

    Car getById(Long id);

    void update(Car car);

    void deleteById(Long id);

}
