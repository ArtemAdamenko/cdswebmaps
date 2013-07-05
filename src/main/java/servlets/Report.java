/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.Gson;
import entities.ReportObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
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
import mapper.Mapper;
import mybatis.MyBatisManager;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Администратор
 */
public class Report extends HttpServlet {

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
        /*инициализация объектов*/
        manager.initFactory("development");
        SqlSession session = manager.getSessionFactory().openSession();
        Mapper mapper = session.getMapper(Mapper.class);
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
            String buses = getDataFromObjects(Objects);    
            Map<Integer,String> userProject = mapper.getUserProject(username);
            String jsonUserProject = gson.toJson(userProject) + "@";
            out.println(jsonUserProject);
            out.println(buses);
        }catch(RuntimeException e){
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, "Ошибка обработки данных Report Servlet"+e);
        } finally {      
            session.commit();
            session.close();
            out.close();
        }
    }
    
    private static String getDataFromObjects(List<ReportObject> Objects) throws UnsupportedEncodingException, ParseException{
        Gson gson = new Gson();
        Collections.sort(Objects, new Comparator<ReportObject>(){
            @Override
            public int compare(ReportObject o1, ReportObject o2) {
               return o1.NAME_.compareTo(o2.NAME_);
            }
        });
        String report = gson.toJson(Objects); 
        return report;
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
             Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
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
             Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
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
