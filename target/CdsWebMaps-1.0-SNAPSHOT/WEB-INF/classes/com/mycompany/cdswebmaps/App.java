package com.mycompany.cdswebmaps;

import com.google.gson.Gson;
import com.mycompany.cdswebmaps.JsonReader;
import entities.BusObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mapper.ProjectsMapper;
import mybatis.MyBatisManager;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONException;

/**
 * Hello world!
 *
 */
public class App 
{
    
    private static MyBatisManager manager = new MyBatisManager();
    public static void main(String[] args) throws IOException, JSONException, Exception
    {
        JsonReader.main(args);
        //MyBatisManager manager = new MyBatisManager();
        /*Выбираем среду параметров БД*/
        //manager.initFactory("development");
        //SqlSession session = manager.getSessionFactory().openSession();
        /*Забираем маппер из сессии*/
        //Mapper mapper = session.getMapper(Mapper.class);
        /*Получаем id поль-ля*/
        //int userId = mapper.selectUserId("ponomarev");
        /*Получаем проекты и маршруты по каждому в соответствии с id поль-ля*/
       // List<Map> routes = mapper.selectProjsAndRoutes(userId);
        //session.close();
        
       // Map<Integer, List<Integer>> newRoutes = mapFromListOfMap(routes);
        //getObjects(newRoutes);
        //System.out.println(newRoutes.toString());
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
    
    public static void getObjects(Map<Integer, List<Integer>> projects) throws Exception
    {
        /*Открываем сессию для запросов*/
        SqlSession session = manager.getSessionFactory().openSession();
        ProjectsMapper mapper = session.getMapper(ProjectsMapper.class);
        /*результирующий список объектов по маршруту*/
        List<BusObject> buses = new ArrayList<BusObject>();
        
        try{
            /*Проход по всем проектам*/
            for (List<Integer> routes : projects.values()){
                /*Проход по всем маршрутам*/
                for (int i = 0; i <= routes.size()-1; i++){
                    if (routes.get(i) != 0){
                        /*Объекты-автобусы по каждому маршруту*/
                        buses = mapper.selectObjects(routes.get(i));
                    }
                }
                /*Преобразование в json автобусов по маршруту*/
                Gson gson = new Gson();
                String jsonBuses = gson.toJson(buses);
                System.out.println(jsonBuses.toString());
            }
        }catch(Exception e){
            throw new Exception(e);
        }
        finally{
            session.commit();
            session.close();
        }
    }
}
