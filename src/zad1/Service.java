/**
 * @author Bartosiak Michał S16484
 */

package zad1;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Service {

    String country;

    public Service() {
    }

    public Service(String country) {
        this.country = country;
    }

    public String getWeather(String city) {
//      https://api.openweathermap.org/data/2.5/weather?q=London&appid=9bec17884c34f7f5db8ee66087c1e02b
        String BASE_URL = "http://api.openweathermap.org/data/2.5/";
        String URL_CURRENT = "weather?";
        String PARAM_CITY_NAME = "q=";
        String PARAM_APPID = "appId=";
        String ACCESS_KEY = "9bec17884c34f7f5db8ee66087c1e02b";
        String PARAM_UNITS = "&units=metric";
        URL url_city_country = null;
        URL url_city = null;
        String json = "";

        try {
            url_city_country = new URL(BASE_URL + URL_CURRENT + PARAM_CITY_NAME + city + "," + CountryCode(country) + "&" + PARAM_APPID + ACCESS_KEY + PARAM_UNITS);
            url_city = new URL(BASE_URL + URL_CURRENT + PARAM_CITY_NAME + city + "&" + PARAM_APPID + ACCESS_KEY + PARAM_UNITS);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        json = getUrlContent(url_city_country);

        if (json.equals("")) {
            json = getUrlContent(url_city);
        }

//        try (BufferedReader in = new BufferedReader(
//                new InputStreamReader(url.openStream(), "UTF-8"))) {
//            String line;
//            while ((line = in.readLine()) != null) json += line;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return json;
    }

    public Double getRateFor(String currency_code) {
        String BASE_URL = "https://api.exchangeratesapi.io/";
        String ENDPOINT = "latest";
        String currency = CurrencyCode(country);
        URL url = null;
        Double currency_rate = null;
        String json_currency = "";

        try {
            url = new URL(BASE_URL + ENDPOINT + "?base=" + currency + "&symbols=" + currency_code);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        json_currency = getUrlContent(url);
        //        try (BufferedReader in = new BufferedReader(
//                new InputStreamReader(url.openStream(), "UTF-8"))) {
//            String line;
//            while ((line = in.readLine()) != null) json_currency += line;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        JSONObject exchangeRates;
        try {
            exchangeRates = new JSONObject(json_currency);

            if (!exchangeRates.isNull("rates")) {
                currency_rate = exchangeRates.getJSONObject("rates").getDouble(currency_code);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(json_currency);
        return currency_rate;
    }

    public Double getNBPRate() {
        String NBP_URL = "http://www.nbp.pl";
        String[] BASE_URL = {NBP_URL + "/kursy/kursya.html", NBP_URL + "/kursy/kursyb.html"};
        Double NBP_rate = null;

        String currency = CurrencyCode(country);

        for (int i = 0; i < 2 && NBP_rate == null; i++) {

            URL url = null;
            String cont = "";

            try {
                url = new URL(BASE_URL[i]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            cont = getUrlContent(url);

//            Pattern p2 = Pattern.compile(".+(<a href=\"(.+\\.xml)\").+");
//            Matcher m2 = p2.matcher(cont);
//
//            URL urlXml = null;
//            String xml = "";
//            if (m2.matches()) {
//                System.out.println(NBP_URL + m2.group(2));
//                try {
//                    urlXml = new URL(NBP_URL + m2.group(2));
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
//                xml = getUrlContent(urlXml);
//            }
//

//            Pattern p1 = Pattern.compile(".+(<td class=\"bgt.+>\\d+\\s(" + currency + "</td>\\s+.{1,23}\">(\\d,\\d+))</).+");
            Pattern p1 = Pattern.compile(".+(" + currency + "<\\/\\w+>\\s+<[a-zA-Z\\s=\"\\d]+>(\\d,\\d+)).+");
            Matcher m1 = p1.matcher(cont);

            if (!country.equals("Poland")) {
                if (m1.matches()) {
                    NBP_rate = Double.valueOf(m1.group(2).replace(',', '.'));
                }
            } else if (country.equals("Poland")) {
                NBP_rate = 1.0;
            }

        }
        return NBP_rate;
    }

    public String getWikiDescription(String city) {

        if (city != null) {
            return "https://en.wikipedia.org/wiki/" + city;
        }

        return city;
    }

    public String getUrlContent(URL urlContent) {
        String cont = "";
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(urlContent.openStream(), "UTF-8"))) {
            String line;
            while ((line = in.readLine()) != null) cont += line;
        } catch (FileNotFoundException e) {
            System.err.println("Wybrano miasto, które nie leży na terytorium podanego kraju.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cont;
    }

    public String CountryCode(String country) {
        Map<String, String> countries = new HashMap<>();
        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("", iso);
            countries.put(l.getDisplayCountry(new Locale("en", "UK")), iso);
            countries.put(l.getDisplayCountry(), iso);
        }
        return countries.get(country);
    }

    public String CurrencyCode(String country) {
        Currency c = Currency.getInstance(new Locale("", CountryCode(country)));
        return String.valueOf(c);
    }
//    public String CurrencyCode(String country) {
//        Map<String, String> countries = new HashMap<>();
//        for (String iso : Locale.getISOCountries()) {
//            Locale l = new Locale("", iso);
//            countries.put(l.getDisplayCountry(new Locale("en", "UK")), iso);
//            countries.put(l.getDisplayCountry(), iso);
//        }
//        Currency c = Currency.getInstance(new Locale("", countries.get(country)));
//        return String.valueOf(c);
//    }

    public String jsonPrettyString(String unformattedJsonString) {

        String prettyJSONBuilder = "";

        try {
            JSONObject jsonObj = new JSONObject(unformattedJsonString);

            if (!jsonObj.isNull("main")) {

                prettyJSONBuilder += "Loc: " + jsonObj.get("name") + "\n";
                prettyJSONBuilder += "Country: " + jsonObj.getJSONObject("sys").get("country") + "\n";
                prettyJSONBuilder += "Temperature: " + jsonObj.getJSONObject("main").get("temp") + " \u00B0" + "C" + "\n";
                prettyJSONBuilder += "Wind speed: " + jsonObj.getJSONObject("wind").get("speed") + "\n";
                prettyJSONBuilder += "Humidity: " + jsonObj.getJSONObject("main").get("humidity") + "\n";
                prettyJSONBuilder += "Pressure: " + jsonObj.getJSONObject("main").get("pressure");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return prettyJSONBuilder;
    }

}
