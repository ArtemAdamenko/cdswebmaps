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
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mapper.ProjectsMapper;
import mybatis.MyBatisManager;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Adamenko Artem <adamenko.artem@gmail.com>
 * Сервлет отдающий данные об автобусах
 */
public class GetBusesServlet extends HttpServlet {
     /*сообщение об ошибке*/
     final String SERVLET_ERROR = "Ошибка обработки данных GetBusesServlet";
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
        
        //#
        //SqlSession session = RequestProjectsSessionManager.getRequestSession();
        SqlSession session = MyBatisManager.getProjectSessionFactory().openSession();
        //#
        
        ProjectsMapper mapper = session.getMapper(ProjectsMapper.class);
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
            out.print(res);   
        }finally {
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
        
        //#
        //SqlSession session = RequestProjectsSessionManager.getRequestSession();  
        SqlSession session = MyBatisManager.getProjectSessionFactory().openSession();
        //#
        
        ProjectsMapper mapper = session.getMapper(ProjectsMapper.class);
        /*результирующий список объектов по маршруту*/
        List<BusObject> buses;
        Gson gson = new Gson();
        String allJsonBuses="[";
       
        try{
            /*Проход по всем проектам*/
            for (Map.Entry<Integer, List<Integer>> routes: projects.entrySet()){
                Integer key = routes.getKey();
                List<Integer> value = routes.getValue();
                /*Проход по всем маршрутам*/
                for (int i = 0; i <= value.size()-1; i++){
                        //Объекты-автобусы по каждому маршруту
                        buses = mapper.selectListObjects(value.get(i), key); 
                        if (!buses.isEmpty())
                            allJsonBuses += gson.toJson(buses).replaceAll("\\[|\\]", "") +",";
                }     
            }
            /*стирание лишних символов для валидности json*/
            allJsonBuses = allJsonBuses.replaceAll(",,", ",");
            allJsonBuses = allJsonBuses.substring(0, allJsonBuses.length()-1);
        }
        finally{
        }
        return allJsonBuses + "]";
    }
    
    
    /*Преобразование в Map из List<Map>
     * @param List<Map> Список с Map
     * @return Map<Integer, List<Integer>>
     */
    public static Map<Integer,List<Integer>> mapFromListOfMap (List<Map> listOfMap ) 
    {
        Map<Integer,List<Integer>> map = new HashMap<Integer,List<Integer>>();
        for(int i = 0; i <= listOfMap.size()-1; i++) {
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
             Logger.getLogger(GetBusesServlet.class.getName()).log(Level.SEVERE, SERVLET_ERROR, ex);
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
             Logger.getLogger(GetBusesServlet.class.getName()).log(Level.SEVERE, SERVLET_ERROR, ex);
         }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Сервлет отдающий данные об автобусах";
    }// </editor-fold>
}
