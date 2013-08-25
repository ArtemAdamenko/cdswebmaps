package servlets;

import com.google.gson.Gson;
import entities.BusObject;
import entities.DetailReportObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
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

/**
 *
 * @author Adamenko Artem <adamenko.artem@gmail.com>
 * Формирования подробного отчета
 */
public class DetailReport extends HttpServlet {
    /*Менеджер подключений к БД*/
     private static MyBatisManager manager = new MyBatisManager();
     /*Среда запуска приложения*/
     final String environment = "development";
     /*База данных для подключения*/
     final String DB = "Data";
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
        manager.initDBFactory(environment, DB);
        SqlSession session = manager.getDataSessionFactory().openSession();
        DataMapper mapper = session.getMapper(DataMapper.class);
        Gson gson = new Gson();
        String objects = gson.fromJson(request.getParameter("objects"), null);
        String fromTime = request.getParameter("from");
        String toTime = request.getParameter("to");
        Integer route = Integer.valueOf(request.getParameter("route"));
        try {
            String sid = getSid();
            //выполняем триггер
            mapper.getRepDetailMovObjects(fromTime, toTime, route, sid);
            session.commit();
            session.close();
            SqlSession session1 = manager.getDataSessionFactory().openSession();
            DataMapper mapper1 = session1.getMapper(DataMapper.class);
            //парсим выбранные в клиентском окне автобусы
            List<BusObject> buses = (List<BusObject>) gson.fromJson(request.getParameter("objects"), BusObject.class);
            String wsql = "(";
            for(int i = 0; i <= buses.size()-1; i++){
                wsql += "a.PID = " + buses.get(i).getProj_id_() + " and " + "a.OID = " + buses.get(i).getObj_id_();
            }
            wsql += ")";
            //забираем сформировавшиеся данные
            List<DetailReportObject> resultObjects = mapper1.getDetailReport(sid, wsql);
            session1.close(); 
            out.print(gson.toJson(resultObjects));
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
        return "Short description";
    }// </editor-fold>
}
