package filters;

import java.io.*; 
import javax.servlet.*; 
import javax.servlet.http.*; 
import mybatis.RequestDataSessionManager;
import mybatis.RequestProjectsSessionManager;
import org.apache.ibatis.session.SqlSession;
  


/**
 *
 * @author Adamenko Artem <adamenko.artem@gmail.com>
 * Фильтр работы Веб-приложения
 */
  public class UserFilter implements Filter 
  { 
      /*Настройки фильтра*/
       private FilterConfig config = null; 
       /*Активность*/
       private boolean active = false;
       
       Throwable problem = null;
       
       @Override
       public void init (FilterConfig config) throws ServletException 
       { 
              this.config = config; 
              String act = config.getInitParameter("active"); 
              if (act != null) 
                  active = (act.toUpperCase().equals("TRUE")); 
       } 
       
       @Override
       public void doFilter (ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException                         
       { 
              boolean exist = false;
              HttpServletRequest req = (HttpServletRequest)request;

              if (active)  
              { 
                    Cookie[] cookies = req.getCookies();
                    if (cookies != null){
                        for (int i = 0; i <= cookies.length-1; i++){
                            if (cookies[i].getName().equals("session_id"))
                                exist = true;         
                        }
                    }
              }
              if (exist)
                //chain.doFilter(request, response);
                  try {
                        chain.doFilter(request, response);
                    } catch (Throwable t) {
                        // If an exception is thrown somewhere down the filter chain,
                        // we still want to execute our after processing, and then
                        // rethrow the problem after that.
                        problem = t;
                    }
              else{
                RequestDispatcher dispatcher = request.getRequestDispatcher("/");
                dispatcher.forward(request, response);
                
              }
              
              //проверка на существовании сессии БД Data
              if (RequestDataSessionManager.isRequestSessionOpen()) {
                SqlSession session = RequestDataSessionManager.getRequestSession();

                try {
                    // commit or rollback transaction
                    if (problem == null) {
                        session.commit();
                    } else {
                        session.rollback();
                    }
                } finally {
                    // close session
                    RequestDataSessionManager.closeRequestSession();
                }
             }
              
              //проверка на существовании сессии БД Data
              if (RequestProjectsSessionManager.isRequestSessionOpen()) {
                SqlSession session = RequestProjectsSessionManager.getRequestSession();

                try {
                    // commit or rollback transaction
                    if (problem == null) {
                        session.commit();
                    } else {
                        session.rollback();
                    }
                } finally {
                    // close session
                    RequestProjectsSessionManager.closeRequestSession();
                }
             }

            // If there was a problem, we want to rethrow it if it is
            // a known type, otherwise log it.
            if (problem != null) {
                if (problem instanceof ServletException) {
                    throw (ServletException) problem;
                } else {
                    throw new ServletException(problem);
                }
            }
       } 
       
       @Override
       public void destroy() 
       { 
              config = null; 
       } 
  } 