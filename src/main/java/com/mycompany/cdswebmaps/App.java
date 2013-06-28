package com.mycompany.cdswebmaps;

import Entities.User;
import com.mycompany.cdswebmaps.JsonReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mapper.Mapper;
import mybatis.MyBatisManager;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, JSONException, Exception
    {
        System.out.println( "Hello World!" );
        //JsonReader.main(args);
        MyBatisManager manager = new MyBatisManager();
        /*Выбираем среду параметров БД*/
        manager.initFactory("development");
        SqlSession session = manager.getSessionFactory().openSession();
        /*Забираем маппер из сессии*/
        Mapper mapper = session.getMapper(Mapper.class);
        /*Получаем id поль-ля*/
        int userId = mapper.selectUserId("ponomarev");
        /*Получаем проекты и маршруты по каждому в соответствии с id поль-ля*/
        List<Map> routes = mapper.selectProjsAndRoutes(userId);
        Map<Integer, List<Integer>> newRoutes = mapFromListOfMap(routes);
        
        System.out.println(newRoutes.size());
    }
    
    public static Map<Integer,List<Integer>> mapFromListOfMap (List<Map> listOfMap ) 
    {
        Map<Integer,List<Integer>> map = new HashMap<Integer,List<Integer>>();
        for(int i = 0; i < listOfMap.size(); i++) {
            Integer key = (Integer)listOfMap.get(i).get("PROJ");
            Integer value = (Integer)listOfMap.get(i).get("ROUTE");
            if (map.containsKey(key)){
                List<Integer> listValues = map.get(key);
                listValues.add(value);
                map.put(key, listValues);
                
            }else{
                List<Integer> newList = new ArrayList<Integer>();
                newList.add(value);
                map.put(key, newList);
            }        
        }
        return map;
    }
}
