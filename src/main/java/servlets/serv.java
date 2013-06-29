/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.Gson;
import com.mycompany.cdswebmaps.App;
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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mapper.Mapper;
import mybatis.MyBatisManager;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Администратор
 */
public class serv extends HttpServlet {

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
        try {
            //App.main();
            manager.initFactory("development");
            SqlSession session = manager.getSessionFactory().openSession();
            /*Забираем маппер из сессии*/
            Mapper mapper = session.getMapper(Mapper.class);
            /*Получаем id поль-ля*/
            int userId = mapper.selectUserId("ponomarev");
            /*Получаем проекты и маршруты по каждому в соответствии с id поль-ля*/
            //List<Map> routes = mapper.selectProjsAndRoutes(10);
           // session.close();
            //Map<Integer, List<Integer>> newRoutes = mapFromListOfMap(routes);
           // getObjects(newRoutes);
           // session.close();
            //System.out.println(newRoutes.toString());
       /* }catch(ResourceException e){
            Logger.getLogger(GetBusesServlet.class.getName()).log(Level.SEVERE, null, e);
        } */
        }finally {            
            out.close();
        }
    }
    
    /*Получить автобусы в виде json
     * @param Map<Integer, List<Integer>> проекты с маршрутами
     */
    public static String getObjects(Map<Integer, List<Integer>> projects) throws Exception
    {
        /*Открываем сессию для запросов*/
        SqlSession session = manager.getSessionFactory().openSession();
        Mapper mapper = session.getMapper(Mapper.class);
        /*результирующий список объектов по маршруту*/
        List<BusObject> buses = new ArrayList<BusObject>();
        Gson gson = new Gson();
        String jsonBuses = "";
        try{
            /*Проход по всем проектам*/
            for (List<Integer> routes : projects.values()){
                /*Проход по всем маршрутам*/
                for (int i = 0; i <= routes.size()-1; i++){
                    if (routes.get(i) != 0){
                        /*Объекты-автобусы по каждому маршруту*/
                        buses = mapper.selectObjects(routes.get(i));
                    }
                }
                /*Преобразование в json автобусов по маршруту*/
                jsonBuses += gson.toJson(buses);
                //System.out.println(jsonBuses.toString());
            }
        return jsonBuses;
        }catch(Exception e){
            throw new Exception(e);
        }
        finally{
            session.commit();
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
