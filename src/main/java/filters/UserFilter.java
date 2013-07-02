package filters;

import java.io.*; 
import javax.servlet.*; 
import javax.servlet.http.*; 
  
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
              if (active)  
              {
                    HttpServletRequest req = (HttpServletRequest)request;
                    Cookie[] cookies = req.getCookies();
                    for (int i = 0; i <= cookies.length-1; i++){
                        if (cookies[i].getName().equals("username"))
                            exist = true;         
                    }
                    if (exist)
                        chain.doFilter(request, response);
                    else{
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/");
                        dispatcher.forward(request, response);
                    }
              } 
              //chain.doFilter(request, response); 
       } 
       
       @Override
       public void destroy() 
       { 
              config = null; 
       } 
  } 