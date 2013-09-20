package servlets;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



/**
 *
 * @author Artem Adamenko <adamenko.artem@gmail.com>
 * Сервлет для прямого и обратного геокодирования
 */
public class GeocodeServlet extends HttpServlet {
     /*сообщение об ошибке*/
     final String SERVLET_ERROR = "Ошибка геокодирования";
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson(); 
        
        String coords = request.getParameter("coords");
        
        try {
            //String lat = request.getParameter("lat");
            //String lng = request.getParameter("lng");
            
            JSONArray coordsObjects = new JSONArray();
            try{
                
                //JSONParser parser = new JSONParser();
                coordsObjects = new JSONArray(coords);
            }catch(JSONException pe){
                Logger.getLogger(DetailReport.class.getName()).log(Level.SEVERE, null, pe);
            }
            
            List<String> addresses = new ArrayList<String>();
            for (int i = 0; i <= coordsObjects.length()-1; i++){
                JSONObject elem = (JSONObject)coordsObjects.get(i);
                addresses.add(getReverseGeoCode(Double.parseDouble(elem.get("lon").toString()), Double.parseDouble(elem.get("lat").toString())));
            }
            out.print(gson.toJson(addresses));
        } finally {            
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (JSONException ex) {
             Logger.getLogger(GeocodeServlet.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    /*Обратное геокодирование
     * @param Double широта
     * @param Double долгота
     * @return String адрес
     */
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
            final JSONObject response = GeocodeServlet.read(url);// делаем запрос к вебсервису и получаем от него ответ
            // как правило, наиболее подходящий ответ первый и данные об адресе можно получить по пути
            // //results[0]/formatted_address
            final JSONObject location = response.getJSONArray("results").getJSONObject(0);
            formattedAddress = location.getString("formatted_address");
        }catch(Exception e){
            //Logger.getLogger(GeocodeServlet.class.getName()).log(Level.SEVERE, null, "Ошибка получения адреса");
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
    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(GeocodeServlet.class.getName()).log(Level.SEVERE, SERVLET_ERROR, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Модуль геокодинга";
    }// </editor-fold>
}
