package servlets;

import com.google.gson.Gson;
import entities.BusObject;
import entities.Geocode;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mapper.DataMapper;
import mapper.ProjectsMapper;
import mybatis.MyBatisManager;
import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Adamenko Artem <adamenko.artem@gmail.com>
 * Получение автобусов по маршуту с условием и по маршрутам без условия 
 */
public class GetRouteBuses extends HttpServlet {

     /*сообщение об ошибке*/
     final String SERVLET_ERROR = "Ошибка получения автобусов по маршруту";
     /*Промежуточный список автобусов*/
     private List<BusObject> buses = new  ArrayList<BusObject>();
     /*Обработка JSON*/
     private Gson gson = new Gson();
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
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String routeName = request.getParameter("route");
        String routesNames = request.getParameter("routes");   
         
        try {
            //пришел запрос на автобусы по конкретным маршрутам
            if (routesNames != null){
                out.print(getBusesOfManyRoutes(routesNames));            
            //запрос на автобусы с одного маршрута
            }else{ 
                //получаем интервал времени
                String fromTime = request.getParameter("from");
                String toTime = request.getParameter("to");
                out.print(getBusesOfOneRoute(routeName, fromTime, toTime));
            }
        }catch(Exception e){
             Logger.getLogger(GetRouteBuses.class.getName()).log(Level.SEVERE, null, e);
        }finally{
            
        }
    }
    
    /**
     * Обработка запроса на автобусы с одного маршрута по интервалу времени
     * @param String routeName название маршрута
     * @param String from начало интервала времени
     * @param String to конец интервала времени
     * @return String автобусы с информацией по каждому в json
     */
    private String getBusesOfOneRoute(String routeName, String from, String to) throws Exception{
        //Инициализация подключения к БД Data

        //#
        //SqlSession session = RequestDataSessionManager.getRequestSession();
        SqlSession session = MyBatisManager.getDataSessionFactory().openSession();
        //#
        
        DataMapper mapper = session.getMapper(DataMapper.class);
        
        //результирующий список
        List<BusObject> resultBuses = new  ArrayList<BusObject>();
        try{
            //Получение списка автобусов
            int routeId = mapper.getRouteId(routeName);
            buses = mapper.getBuses(routeId, from, to);     

            //получение по каждому автобусу информации
            for (int i = 0 ; i <= buses.size()-1; i++){
                BusObject bus = new BusObject();
                bus.setName_(mapper.getNameofBus(buses.get(i).getProj_id_(), buses.get(i).getObj_id_()));
                bus.setObj_id_(buses.get(i).getObj_id_());
                bus.setProj_id_(buses.get(i).getProj_id_());
                //Ставим фактический адрес
                String address = Geocode.getReverseGeoCode(buses.get(i).getLast_lat_(), buses.get(i).getLast_lon_());
                bus.setAddress(address);
                resultBuses.add(bus);
            }
    
        }finally{
        }
        return gson.toJson(resultBuses);     
    }
    
    /**
     * Обработка запроса по конкретным маршрутам
     * @param String routesNames список названия маршрутов
     * @param String список автобусов с информацией
     */
    private String getBusesOfManyRoutes(String routesNames) throws Exception{
        //результирующий список
        String allRoutesBuses = "[";

        //инициализация подключений к бд Data и Projects

        SqlSession session = MyBatisManager.getProjectSessionFactory().openSession();

        
        //инициализация мапперов
        ProjectsMapper mapper = session.getMapper(ProjectsMapper.class);
        
        //парсим пришедший список маршрутов
        JSONArray routes = new JSONArray();
        try{
            JSONParser parser = new JSONParser();
            routes = (JSONArray)parser.parse(routesNames);
        }catch(ParseException pe){
            Logger.getLogger(DetailReport.class.getName()).log(Level.SEVERE, null, pe);
        }
        
        //получаем данные автобусов по всем маршрутам
        try{         
            for (int i = 0; i <= routes.size()-1; i++){
                JSONObject obj = (JSONObject)routes.get(i);
                int routeID = mapper.getRouteId(obj.get("route").toString());
                int projID = Integer.parseInt(obj.get("proj_ID").toString());
                buses = getAddresses(mapper.selectListObjectsWithoutStations(routeID, projID));
                allRoutesBuses += gson.toJson(buses).replaceAll("\\[|\\]", "") +",";
            }
            //валидация json
            allRoutesBuses = allRoutesBuses.replaceAll(",,", ",");
            allRoutesBuses = allRoutesBuses.substring(0, allRoutesBuses.length()-1);

        }finally{
        }
        return allRoutesBuses + "]";         
    }
    
    /**Получение физического адреса ТС
     * @param List<BusObject> Тс которым нужен адрес
     * @return List<BusObject> ТС с адресами
     */
    private static List<BusObject> getAddresses(List<BusObject> buses) throws IOException{
        for (int i = 0; i <= buses.size()-1; i++){
            BusObject bus = buses.get(i);
            String address = Geocode.getReverseGeoCode(bus.getLast_lat_(), bus.getLast_lon_());
            buses.get(i).setAddress(address);
        }
        return buses;
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
         } catch (Exception ex) {
             Logger.getLogger(GetRouteBuses.class.getName()).log(Level.SEVERE, null, ex);
         }
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
         } catch (Exception ex) {
             Logger.getLogger(GetRouteBuses.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Получение автобусов по маршуту с условием и по маршрутам без условия ";
    }// </editor-fold>
}
