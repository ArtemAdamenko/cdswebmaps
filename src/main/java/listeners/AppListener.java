package listeners;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import mybatis.MyBatisManager;
/**
 *
 * @author Artem Adamenko <adamenko.artem@gmail.com>
 * Инициализация объектов при запуске приложения
 */
public class AppListener implements ServletContextListener{
	ServletContext context;
        /*Менеджер подключений к БД*/
        private static MyBatisManager manager = new MyBatisManager();
        @Override
	public void contextInitialized(ServletContextEvent contextEvent) {
            try {
                manager.initFactory("development", "Projects");
                manager.initFactory("development", "Data");
            } catch (Exception ex) {
                Logger.getLogger(AppListener.class.getName()).log(Level.SEVERE, null, ex);
            }
		System.out.println("Context Created");
		context = contextEvent.getServletContext();
		// set variable to servlet context
		//context.setAttribute("TEST", "TEST_VALUE");
	}
        @Override
	public void contextDestroyed(ServletContextEvent contextEvent) {
		context = contextEvent.getServletContext();
		System.out.println("Context Destroyed");
	}
    
}
