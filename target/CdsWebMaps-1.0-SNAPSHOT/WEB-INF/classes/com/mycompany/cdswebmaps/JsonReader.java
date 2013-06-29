package com.mycompany.cdswebmaps;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Adamenko Artem <adamenko.artem@gmail.com>
 * Класс для обращения к вебсервисами и получения ответа в json
 */
public class JsonReader {

    /*Запрос к службе геокодирования (по адресу получаем координаты)*/
    public static void main(final String[] args) throws IOException, JSONException {
    final String baseUrl = "http://maps.googleapis.com/maps/api/geocode/json";// путь к Geocoding API по HTTP
    final Map<String, String> params = Maps.newHashMap();
    params.put("sensor", "false");// исходит ли запрос на геокодирование от устройства с датчиком местоположения
    params.put("address", "Россия, Воронеж, переулок Полтавский, 38");// адрес, который нужно геокодировать
    final String url = baseUrl + '?' + encodeParams(params);// генерируем путь с параметрами
    System.out.println(url);// Путь, что бы можно было посмотреть в браузере ответ службы
    final JSONObject response = JsonReader.read(url);// делаем запрос к вебсервису и получаем от него ответ
    // как правило наиболее подходящий ответ первый и данные о координатах можно получить по пути
    // //results[0]/geometry/location/lng и //results[0]/geometry/location/lat
    JSONObject location = response.getJSONArray("results").getJSONObject(0);
    location = location.getJSONObject("geometry");
    location = location.getJSONObject("location");
    final double lng = location.getDouble("lng");// долгота
    final double lat = location.getDouble("lat");// широта
    System.out.println(String.format("%f,%f", lat, lng));// итоговая широта и долгота
}
    /*Обратная операция (по координатам определяем адрес)*/
    /*public static void main(final String[] args) throws IOException, JSONException {
    final String baseUrl = "http://maps.googleapis.com/maps/api/geocode/json";// путь к Geocoding API по HTTP
    final Map<String, String> params = Maps.newHashMap();
    params.put("language", "ru");// язык данных, на котором мы хотим получить
    params.put("sensor", "false");// исходит ли запрос на геокодирование от устройства с датчиком местоположения
    // текстовое значение широты/долготы, для которого следует получить ближайший понятный человеку адрес, долгота и
    // широта разделяется запятой, берем из предыдущего примера
    Double lat = 51.673130;
    Double lon = 39.261495;
    params.put("latlng", lat.toString()+","+lon.toString());
    final String url = baseUrl + '?' + encodeParams(params);// генерируем путь с параметрами
    System.out.println(url);// Путь, что бы можно было посмотреть в браузере ответ службы
    final JSONObject response = JsonReader.read(url);// делаем запрос к вебсервису и получаем от него ответ
    // как правило, наиболее подходящий ответ первый и данные об адресе можно получить по пути
    // //results[0]/formatted_address
    final JSONObject location = response.getJSONArray("results").getJSONObject(0);
    final String formattedAddress = location.getString("formatted_address");
    System.out.println(formattedAddress);// итоговый адрес
}*/
    
    private static String readAll(final Reader rd) throws IOException {
        final StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject read(final String url) throws IOException, JSONException {
        final InputStream is = new URL(url).openStream();
        try {
            final BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            final String jsonText = readAll(rd);
            final JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
    private static String encodeParams(final Map<String, String> params) {
    final String paramsUrl = Joiner.on('&').join(// получаем значение вида key1=value1&key2=value2...
            Iterables.transform(params.entrySet(), new Function<Entry<String, String>, String>() {

                @Override
                public String apply(final Entry<String, String> input) {
                    try {
                        final StringBuffer buffer = new StringBuffer();
                        buffer.append(input.getKey());// получаем значение вида key=value
                        buffer.append('=');
                        buffer.append(URLEncoder.encode(input.getValue(), "utf-8"));// кодируем строку в соответствии со стандартом HTML 4.01
                        return buffer.toString();
                    } catch (final UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }));
    return paramsUrl;
}
}
