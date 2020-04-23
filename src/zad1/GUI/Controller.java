/**
 * @author Bartosiak Michał S16484
 */

package zad1.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import zad1.Service;

public class Controller {

    String countryText;
    String cityText;
    String currencyCodeText;

    @FXML
    private TextField country;

    @FXML
    private TextField city;

    @FXML
    private TextField PLNRate;

    @FXML
    private WebView webView;

    @FXML
    private TextArea weatherLabel;

    @FXML
    private TextArea currencyRateLabel;

    @FXML
    private TextArea PLNrateLabel;

    @FXML
    private Label allertLabel;

    @FXML
    private void processGetData(ActionEvent actionEvent) {
        actionEvent.getSource();
        countryText = country.getText();
        cityText = city.getText();
        currencyCodeText = PLNRate.getText().toUpperCase();

        if (!countryText.equals("") && !cityText.equals("") && !currencyCodeText.equals("")) {
            allertLabel.setText("");

            System.out.println("Wpisano:\n\t-Państwo: " + countryText + "\n\t-Miasto: " + cityText + "\n\t-Waluta: " + currencyCodeText);
            initialize();
            Service s = new Service(countryText);

            String weatherJson = s.getWeather(cityText);
            Double rate1 = s.getRateFor(currencyCodeText);
            Double rate2 = s.getNBPRate();
            rate1 *= 10000;
            rate1 = Double.valueOf(Math.round(rate1));
            rate1 /= 10000;

            weatherLabel.setText(s.jsonPrettyString(weatherJson));
            currencyRateLabel.setText("1 " + s.CurrencyCode(countryText) + " = " + String.valueOf(rate1) + " " + currencyCodeText);
            PLNrateLabel.setText("1 PLN = " + String.valueOf(rate2) + " " + s.CurrencyCode(countryText));
        } else {
            allertLabel.setText("Input parameters");
        }
    }

    @FXML
    public void initialize() {
        WebEngine web = webView.getEngine();

        Service s = new Service();
        String urlWeb = s.getWikiDescription(cityText);
        web.load(urlWeb);
    }


}

