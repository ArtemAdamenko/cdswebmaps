package listeners;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import mybatis.MyBatisManager;
import mybatis.RequestDataSessionManager;
import mybatis.RequestProjectsSessionManager;
import org.apache.ibatis.session.SqlSession;
/**
 *
 * @author Artem Adamenko <adamenko.artem@gmail.com>
 * Инициализация объектов при запуске приложения
 */
public class AppListener implements ServletContextListener{
	ServletContext context;
        Throwable problem = null;
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
                    System.out.println("Conn close");
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
                    System.out.println("Conn close");
                    RequestProjectsSessionManager.closeRequestSession();
                }
             }
	}
    
}
