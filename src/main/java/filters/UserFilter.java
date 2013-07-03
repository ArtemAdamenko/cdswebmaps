package filters;

import java.io.*; 
import javax.servlet.*; 
import javax.servlet.http.*; 
  


/**
 *
 * @author Adamenko Artem <adamenko.artem@gmail.com>
 * Фильтр работы Веб-приложения
 */
  public class UserFilter implements Filter 
  { 
       private FilterConfig config = null; 
       private boolean active = false; 
       
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
              HttpServletResponse res = (HttpServletResponse)response;
              String site = req.getServerName() + req.getContextPath();

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
                chain.doFilter(request, response);
              else{
                RequestDispatcher dispatcher = request.getRequestDispatcher("/");
                dispatcher.forward(request, response);
                
              }
       } 
       
       @Override
       public void destroy() 
       { 
              config = null; 
       } 
  } 