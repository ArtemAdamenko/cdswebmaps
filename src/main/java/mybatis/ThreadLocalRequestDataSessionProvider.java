package mybatis;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.ibatis.session.SqlSession;

/**
*Класс сессии БД Data для каждого потока
 * Adamenko Artem <adamenko.artem@gmail.com>
 */
class ThreadLocalRequestDataSessionProvider implements RequestSessionProvider {

    private final ThreadLocal<SqlSession> thSession = new ThreadLocal<SqlSession>();

    
    
    
    @Override
    public SqlSession getRequestSession() {
        SqlSession session = thSession.get();
        if (session == null) {
            try {
                session = MyBatisManager.getDataSessionFactory().openSession();
            } catch (Exception ex) {
                Logger.getLogger(ThreadLocalRequestDataSessionProvider.class.getName()).log(Level.SEVERE, null, ex);
            }
            thSession.set(session);
        }
        return session;
    }

    @Override
    public boolean isRequestSessionOpen() {
        return thSession.get() != null;
    }

    @Override
    public void closeRequestSession() {
        SqlSession session = thSession.get();
        if (session != null) {
            try {
                session.close();
            } finally {
                thSession.remove();
            }
        }
    }
}
