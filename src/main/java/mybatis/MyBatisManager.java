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
    /*Объект хранящий Sql Project сессии*/
    private SqlSessionFactory sqlProjectSessionFactory;
    /*Объект хранящий Sql Data сессии*/
    private SqlSessionFactory sqlDataSessionFactory;
    /*Запись в лог*/
    private final static Logger Log = Logger.getLogger(MyBatisManager.class.getName());
    
    /*
     * Инициализация подключения к БД
     * @param String environment
     */
    public void initFactory(String environment, String db) throws Exception{
        if (db.equals("Projects")){     
            try{
                Log.info("Запуск SqlProjectSessionFactory");
                String resource = "mybatis/mybatis-config-projects.xml";
                Reader reader = Resources.getResourceAsReader(resource);
                if (sqlProjectSessionFactory == null){
                    sqlProjectSessionFactory = new SqlSessionFactoryBuilder().build(reader, environment);
                    sqlProjectSessionFactory.getConfiguration().addMapper(ProjectsMapper.class);
                }            
            }catch(Exception e){
                Log.info("Ошибка подключения к БД Projects: " + e); 
            }
        }if (db.equals("Data")){
            try{
                Log.info("Запуск SqlDataSessionFactory");
                String resource = "mybatis/mybatis-config-data.xml";
                Reader reader = Resources.getResourceAsReader(resource);
                if (sqlDataSessionFactory == null){
                    sqlDataSessionFactory = new SqlSessionFactoryBuilder().build(reader, environment);
                    sqlDataSessionFactory.getConfiguration().addMapper(DataMapper.class);
                }
                
            }catch(Exception e){
                Log.info("Ошибка подключения к БД Data: " + e); 
            }
        }
    }
    
    /*
     * Возвращает singleton SqlSessionFactory
     * @return SqlSessionFactory
     */
    public SqlSessionFactory getProjectSessionFactory() throws Exception{
        return sqlProjectSessionFactory;
    }
    
    public SqlSessionFactory getDataSessionFactory() throws Exception{
        return sqlDataSessionFactory;
    }
}

