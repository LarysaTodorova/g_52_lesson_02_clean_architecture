package app.service;

import app.domain.Car;
import app.repository.CarRepository;

import java.math.BigDecimal;
import java.util.List;

public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public Car save(Car car) {
        return carRepository.save(car);
    }

    @Override
    public List<Car> getAll() {
        return carRepository.getAll();
    }

    @Override
    public Car getById(Long id) {
        return carRepository.getById(id);
    }

    @Override
    public BigDecimal getCarAveragePrice() {
        return new BigDecimal(getAll()
                .stream()
                .mapToDouble(c -> c.getPrice().doubleValue())
                .average()
                .orElse(0.0));
    }
}
