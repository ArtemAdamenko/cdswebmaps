package entities;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Artem Adamenko <adamenko.artem@gmail.com>
 * Класс формирования физического адреса по широте и долготе
 */
public class Geocode {
    public static String getReverseGeoCode(Double lat, Double lng) throws IOException, JSONException {
        final String baseUrl = "http://maps.googleapis.com/maps/api/geocode/json";// путь к Geocoding API по HTTP
        final Map<String, String> params = Maps.newHashMap();
        params.put("language", "ru");// язык данных, на котором мы хотим получить
        params.put("sensor", "false");// исходит ли запрос на геокодирование от устройства с датчиком местоположения
        // текстовое значение широты/долготы, для которого следует получить ближайший понятный человеку адрес, долгота и
        params.put("latlng", lat.toString()+","+lng.toString());
        final String url = baseUrl + '?' + encodeParams(params);// генерируем путь с параметрами
        String formattedAddress = null;
        try{
            final JSONObject response = Geocode.read(url);// делаем запрос к вебсервису и получаем от него ответ
            // как правило, наиболее подходящий ответ первый и данные об адресе можно получить по пути
            // //results[0]/formatted_address
            final JSONObject location = response.getJSONArray("results").getJSONObject(0);
            formattedAddress = location.getString("formatted_address");
        }catch(Exception e){
            //Logger.getLogger(Geocode.class.getName()).log(Level.SEVERE, null, "Ошибка получения адреса");
        }
        if (formattedAddress == null)
            formattedAddress = "Адрес не получен";
        return formattedAddress;
    }
    
    /*
     * Разбор ответа сервера
     * @param Reader данные
     * @return String ответ
     */
    private static String readAll(final Reader rd) throws IOException {
        final StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
    /*
     * Отправка запроса
     * @param String адрес запроса
     * @return JSONObject ответ сервера
     */
    public static JSONObject read(final String url) throws IOException, JSONException {
        final InputStream is = new URL(url).openStream();
        try {
            final BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            final String jsonText = readAll(rd);
            final JSONObject json = new JSONObject(jsonText);
            return json;
        }finally {
            is.close();
        }
    }
    /*
     * Генерация пути с параметрами
     * @param Map<String, String> параметры запроса
     * @return String путь(url) запроса
     */
    private static String encodeParams(final Map<String, String> params) {
        final String paramsUrl = Joiner.on('&').join(// получаем значение вида key1=value1&key2=value2...
        Iterables.transform(params.entrySet(), new Function<Map.Entry<String, String>, String>() {

            @Override
            public String apply(final Map.Entry<String, String> input) {
                try {
                    final StringBuffer buffer = new StringBuffer();
                    buffer.append(input.getKey());// получаем значение вида key=value
                    buffer.append('=');
                    buffer.append(URLEncoder.encode(input.getValue(), "utf-8"));// кодируем строку в соответствии со стандартом HTML 4.01
                    return buffer.toString();
                }catch (final UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
        return paramsUrl;
    }
}
