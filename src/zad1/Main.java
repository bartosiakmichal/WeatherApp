/**
 * @author Bartosiak Michał S16484
 */

package zad1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("GUI/weather_v2.fxml"));
        primaryStage.setTitle("City Info");

        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Service s = new Service("Italy");
        String weatherJson = s.getWeather("Rome");
        Double rate1 = s.getRateFor("USD");
        Double rate2 = s.getNBPRate();
        String wikiAdress = s.getWikiDescription("Warsaw");

        System.out.println("Pogoda w postaci json:\n" + weatherJson);
        System.out.println(rate1);
        System.out.println(rate2);
        System.out.println(wikiAdress);
        System.out.println("==========GUI=============");

        launch(args);
    }
}
