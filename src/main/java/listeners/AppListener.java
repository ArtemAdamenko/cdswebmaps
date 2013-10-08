package listeners;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import mybatis.MyBatisManager;
import mybatis.RequestDataSessionManager;
import mybatis.RequestProjectsSessionManager;
/**
 *
 * @author Artem Adamenko <adamenko.artem@gmail.com>
 * Инициализация объектов при запуске приложения
 */
public class AppListener implements ServletContextListener{
	ServletContext context;
        @Override
	public void contextInitialized(ServletContextEvent contextEvent) {
            try {
                MyBatisManager.initDBFactory("development", "Projects");
                MyBatisManager.initDBFactory("development", "Data");
                RequestProjectsSessionManager.initialize();
                RequestDataSessionManager.initialize();
            } catch (Exception ex) {
                Logger.getLogger(AppListener.class.getName()).log(Level.SEVERE, null, ex);
            }
		System.out.println("Context Created");
	}
        
        @Override
	public void contextDestroyed(ServletContextEvent contextEvent) {
		context = contextEvent.getServletContext();
		System.out.println("Context Destroyed");
	}
    
}
