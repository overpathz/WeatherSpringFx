package org.example;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import org.example.configuration.MyConfig;
import org.example.error.CityNotFoundException;
import org.example.service.WeatherFinderAPI;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PrimaryController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button button;

    @FXML
    private TextField cityInput;

    @FXML
    private Text enterCityPlainText;

    @FXML
    private Text label_temp;

    @FXML
    private Text label_feels;

    @FXML
    private Text label_min;

    @FXML
    private Text label_max;

    @FXML
    private Text label_desc;

    @FXML
    private Text temp;

    @FXML
    private Text notFound;

    @FXML
    private Text feels;

    @FXML
    private Text min;

    @FXML
    private Text max;

    @FXML
    private Text description;

    @FXML
    private ImageView image;

    private final ApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
    private WeatherFinderAPI weatherFinderAPI = context.getBean("weatherFinder", WeatherFinderAPI.class);

    private Map<String, String> weatherDataMap;
    private List<Shape> elementsFx;

    @FXML
    void getWeather(ActionEvent event) {
        processInput();
    }

    @FXML
    void initialize() throws IOException {
        // sending request to reduce latency of response
        try {
            weatherFinderAPI.getWeather("");
        } catch (CityNotFoundException e) {
            ;
        }

        elementsFx = new ArrayList<>(List.of(temp, feels, min, max, description, label_temp,
                label_feels, label_max, label_min, label_desc));
    }

    private void processInput() {
        String city = cityInput.getText();

        try {
            weatherDataMap = weatherFinderAPI.getWeather(city);
            notFound.setText("");
            inputFields();
        } catch (CityNotFoundException e) {
            setFieldsVisibility(false);
            notFound.setText("City not found");
            System.out.println(e.getMessage());
        }
    }

    private void inputFields() {
        setFieldsVisibility(true);
        temp.setText(weatherDataMap.get("temp"));
        feels.setText(weatherDataMap.get("feelsLike"));
        max.setText(weatherDataMap.get("tempMax"));
        min.setText(weatherDataMap.get("tempMin"));
        description.setText(weatherDataMap.get("description"));
    }

    private void setFieldsVisibility(boolean value) {
        elementsFx.forEach(x->x.setVisible(value));
    }
}
