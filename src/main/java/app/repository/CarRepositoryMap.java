package app.repository;

import app.domain.Car;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarRepositoryMap implements CarRepository {

    private final Map<Long, Car> database = new HashMap<>();
    private long currentId = 0;

    @Override
    public Car save(Car car) {
        car.setId(++currentId);
        database.put(currentId, car);
        return car;
    }

    @Override
    public List<Car> getAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public Car getById(Long id) {
        return database.get(id);
    }

    @Override
    public void update(Car car) {
        database.put(car.getId(), car);
    }

    @Override
    public void deleteById(Long id) {
        database.remove(id);
    }
}
