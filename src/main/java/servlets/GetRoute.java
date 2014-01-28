package servlets;

import com.google.gson.Gson;
import entities.Route;
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
import mybatis.RequestDataSessionManager;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Adamenko Artem <adamenko.artem@gmail.com>
 * Получение данных для отображения маршрута автобуса
 */
public class GetRoute extends HttpServlet {

     /*сообщение об ошибке*/
     final String SERVLET_ERROR = "Ошибка в GetRouteServlet";
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
        SqlSession sessionData = RequestDataSessionManager.getRequestSession();    
        DataMapper mapperData = sessionData.getMapper(DataMapper.class);
        
        String projectId = request.getParameter("proj");
        String busId = request.getParameter("bus");
        String fromTimeStr = request.getParameter("fromTime");
        String toTimeStr = request.getParameter("toTime");
        try {
            /*тректория*/
            List<Route> route = mapperData.getRoute(Integer.parseInt(busId), Integer.parseInt(projectId), fromTimeStr, toTimeStr);
            /*пересчет ккординат для точек траектории*/
            /*for (int i = 0; i <= route.size()-1; i++){
                Double lon = convertCoord(route.get(i).getLON_());
                Double lat = convertCoord(route.get(i).getLAT_());
                System.out.println(lon + "-" + lat);
                
                route.get(i).setLON_(lon);
                route.get(i).setLAT_(lat);
            } */
            Gson gson = new Gson();
            out.print(gson.toJson(route));
        } finally {            

        }
    }

    
    private static Double convertCoord(Double coord){   
        double x = coord;
        double y = x;
        y = (int)x/100;
        x=x-y*100;
        double x1=(int)x;
        y=y+x1/60+(x-x1)/60;
        return y;
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
             Logger.getLogger(GetRoute.class.getName()).log(Level.SEVERE, SERVLET_ERROR, ex);
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
             Logger.getLogger(GetRoute.class.getName()).log(Level.SEVERE, SERVLET_ERROR, ex);
         }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Получение данных для отображения маршрута автобуса";
    }// </editor-fold>
}
