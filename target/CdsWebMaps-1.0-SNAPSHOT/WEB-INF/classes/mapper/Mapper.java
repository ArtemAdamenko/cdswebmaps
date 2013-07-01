package mapper;

import entities.BusObject;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author Artem Adamenko <adamenko.artem@gmail.com>
 */
public interface Mapper {
   
    @Select("SELECT ID_ FROM USERS WHERE NAME_ = #{name}")
    int selectUserId(String name);
    
    /*Запрос на проекты и маршруты к каждому из них*/
    @Select("select up.proj_id_ as proj, pr.rout_id_ as route\n" +
            "from users_projs up\n" +
            "right join proj_routs pr on pr.proj_id_= up.proj_id_\n" +
            "where up.user_ = #{userId}")
    @MapKey("proj")
    public List<Map> selectProjsAndRoutes(int userId);
    
    /*Запрос на объекты по маршруту*/
    @Select("SELECT name_, last_lon_, last_lat_, last_time_ FROM objects WHERE disp_route_ = #{route} ORDER BY last_time_ ASC")
    public List<BusObject> selectObjects(int route);
}
