package mapper;

import entities.BusObject;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
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
    
    /*Проверка пользователя*/
    @Select("SELECT ID_ FROM USERS WHERE NAME_ = #{userName} AND PASS_ = #{password}")
    public Integer checkUser(@Param("userName") String name, @Param("password") String pass);
    
    /*Запрос на получение данный для отчета*/
    @Select("SELECT "
            + "o.NAME_"
            + ", (select cb.CB_NAME_ from CAR_BRAND cb where cb.CB_ID_ = o.CAR_BRAND_) as cbname_"
            + ", (select r.name_ from routs r where r.id_=o.last_rout_)  as rname_"
            + ", o.LAST_TIME_"
            + ", o.LAST_STATION_TIME_"
            + ", (select bs.NAME_ from BUS_STATIONS bs where bs.ROUT_ = o.LAST_ROUT_ and bs.NUMBER_=o.LAST_STATION_) as bsname_"
            + ", (select pv.name_ from providers pv where pv.id_ = o.provider_) as pvname"
            + "FROM OBJECTS o"
            + "WHERE o.PROJ_ID_ in (select up.PROJ_ID_ from USERS_PROJS up left join USERS u on up.USER_=u.ID_ where upper(u.NAME_)=upper(current_user))"
            + "AND o.OBJ_OUTPUT_ = 0"
            + "ORDER BY o.proj_id_, o.last_rout_, o.last_station_time_ desc")
    public void getDataToReport();
}
