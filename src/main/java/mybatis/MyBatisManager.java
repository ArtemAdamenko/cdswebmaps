package mybatis;

import mapper.Mapper;
import java.io.Reader;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.util.logging.Logger;

/**
 *
 * @author Artem Adamenko <adamenko.artem@gmail.com>
 */
public class MyBatisManager {
    /*Объект хранящий Sql сессии*/
    private SqlSessionFactory sqlSessionFactory;
    /*Запись в лог*/
    private final static Logger Log = Logger.getLogger(MyBatisManager.class.getName());
    
    /*
     * Инициализация подключения к БД
     */
    public void initFactory(String environment) throws Exception{
        try{
            Log.info("Запуск SqlSessionFactory");
            String resource = "mybatis/mybatis-config.xml";
            Reader reader = Resources.getResourceAsReader(resource);
            if (sqlSessionFactory == null){
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, environment);
            }
            sqlSessionFactory.getConfiguration().addMapper(Mapper.class);
        }catch(Exception e){
            Log.info("Ошибка подключения к БД: " + e);
            //throw new IOException("Ошибка подключения к БД : " + e); 
        }
    }
    
    /*
     * Возвращает singleton SqlSessionFactory
     */
    public SqlSessionFactory getSessionFactory() throws Exception{
        return sqlSessionFactory;
    }
}

