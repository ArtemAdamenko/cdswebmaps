package servlets;

import com.google.gson.Gson;
import entities.RouteReportObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mapper.DataMapper;
import java.util.Random;
import mybatis.MyBatisManager;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Adamenko Artem <adamenko.artem@gmail.com>
 * Обработка данных для отчета по рейсам
 */
public class ReportRoute extends HttpServlet {

     /*сообщение об ошибке*/
     final String SERVLET_ERROR = "Ошибка обработки данных ReportRoute Servlet";
     /*Костыль для начала времени*/
     final String START_TIME = " 00:00:00";
     /*Костыль для конца времени*/
     final String END_TIME = " 23:59:00";
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
        
        String routeName = request.getParameter("route");
        int routeId = mapper.getRouteId(routeName);
        String from_date = request.getParameter("date") + START_TIME;
        String to_date = request.getParameter("date") + END_TIME;
        try {
            String sid = getSid();
            mapper.getReport4(from_date, to_date, routeId, sid);
            session.commit();
            
            //#
            session.close();
            session = MyBatisManager.getDataSessionFactory().openSession();
            //RequestDataSessionManager.closeRequestSession();
            //session = RequestDataSessionManager.getRequestSession();
            //#
            
            mapper = session.getMapper(DataMapper.class);
            List<RouteReportObject> report = mapper.getDataToRouteReport(sid, routeId);
            Gson gson = new Gson();
            String jsonReport = gson.toJson(report);
            out.print(jsonReport);
        } finally {   
            out.close();
        }
    }

    /*получение уникального id*/
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
        return "Отчет по рейсам";
    }// </editor-fold>
}
