package client;

import app.controller.CarController;
import app.domain.Car;
import app.repository.CarRepository;
import app.repository.CarRepositoryMap;
import app.service.CarService;
import app.service.CarServiceImpl;

public class Client {

    public static void main(String[] args) {

        CarRepository repository = new CarRepositoryMap();
        CarService service = new CarServiceImpl(repository);
        CarController controller = new CarController(service);

        controller.save("Audi", 2025, 58000);
        controller.save("BMW", 2012, 12000);
        controller.save("Honda", 2024, 20000);

        System.out.println("All Cars:");
        controller.getAll().forEach(System.out::println);
//        for (Car car : controller.getAll()) {
//            System.out.println(car);
//        }

        System.out.println("Car with id 2:");
        System.out.println(controller.getById(2L));

        System.out.println("All cars average price:");
        System.out.println(controller.getAveragePrice());

    }
}
