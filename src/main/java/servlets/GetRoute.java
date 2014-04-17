package servlets;

import com.google.gson.Gson;
import entities.Geocode;
import entities.Route;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mapper.DataMapper;
import mybatis.MyBatisManager;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Adamenko Artem <adamenko.artem@gmail.com>
 * Получение данных для отображения маршрута автобуса
 */
public class GetRoute extends HttpServlet {

     /*сообщение об ошибке*/
     final String SERVLET_ERROR = "Ошибка в GetRouteServlet";
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
        
        /*инициализация объектов*/
        //#
        //SqlSession sessionData = RequestDataSessionManager.getRequestSession();    
        SqlSession sessionData = MyBatisManager.getDataSessionFactory().openSession();
        //#
        
        DataMapper mapperData = sessionData.getMapper(DataMapper.class);
        
        String projectId = request.getParameter("proj");
        String busId = request.getParameter("bus");
        String fromTimeStr = request.getParameter("fromTime");
        String toTimeStr = request.getParameter("toTime");
        Integer typeAuto = 0;
        Gson gson = new Gson();
        try {
            int Bus_ID = Integer.parseInt(busId);
            int Proj_ID = Integer.parseInt(projectId);
            if (Proj_ID == 58)
                typeAuto = 1;
            else
                typeAuto = 0;
            /*тректория*/
            List<Route> route = mapperData.getRoute(Bus_ID, Proj_ID, fromTimeStr, toTimeStr);
            //typeAuto = mapperData.getTypeofBus(Proj_ID, Bus_ID);

            List<waitInterval> intervals = new ArrayList<waitInterval>();
            if (typeAuto == 1){
                if (route != null){
                    intervals = waitingAuto(route);
                    out.print(gson.toJson(typeAuto) + gson.toJson(route) + gson.toJson(intervals));
                }
            }else if(typeAuto == 0 || typeAuto == null){
                out.print(gson.toJson(typeAuto) + gson.toJson(route));
            }

        }catch(Exception e){
            
        }finally {            
        }
    }

    /**
     * внутренний класс для интервала простоев спец транспорта
     */
    public class waitInterval{
        /*Начало интервала*/
        private long startInterval;
        /*Конец интервала*/
        private long endInterval;
        /*Адрес простоя*/
        private String address;

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }

        public long getStartInterval() {
            return startInterval;
        }

        public long getEndInterval() {
            return endInterval;
        }

        public void setStartInterval(long startInterval) {
            this.startInterval = startInterval;
        }

        public void setEndInterval(long endInterval) {
            this.endInterval = endInterval;
        }
    }
    
    /**
     * Точки простоев спецтранспорта
     * @param route
     * @return List<waitInterval>
     * @throws ParseException
     * @throws IOException 
     */
    private  List<waitInterval> waitingAuto(List<Route> route) throws ParseException, IOException{
        //начальная точка отсчета
        Double lon = route.get(0).LON_;
        Double lat = route.get(0).LAT_;
        String time = route.get(0).TIME_;
        //промежуточные точки для сравнения с начальной
        double lon2;
        double lat2;
        String time2;
        
        /*промежуточные значения для записи*/
        long tempStart = 0;
        long tempEnd = 0;
        Double tempLon = 0.0;
        Double tempLat = 0.0;
        //список промежутков времени >5 минут
        List<waitInterval> intervals = new ArrayList<waitInterval>();

        //size-2 т.к. сравниваем попарно элементы и нам не надо IndexOut
        for (int i = 1; i <= route.size()-2; i++){
            lon2 = route.get(i).LON_;
            lat2 = route.get(i).LAT_;
            time2 = route.get(i).TIME_;

            //если ТС стоит
            if ((lon == lon2) && (lat == lat2)){
                long start = Timestamp.valueOf(time).getTime();
                long end = Timestamp.valueOf(time2).getTime();

                long temp = end - start;
                if (temp > 600000){
                    tempStart = start;
                    tempEnd = end;
                    tempLat = lat;
                    tempLon = lon;         
                }
                //если тс сдвинулся с места предыдущего местоположения
            }else{
                //и если у нас есть интервал простоя
                if (tempStart !=0 && tempEnd != 0 && tempLat != 0 && tempLon != 0){    
                    //создаем интервал
                    waitInterval interval = new waitInterval();
                    interval.setStartInterval(tempStart);
                    interval.setEndInterval(tempEnd);
                    interval.setAddress(Geocode.getReverseGeoCode(tempLat, tempLon));
                    intervals.add(interval);
                    //обнуляем переменные для дальнейшего интервала, иначе они будут и дальше подходить для интервала со своими значениями
                    tempEnd = 0;
                    tempStart = 0;
                    tempLat = 0.0;
                    tempLon = 0.0;
                }
                //берем новые координаты, для поиска следующего интервала
                lon = route.get(i).LON_;
                lat = route.get(i).LAT_;
                time = route.get(i).TIME_;
            }     
        }

        return intervals;
        
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
             Logger.getLogger(GetRoute.class.getName()).log(Level.SEVERE, SERVLET_ERROR, ex);
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
             Logger.getLogger(GetRoute.class.getName()).log(Level.SEVERE, SERVLET_ERROR, ex);
         }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Получение данных для отображения маршрута автобуса";
    }// </editor-fold>
}
