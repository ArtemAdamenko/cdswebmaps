package servlets;

import com.google.gson.Gson;
import entities.BusObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.resource.ResourceException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mapper.Mapper;
import mybatis.MyBatisManager;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Adamenko Artem <adamenko.artem@gmail.com>
 * Сервлет отдающий данные об автобусах
 */
public class GetBusesServlet extends HttpServlet {

    /*Менеджер подключений к БД*/
     private static MyBatisManager manager = new MyBatisManager();
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
        manager.initFactory("development");
        SqlSession session = manager.getSessionFactory().openSession();
        Mapper mapper = session.getMapper(Mapper.class);
        Cookie[] cookies = request.getCookies();
        String username = "";
        try {
            for (int i = 0; i <= cookies.length-1; i++){
                if (cookies[i].getName().equals("session_id"))
                     username = cookies[i].getValue();
            }
            /*Получаем id поль-ля*/
            int userId = mapper.selectUserId(username);
            /*Получаем проекты и маршруты по каждому в соответствии с id поль-ля*/
            List<Map> routes = mapper.selectProjsAndRoutes(userId);
            /*преобразовываем данные в удобный формат*/
            Map<Integer, List<Integer>> newRoutes = mapFromListOfMap(routes);
            /*преобразовываем данные в json*/
            String res = getObjects(newRoutes);
            session.commit();
            out.print(res);       
        }catch(ResourceException e){
            Logger.getLogger(GetBusesServlet.class.getName()).log(Level.SEVERE, null, e);
        }finally {
            session.close();
            out.close();
        }
    }
    
   
    
    /*Получить автобусы в виде json
     * @param Map<Integer, List<Integer>> проекты с маршрутами
     * @return String
     */
    public static String getObjects(Map<Integer, List<Integer>> projects) throws Exception
    {
        /*Открываем сессию для запросов*/
        SqlSession session = manager.getSessionFactory().openSession();
        Mapper mapper = session.getMapper(Mapper.class);
        /*результирующий список объектов по маршруту*/
        List<BusObject> buses = new ArrayList<BusObject>();
        Gson gson = new Gson();
        String allJsonBuses="[";
       
        try{
            /*Проход по всем проектам*/
            for (List<Integer> routes : projects.values()){
                /*Проход по всем маршрутам*/
                for (int i = 0; i <= routes.size()-1; i++){
                    if (routes.get(i) != 0){
                        /*Объекты-автобусы по каждому маршруту*/
                        buses = mapper.selectObjects(routes.get(i));
                        if (!buses.isEmpty())
                            allJsonBuses += gson.toJson(buses).replaceAll("\\[|\\]", "") +",";
                    }
                }      
            }
        session.commit();
        /*стирание лишних символов дял валидности json*/
        allJsonBuses = allJsonBuses.replaceAll(",,", ",");
        allJsonBuses = allJsonBuses.substring(0, allJsonBuses.length()-1);
        return allJsonBuses + "]";
        }catch(Exception e){
            throw new Exception(e);
        }
        finally{
            session.close();
        }
    }
    
    /*Преобразование в Map из List<Map>
     * @param List<Map> Список с Map
     * @return Map<Integer, List<Integer>>
     */
    public static Map<Integer,List<Integer>> mapFromListOfMap (List<Map> listOfMap ) 
    {
        Map<Integer,List<Integer>> map = new HashMap<Integer,List<Integer>>();
        for(int i = 0; i < listOfMap.size(); i++) {
            Integer key = (Integer)listOfMap.get(i).get("PROJ");
            Integer value = (Integer)listOfMap.get(i).get("ROUTE");
            if (map.containsKey(key)){
                List<Integer> listValues = map.get(key);
                listValues.add(value);
                map.put(key, listValues);
                
            }else{
                List<Integer> newList = new ArrayList<Integer>();
                newList.add(value);
                map.put(key, newList);
            }        
        }
        return map;
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
             Logger.getLogger(GetBusesServlet.class.getName()).log(Level.SEVERE, null, ex);
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
             Logger.getLogger(GetBusesServlet.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
