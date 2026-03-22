package app.repository;

import app.domain.Car;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static app.constants.Constants.*;

public class CarRepositoryJdbc implements CarRepository {

    private Connection getConnection() {
        try {
            Class.forName(DB_DRIVER_PATH);
            String dbUrl = DB_ADDRESS + DB_NAME;
            return DriverManager.getConnection(dbUrl, DB_USERNAME, DB_PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Car save(Car car) {
        try (Connection connection = getConnection()) {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Car> getAll() {
        try (Connection connection = getConnection()) {

            String query = "SELECT * FROM car";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<Car> cars = new ArrayList<>();

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String brand = resultSet.getString("brand");
                int year = resultSet.getInt("year");
                BigDecimal price = resultSet.getBigDecimal("price");

                Car car = new Car(id, brand, year, price);
                cars.add(car);
            }
            return cars;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Car getById(Long id) {
        try (Connection connection = getConnection()) {

            String query = "SELECT * FROM car WHERE id = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }

            Long currentId = resultSet.getLong("id");
            String brand = resultSet.getString("brand");
            int year = resultSet.getInt("year");
            BigDecimal price = resultSet.getBigDecimal("price");

            return new Car(currentId, brand, year, price);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Car car) {
        try (Connection connection = getConnection()) {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Connection connection = getConnection()) {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
