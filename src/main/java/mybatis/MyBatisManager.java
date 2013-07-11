package mybatis;

import mapper.ProjectsMapper;
import java.io.Reader;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.util.logging.Logger;
import mapper.DataMapper;

/**
 *
 * @author Artem Adamenko <adamenko.artem@gmail.com>
 * Класс для работы с сессиями
 */
public class MyBatisManager {
    /*Объект хранящий Sql сессии*/
    private SqlSessionFactory sqlSessionFactory;
    /*Запись в лог*/
    private final static Logger Log = Logger.getLogger(MyBatisManager.class.getName());
    
    /*
     * Инициализация подключения к БД
     * @param String environment
     */
    public void initFactory(String environment, String db) throws Exception{
        Log.info("Запуск SqlSessionFactory");
        if (db.equals("Projects")){
            try{
                String resource = "mybatis/mybatis-config-projects.xml";
                Reader reader = Resources.getResourceAsReader(resource);
                if (sqlSessionFactory == null){
                    sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, environment);
                }
                sqlSessionFactory.getConfiguration().addMapper(ProjectsMapper.class);
            }catch(Exception e){
                Log.info("Ошибка подключения к БД Projects: " + e); 
            }
        }if (db.equals("Data")){
            try{
                String resource = "mybatis/mybatis-config-data.xml";
                Reader reader = Resources.getResourceAsReader(resource);
                if (sqlSessionFactory == null){
                    sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, environment);
                }
                sqlSessionFactory.getConfiguration().addMapper(DataMapper.class);
            }catch(Exception e){
                Log.info("Ошибка подключения к БД Data: " + e); 
            }
        }
    }
    
    /*
     * Возвращает singleton SqlSessionFactory
     * @return SqlSessionFactory
     */
    public SqlSessionFactory getSessionFactory() throws Exception{
        return sqlSessionFactory;
    }
}

