package mapper;

import entities.BusObject;
import entities.ReportObject;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author Artem Adamenko <adamenko.artem@gmail.com>
 * Интерфейс запросов к базе Project
 */
public interface ProjectsMapper {

    @Select("SELECT ID_ FROM USERS WHERE NAME_ = #{name}")
    int selectUserId(String name);
    
    /*Запрос на проекты и маршруты к каждому из них*/
    @Select("select distinct up.proj_id_ as proj, pr.rout_id_ as route\n" 
            + "from users_projs up\n" 
            + "right join proj_routs pr on pr.proj_id_= up.proj_id_\n" 
            + "where up.user_ = #{userId}")
    @MapKey("proj")
    public List<Map> selectProjsAndRoutes(int userId);
    
    /*Запрос на объекты по маршруту*/
    @Select("SELECT o.name_, o.last_lon_, o.last_lat_, o.last_time_, o.obj_id_, o.proj_id_, o.last_speed_, o.last_station_time_, o.last_rout_,o.last_station_,\n" 
            + "(SELECT rt.name_ as route_name_ FROM routs rt where rt.id_ = o.last_rout_),"
            + "(SELECT bs.name_ as bus_station_ FROM bus_stations bs where bs.number_ = o.last_station_ AND bs.rout_ = o.last_rout_)"
            + " FROM objects o WHERE o.last_rout_ = #{route} and proj_id_ = #{proj_id_} ORDER BY o.last_time_ ASC")
    public List<BusObject> selectObjects(@Param("route")int route, @Param("proj_id_")int proj_id_);
    
    /*Проверка пользователя*/
    @Select("SELECT ID_ FROM USERS WHERE NAME_ = #{userName} AND PASS_ = #{password}")
    public Integer checkUser(@Param("userName") String name, @Param("password") String pass);
    
    /*Запрос на получение данный для отчета*/
    @Select("SELECT \n"
            + "o.NAME_\n"
            + ", (select cb.CB_NAME_ from CAR_BRAND cb where cb.CB_ID_ = o.CAR_BRAND_) as cbname_\n"
            + ", (select r.name_ from routs r where r.id_=o.last_rout_)  as rname_\n"
            + ", o.LAST_TIME_\n"
            + ", o.LAST_STATION_TIME_\n"
            + ", (select pv.name_ from providers pv where pv.id_ = o.provider_) as pvname"
            + " FROM OBJECTS o\n"
            + " WHERE o.PROJ_ID_ in (select up.PROJ_ID_ from USERS_PROJS up left join USERS u on up.USER_=u.ID_ where u.NAME_= #{name})\n"
            + " AND o.OBJ_OUTPUT_ = 0\n"
            + " ORDER BY o.proj_id_, o.last_rout_, o.last_station_time_ desc")
    public List<ReportObject> getDataToReport(String name);
    
    @Select("SELECT FIRST 1 SKIP 0 a.NAME_\n"
                    + "FROM PROJECTS a\n"
                    + "WHERE a.ID_ in (select up.PROJ_ID_ from USERS_PROJS up left join USERS u on up.USER_=u.ID_ where u.NAME_=#{name})")
    public Map<Integer,String> getUserProject(String user);
    
    @Select("SELECT LAST_SPEED_ FROM OBJECTS WHERE OBJ_ID_ = #{objId} AND PROJ_ID_ = #{projId} AND LAST_TIME_ BETWEEN #{fromTime} AND #{toTime}")
    public List<Integer> getSpeedBus(@Param("objId")Integer objId, @Param("projId")Integer projId, @Param("fromTime")String fromTime, @Param("toTime")String toTime);
}
