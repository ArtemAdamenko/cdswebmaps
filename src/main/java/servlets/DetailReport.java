package servlets;

import com.google.gson.Gson;
import entities.DetailReportObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mapper.DataMapper;
import mybatis.MyBatisManager;
import org.apache.ibatis.session.SqlSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import static org.apache.ibatis.jdbc.SqlBuilder.*;

/**
 *
 * @author Adamenko Artem <adamenko.artem@gmail.com>
 * Формирование подробного отчета
 */
public class DetailReport extends HttpServlet {
     /*сообщение об ошибке*/
     final String SERVLET_ERROR = "Ошибка получения данных для детального отчета по ТС";
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
        //SqlSession session = RequestDataSessionManager.getRequestSession();
        SqlSession session = MyBatisManager.getDataSessionFactory().openSession();
        //#
        
        DataMapper mapper = session.getMapper(DataMapper.class);         
        //парсим выбранные в клиентском окне автобусы
        String objects = request.getParameter("objects");  
        JSONArray buses = new JSONArray();
        try{
            JSONParser parser = new JSONParser();
            buses = (JSONArray)parser.parse(objects);
        }catch(ParseException pe){
            Logger.getLogger(DetailReport.class.getName()).log(Level.SEVERE, null, pe);
        }
        
        String fromTime = request.getParameter("from");
        String toTime = request.getParameter("to");
        String routeName = request.getParameter("route");
        int routeID = mapper.getRouteId(routeName);


        try {
            String sid = getSid();
            //выполняем триггер
            mapper.getRepDetailMovObjects(fromTime, toTime, routeID, sid);
            session.commit();
            
            //#
            //RequestDataSessionManager.closeRequestSession();
            //session = RequestDataSessionManager.getRequestSession();
            session.close();
            session = MyBatisManager.getDataSessionFactory().openSession();
            //#
            
            mapper = session.getMapper(DataMapper.class);
            String wsql = "";
            for(int i = 0; i <= buses.size()-1; i++){
                JSONObject bus = (JSONObject)buses.get(i);
                wsql += "(a.PID = " + bus.get("proj_id_") + " and " + "a.OID = " + bus.get("obj_id_") + ")";
                if (i < buses.size()-1)
                    wsql += " or ";
            }

            List<DetailReportObject> resultObjects = mapper.selectPersonSql(sid,wsql);
            Gson gson = new Gson();  
            out.print(gson.toJson(resultObjects));
        } finally {        
            out.close();
        }
    }
    
    /**
     * Составной динамический запрос
     * @param params
     * @return string
     */
    public String selectPersonSql(Map<String, Object> params) {
      String sid = params.get("sid").toString();
      String wsql = params.get("wsql").toString();
      BEGIN(); // Clears ThreadLocal variable
      SELECT("a.PID, a.OID, (SELECT o.name_ FROM OBJECTS o WHERE o.OBJ_ID_ = a.OID and o.PROJ_ID_ = a.PID) as oname_, (SELECT bs.NAME_ FROM BUS_STATIONS bs WHERE bs.NUMBER_ = a.BSNUM and bs.ROUT_ = a.RID) as bsname_, (SELECT bs.CONTROL_ from BUS_STATIONS bs WHERE bs.NUMBER_ = a.BSNUM and bs.ROUT_ = a.RID) as bscontrol_, a.DT as dt");
      FROM("REP_FULLMOVE_OBJECTS_OF_ROUTE a");
      WHERE("a.UUID_ = " + sid.toString());
      AND();
      WHERE(wsql);
      ORDER_BY("a.PID ASC, a.OID ASC, a.DT ASC");
      return SQL();
    }
    
    /**
     * получение уникального id
     * @return string
     */
    private String getSid(){
        Date date = new Date();
        int h = date.getHours();
        int m = date.getMinutes();
        int s = date.getSeconds();
        
        Random randomGenerator = new Random();
        int ss = randomGenerator.nextInt(100);
        
        long result = ss * 1000000 + h * 1000 + m * 100 + s;
        return Long.toString(result);
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
             Logger.getLogger(DetailReport.class.getName()).log(Level.SEVERE, null, ex);
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
             Logger.getLogger(DetailReport.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Подробный отчет";
    }// </editor-fold>
}
