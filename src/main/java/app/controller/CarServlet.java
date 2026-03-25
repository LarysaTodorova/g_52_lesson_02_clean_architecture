package app.controller;

import app.domain.Car;
import app.repository.CarRepository;
import app.repository.CarRepositoryHibernate;
import app.repository.CarRepositoryJdbc;
import app.repository.CarRepositoryMap;
import app.service.CarService;
import app.service.CarServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.List;

//Это сервлет. Сервлет - это такой класс, задача которого принимать
// запросы, обрабатывать их и отдавать клиенту ответы.
public class CarServlet extends HttpServlet {

    private final CarService service;

    public CarServlet() {
        CarRepository repository = new CarRepositoryHibernate();
        service = new CarServiceImpl(repository);
    }

    ObjectMapper mapper = new ObjectMapper();

    // Это метод doGet. Для чего он нужен?
    // Когда на наше приложение будет приходить GET-запрос,
    // Tomcat будет создавать Джава-объекты запроса и ответа,
    // затем вызывать наш метод doGet и передать эти объекты в метод аргументами.
    // Наша задача - прочитать всё, что нужно, из объекта req (request)
    // и записать всю информацию для клиента в объект resp (response).
    // После того как метод обработает, Tomcat прочитает всю информацию
    // из объекта resp, упакует её в http-ответ и отправит обратно клиенту.
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Если в запросе будет присутствовать id, то его значение запишется в переменную.
        // А если id не будет в запросе, то в переменную запишется null.
        String id = req.getParameter("id");
        // получаем поток для записи тела HTTP-ответа клиенту
        Writer writer = resp.getWriter();
        resp.setContentType("application/json");

        if (id == null) {
            // Здесь будем отдавать клиенту все автомобили
            List<Car> cars = service.getAll();
            // преобразуем объекты cars в JSON и отправляем их клиенту
            mapper.writeValue(writer, cars);
        } else {
            // Здесь будем отдавать клиенту один автомобиль, соответствующий id
            Long numericId = Long.parseLong(id);
            Car car = service.getById(numericId);
            // преобразуем объект car в JSON и отправляем его клиенту
            mapper.writeValue(writer, car);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String brand = req.getParameter("brand");
        int year = Integer.parseInt(req.getParameter("year"));
        BigDecimal price = new BigDecimal(req.getParameter("price"));

        Writer writer = resp.getWriter();

        Car car = service.save(new Car(brand, year, price));
        mapper.writeValue(writer, car);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        Long id = Long.parseLong(req.getParameter("id"));
        String brand = req.getParameter("brand");
        int year = Integer.parseInt(req.getParameter("year"));
        BigDecimal price = new BigDecimal(req.getParameter("price"));

        Writer writer = resp.getWriter();

        Car car = new Car(brand, year, price);
        car.setId(id);

        service.update(car);
        mapper.writeValue(writer, "success");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        Writer writer = resp.getWriter();
        resp.setContentType("application/json");

        Long numericId = Long.parseLong(id);
        service.delete(numericId);
        mapper.writeValue(writer, "success");
    }

}
