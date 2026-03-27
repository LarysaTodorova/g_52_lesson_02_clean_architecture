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
            // Эта строка кода подгружает необходимый драйвер БД
            // в память работающего приложения, чтобы драйвер был
            // доступен во время выполнения программы.
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

            String query = "INSERT INTO car (brand, year, price) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(
                    query,
                    Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1, car.getBrand());
            preparedStatement.setInt(2, car.getYear());
            preparedStatement.setBigDecimal(3, car.getPrice());

            preparedStatement.executeUpdate();

            ResultSet keys = preparedStatement.getGeneratedKeys();

            if (keys.next()) {
                Long id = keys.getLong(1);
                return new Car(id, car.getBrand(), car.getYear(), car.getPrice());
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Car> getAll() {
        // Открываем соединение с БД
        try (Connection connection = getConnection()) {

            // Создаём запрос, который собираемся отправить в БД
            String query = "SELECT * FROM car";
            // Получаем объект, который умеет отправлять запросы в БД
            Statement statement = connection.createStatement();
            // Отправляем запрос в БД и получаем от неё ответ
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
        // Открываем соединение с БД (и автоматически закроем его после выполнения try)
        try (Connection connection = getConnection()) {

            // SQL-запрос: найти машину по id
            String query = "SELECT * FROM car WHERE id = ?";

            // Подготавливаем запрос (PreparedStatement защищает от SQL-инъекций)
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            // Подставляем значение вместо ? (первый параметр = id)
            preparedStatement.setLong(1, id);

            // Выполняем запрос и получаем результат (таблицу)
            ResultSet resultSet = preparedStatement.executeQuery();

            // Переходим к первой строке результата
            // Если строки нет → значит в БД нет такой машины → возвращаем null
            if (!resultSet.next()) {
                return null;
            }

            // Читаем данные из найденной строки (из колонок таблицы)
            Long currentId = resultSet.getLong("id");
            String brand = resultSet.getString("brand");
            int year = resultSet.getInt("year");
            BigDecimal price = resultSet.getBigDecimal("price");

            // Создаём объект Car из данных БД и возвращаем его
            return new Car(currentId, brand, year, price);

            // Если произошла ошибка — оборачиваем её в RuntimeException
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Car car) {
        try (Connection connection = getConnection()) {


            String query = "UPDATE car SET brand = ?, year = ?, price = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);


            preparedStatement.setString(1, car.getBrand());
            preparedStatement.setInt(2, car.getYear());
            preparedStatement.setBigDecimal(3, car.getPrice());
            preparedStatement.setLong(4, car.getId());

            // Выполняем UPDATE-запрос
            // executeUpdate() возвращает количество изменённых строк
            int affectedRows = preparedStatement.executeUpdate();

            // Если ни одна строка не обновилась → значит такого id нет в БД
            if (affectedRows == 0) {
                throw new RuntimeException("Car not found with id = " + car.getId());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Connection connection = getConnection()) {
            String query = "DELETE FROM car WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, id);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new RuntimeException("Car not found with id = " + id);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
