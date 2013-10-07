package mybatis;

import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Adamenko Artem <adamenko.artem@gmail.com>
 */
interface RequestSessionProvider {

    /** Возвращает сессию текущего запроса. */
    SqlSession getRequestSession();
    /** Была ли открыта сессия текущего запросы. */
    boolean isRequestSessionOpen();
    /** Закрывает сессию текущего запроса. */
    void closeRequestSession();
}
