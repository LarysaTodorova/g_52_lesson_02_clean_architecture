package app.repository;

import app.domain.Car;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class CarRepositoryHibernate implements CarRepository {


    // Менеджер сущностей - это объект, через который происходит взаимодействие с БД
    private final EntityManager entityManager;

    public CarRepositoryHibernate() {
        /*
        Чтобы сконфигурировать энтити менеджер, создаём специальный объект
        для конфигурации и при помощи метода configure указываем путь к файлу конфигурации.
        Путь указывается относительно папки resources.
        После этого метода при помощи метода buildSessionFactory создаём специальный
        объект "фабрики сессий", при помощи которого и создаём объект энтити менеджера
         */
        entityManager = new Configuration()
                .configure("mysql.cfg.xml")
                .buildSessionFactory()
                .createEntityManager();
    }

    @Override
    public Car save(Car car) {
        // Получаем объект транзакции у энтити менеджера
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            // Открываем транзакцию
            transaction.begin();
            // Сохраняем автомобиль в БД (метод persist служит для того, чтобы сохранить объект в базе)
            entityManager.persist(car);
            // Закрываем транзакцию
            transaction.commit();
            // Возвращаем автомобиль (ему уже будет присвоен ИД)
            return car;
        } catch (Exception e) {
            // В случае ошибки проверяем, активна ли транзакция
            if (transaction.isActive()) {
                // и если да, то выполняем откат транзакции
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Car> getAll() {
        return entityManager.createQuery("from Car", Car.class).getResultList();
    }

    @Override
    public Car getById(Long id) {
        return entityManager.find(Car.class, id);
    }

    @Override
    public void update(Car car) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Car foundCar = getById(car.getId());
            foundCar.setPrice(car.getPrice());
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Car carForDelete = getById(id);
            entityManager.remove(carForDelete);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }
}
