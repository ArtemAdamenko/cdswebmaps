package servlets;

import com.google.gson.Gson;
import entities.RouteReportObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mapper.DataMapper;
import mybatis.MyBatisManager;
import java.util.Random;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Администратор
 */
public class ReportRoute extends HttpServlet {
     /*Менеджер подключений к БД*/
     private static MyBatisManager manager = new MyBatisManager();
     /*Среда запуска приложения*/
     final String environment = "development";
     /*База данных для подключения*/
     final String DB = "Data";
     /*сообщение об ошибке*/
     final String SERVLET_ERROR = "Ошибка обработки данных ReportRoute Servlet";
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
        manager.initDBFactory(environment, DB);
        SqlSession session = manager.getDataSessionFactory().openSession();
        DataMapper mapper = session.getMapper(DataMapper.class);
        String routeName = request.getParameter("route");
        int routeId = mapper.getRouteId(routeName);
        String from_date = request.getParameter("date") + " 00:00:00";
        String to_date = request.getParameter("date") + " 23:59:00";
        try {
            String sid = getSid();
            mapper.getReport4(from_date, to_date, routeId, sid);
            session.commit();
            session.close();
            SqlSession session1 = manager.getDataSessionFactory().openSession();
            DataMapper mapper1 = session1.getMapper(DataMapper.class);
            List<RouteReportObject> report = mapper1.getDataToRouteReport(sid, routeId);
            Gson gson = new Gson();
            String jsonReport = gson.toJson(report);
            out.print(jsonReport);
        } finally {            
            out.close();
        }
    }

    private String getSid(){
        //DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
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
             Logger.getLogger(ReportRoute.class.getName()).log(Level.SEVERE, null, ex);
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
             Logger.getLogger(ReportRoute.class.getName()).log(Level.SEVERE, null, ex);
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
