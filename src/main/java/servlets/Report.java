package servlets;

import com.google.gson.Gson;
import entities.ReportObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
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
 * Класс для формирования данных для отчетов
 */
public class Report extends HttpServlet {


     /*сообщение об ошибке*/
     final String SERVLET_ERROR = "Ошибка обработки данных Report Servlet";
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

        SqlSession session = MyBatisManager.getProjectSessionFactory().openSession();
        
        ProjectsMapper mapper = session.getMapper(ProjectsMapper.class);
        
        Cookie[] cookies = request.getCookies();
        String username = "";
        Gson gson = new Gson();
        
        /*берем из куки имя пользователя*/
        for (int i = 0; i <= cookies.length-1; i++){
            if (cookies[i].getName().equals("session_id"))
                username = cookies[i].getValue();
        }
        try {
            /*подготовка данных для клиентской стороны*/
            List<ReportObject> Objects = mapper.getDataToReport(username);
            String buses = gson.toJson(Objects);
            Map<Integer,String> userProject = mapper.getUserProject(username);
            String jsonUserProject = gson.toJson(userProject) + "@";
            out.println(jsonUserProject);
            out.println(buses);
        } finally {   
            out.close();
        }
    }
    
    /**Сортировка
     * @param List<ReportObject>
     * @return List<ReportObject>
     */
    private static List<ReportObject> sortDataList(List<ReportObject> Objects) throws UnsupportedEncodingException, ParseException{
        Collections.sort(Objects, new Comparator<ReportObject>(){
            @Override
            public int compare(ReportObject o1, ReportObject o2) {
               return o1.NAME_.compareTo(o2.NAME_);
            }
        });
        return Objects;
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
             Logger.getLogger(Report.class.getName()).log(Level.SEVERE, SERVLET_ERROR, ex);
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
             Logger.getLogger(Report.class.getName()).log(Level.SEVERE, SERVLET_ERROR, ex);
         }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Класс для формирования данных для отчетов";
    }// </editor-fold>
}
