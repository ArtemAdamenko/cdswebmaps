package mybatis;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.ibatis.session.SqlSession;

/**
 *Класс сессии БД Projects для каждого потока
 * Adamenko Artem <adamenko.artem@gmail.com>
 * 
 */
class ThreadLocalRequestProjectsSessionProvider implements RequestSessionProvider {

    private final ThreadLocal<SqlSession> thSession = new ThreadLocal<SqlSession>();

    /*
     * Получить сессию
     * @return SqlSession
     */
    @Override
    public SqlSession getRequestSession() {
        SqlSession session = thSession.get();
        if (session == null) {
            try {
                session = MyBatisManager.getProjectSessionFactory().openSession();
            } catch (Exception ex) {
                Logger.getLogger(ThreadLocalRequestProjectsSessionProvider.class.getName()).log(Level.SEVERE, null, ex);
            }
            thSession.set(session);
        }
        return session;
    }

    /*
     * Проверка на создание сессии
     * @return boolean
     */
    @Override
    public boolean isRequestSessionOpen() {
        return thSession.get() != null;
    }
    
    /*
     *  Закрытие сессии 
     */
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
