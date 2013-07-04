/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entities.ReportObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class Report extends HttpServlet {

    /*Менеджер подключений к БД*/
     private static MyBatisManager manager = new MyBatisManager();
     private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.US);
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
        List<ReportObject> Objects = mapper.getDataToReport("ptp2");
        String res = getDataFromObjects(Objects);
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println(res);
        } finally {            
            out.close();
        }
    }

    private static String createHeader(){
        String web = "";
        web += "<table align=center BORDER=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse: collapse; border: 0px solid black; width:830px; background-color:#ffffff;\"><tr bgcolor:'#cccccc'><td>№ п/п</td><td>ГосНомерТС</td><td>Марка ТС</td><td>Установщик</td><td>Маршрут следования</td><td>Время прохождения последней остановки</td><td>Время последнего отклика</td>";
        return web;
    }
    
    private static String getDataFromObjects(List<ReportObject> Objects) throws UnsupportedEncodingException, ParseException{
        /*Применение рефлексии для получения полей класса*/
        Class c = Objects.get(0).getClass();
        Field[] publicFields = c.getFields();
        /*результирующий массив*/
        String[][] result = new String[Objects.size()][publicFields.length];
        String web = createHeader();
        
        /*проход по всем объектам*/
        for (int i = 0; i <= Objects.size()-1; i++){
            /*проход по всем полям объекта*/
            int j = i;
            web +="<tr>";
            web +="<td class=\"pj_obj_item_num\" style=\"width:50px;\">";
            web += j+1;
            web +="</td>";
            
            web  += "<td class=\"pj_obj_item\"  style=\"width:80px;\">"+Objects.get(i).NAME_+"</td>";
            web += "<td class=\"pj_obj_item\" style=\"width:120px;\">"+Objects.get(i).CBNAME_+"</td>";

            web += "<td class=\"pj_obj_item\" style=\"width:120px;\">"+Objects.get(i).PVNAME+"</td>";
            web +="<td class=\"pj_obj_item\" style=\"width:100px;\">" +Objects.get(i).RNAME_+"</td>";
            web +="<td style=\"width:100px;\">";
            if (Objects.get(i).LAST_STATION_TIME_ !=null)
                web  += simpleDateFormat.format(Objects.get(i).LAST_STATION_TIME_);
            web +="</td>";
            web +="<td style=\"width:100px;\" class=\"pj_obj_item\">";
            if (Objects.get(i).LAST_TIME_ !=null)
                web += simpleDateFormat.format(Objects.get(i).LAST_TIME_);
            web +="</td>";
            web +="</tr>";
        }
        
        return web;
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
