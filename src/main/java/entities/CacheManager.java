package entities;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Adamenko Artem <adamenko.artem@gmail.com>
 */
public class CacheManager {
    /*Кэш хранящий ключ,значение*/
    final private static Map<Double,String> cacheMap = new ConcurrentHashMap<Double,String>();
   
    /**
     * добавить элемент
     * @param double ключ
     * @param String значение
     */
    public static void add(Double key, String value){
        if (key != null && value != null){
            cacheMap.put(key, value);
        }    
    }
    
    /**
     * Получить значение из кэша
     * @param double ключ
     * @return String значение
     */
    public static String getValue(Double key){
        String value = "";
        if (checkElement(key)){
            value = cacheMap.get(key);
        }
        return value;
    }
    
    /**
     * Проверка на существование элементы в кэше
     * @param Double ключ
     * @return boolean 
     */
    public static boolean checkElement(Double key){
        if (key != null && cacheMap.containsKey(key))
            return true;
        else
            return false;
    }
    
    /**
     * Удаление элемента
     * @param Double ключ
     */
    public static void remove(Double key){
        if (checkElement(key))
            cacheMap.remove(key);
    }

   // @Override
    public static String toStr() {
        return "CacheManager{" + cacheMap.toString()+ '}';
    }
    
    
}
