package servlets;

import com.google.gson.Gson;
import entities.SpeedBus;
import java.io.IOException;
import java.io.PrintWriter;
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
 * Обработка данных скорости по времени
 */
public class GetSpeedBus extends HttpServlet {

    /*Менеджер подключений к БД*/
     private static MyBatisManager manager = new MyBatisManager();
     /*Среда запуска приложения*/
     final String environment = "development";
     /*База данных для подключения*/
     final String DB = "Data";
     /*сообщение об ошибке*/
     final String SERVLET_ERROR = "Ошибка получения данных по скорости";
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
        Integer objId = Integer.valueOf(request.getParameter("obj"));
        Integer projId = Integer.valueOf(request.getParameter("proj"));
        String fromTime = request.getParameter("from_");
        String toTime = request.getParameter("to_");
        try {
           List<SpeedBus> speed = mapper.getSpeedBus(objId, projId, fromTime, toTime);
           Gson gson = new Gson();
           out.print(gson.toJson(speed));
           session.commit();
        } finally {         
            session.close();
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
         } catch (Exception ex) {
             Logger.getLogger(GetSpeedBus.class.getName()).log(Level.SEVERE, null, ex);
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
             Logger.getLogger(GetSpeedBus.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Получение данных скорости ТС по дате для диаграммы";
    }// </editor-fold>
}
