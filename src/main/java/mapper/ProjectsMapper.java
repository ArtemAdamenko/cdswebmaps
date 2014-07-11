package mapper;

import entities.BusObject;
import entities.BusStationObject;
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

    /** ID Route
     * @param  name String имя пользователя
     * @return ID_ int route
    */
    @Select("SELECT ID_ FROM ROUTS WHERE NAME_ = #{name}")
    public int getRouteId(String name);
    
    /**
     * Id user
     * @param name String имя
     * @return  ID_ int user
     */
    @Select("SELECT ID_ FROM USERS WHERE NAME_ = #{name}")
    public int selectUserId(String name);
    
    /**
     * Id Object
     * @param objID
     * @param projID
     * @return IDS_ int
     */
    @Select("SELECT IDS_ FROM OBJECTS WHERE OBJ_ID_ = #{objID} AND PROJ_ID_ = #{projID}")
    public int getIDS(@Param("objID")int objID, @Param("projID") int projID); 
    
    /**
     * Получение только всех маршрутов текущего пользователя
     * @param userId int
     * @return List<Map> current routes
     */
    @Select("select pr.rout_id_ as route,(select NAME_ FROM ROUTS WHERE ID_=pr.rout_id_)\n" +
            "from users_projs up\n" +
            "right join proj_routs pr on pr.proj_id_= up.proj_id_\n" +
            "where up.user_ = #{user}")
    @MapKey("ROUTE")
    public List<Map> getCurrentRoutes(int userId);
    
    /**
     * Автобусы заданного маршрута
     * @param user int
     * @param route int
     * @return List<BusObject> current route buses
     */
    @Select("select * from objects o\n" +
            "inner join projects p on p.id_=o.proj_id_\n" +
            "inner join users_projs up on up.proj_id_=o.proj_id_\n" +
            "inner join routs r on r.id_=o.last_rout_\n" +
            "where up.user_=#{user} and r.id_=#{route}")
    public List<BusObject> getCurrentRouteBuses(@Param("user")int user, @Param("route")int route);
    
    /**
     * Запрос на проекты и маршруты к каждому из них
     * @param userId int
     * @return  List<Map> Projects with routes
     */
    @Select("select distinct up.proj_id_ as proj, pr.rout_id_ as route\n" 
            + "from users_projs up\n" 
            + "right join proj_routs pr on pr.proj_id_= up.proj_id_\n" 
            + "where up.user_ = #{userId}")
    @MapKey("proj")
    public List<Map> selectProjsAndRoutes(int userId);
    
    /**
     * Проверка на принадлежность навигатора к гранитам
     * @param IDS int
     * @return ID_ int
     */
    @Select("SELECT ID_ FROM GRANITS WHERE OIDS_ = #{IDS}")
    public Integer checkGranit(@Param("IDS")int IDS);
    
    /**
     * Запрос на объекты по маршруту
     * @param route int 
     * @param proj_id_ int
     * @return List<BusObject> Objects
     */
    @Select("SELECT o.name_,o.last_time_, o.obj_id_, o.proj_id_, o.last_rout_,o.last_station_,\n" 
            + "(SELECT rt.name_ as route_name_ FROM routs rt where rt.id_ = o.last_rout_)"
            + " FROM objects o WHERE o.last_rout_ = #{route} and proj_id_ = #{proj_id_} and obj_output_ = 0 ORDER BY o.last_time_ DESC")
    public List<BusObject> selectListObjects(@Param("route")int route, @Param("proj_id_")int proj_id_);
    
    /**
     * Запрос на объекты по маршруту
     * @param route int 
     * @param proj_id_ int 
     * @return List<BusObject> 
     */
    @Select("SELECT o.name_, o.last_lon_, o.last_lat_, o.last_time_, o.obj_id_, o.proj_id_, o.last_speed_, o.last_station_time_, o.last_rout_,o.last_station_, ids_,\n" 
            + "(SELECT rt.name_ as route_name_ FROM routs rt where rt.id_ = o.last_rout_),"
            + "(SELECT bs.name_ as bus_station_ FROM bus_stations bs where bs.number_ = o.last_station_ AND bs.rout_ = o.last_rout_)"
            + " FROM objects o WHERE o.last_rout_ = #{route} and proj_id_ = #{proj_id_} and obj_output_ = 0 ORDER BY o.last_time_ DESC")
    public List<BusObject> selectListObjectsWithoutStations(@Param("route")int route, @Param("proj_id_")int proj_id_);
    
    /**
     * Проверка пользователя
     * @param name String
     * @param pass String
     * @return ID_ int
     */
    @Select("SELECT ID_ FROM USERS WHERE NAME_ = #{userName} AND PASS_ = #{password}")
    public Integer checkUser(@Param("userName") String name, @Param("password") String pass);
    
    /**
     * Запрос на получение данный для отчета
     * @param name String
     * @return List<ReportObject>
     */
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
            + " ORDER BY o.last_station_time_ desc")
    public List<ReportObject> getDataToReport(String name);
    
    
    @Select("SELECT FIRST 1 SKIP 0 a.NAME_\n"
                    + "FROM PROJECTS a\n"
                    + "WHERE a.ID_ in (select up.PROJ_ID_ from USERS_PROJS up left join USERS u on up.USER_=u.ID_ where u.NAME_=#{name})")
    public Map<Integer,String> getUserProject(String user);  
    
    /**
     * Достаем время последнего отклика, маршрут и название проекта по названию маршрута
     * @param busName String 
     * @return List<BusObject>
     */
    @Select("SELECT last_time_, name_,"
            + "(SELECT name_ FROM projects pr WHERE pr.id_ = o.proj_id_) as projName, "
            + "(SELECT name_ FROM routs rt WHERE rt.id_ = o.last_rout_) as route_name_ "
            + "FROM objects o WHERE (o.name_ CONTAINING #{busName})")
    public List<BusObject> getBusInfo(String busName);
    
    /**
     * Достаем остановки по маршруту
     * @param routeID int
     * @return List<BusStationObject>
     */
    @Select("SELECT NUMBER_ as Number, NAME_ as name, CONTROL_ as Control FROM BUS_STATIONS WHERE ROUT_ = #{routeID}")
    public List<BusStationObject> getBusStations(int routeID); 
    
    /**
     * Остановка с координатами
     * @param number int
     * @param route int
     * @return BusStationObject Coords of station
     */
    @Select("SELECT LON_, LAT_ FROM BUS_STATIONS WHERE NUMBER_ = #{number} AND ROUT_ = #{route}")
    public BusStationObject getCoordsOfStation(@Param("number")int number, @Param("route")int route);
    
    
    @Select("Select NUMBER_ AS Number, LON_, LAT_ AS Lat FROM BUS_STATIONS WHERE ROUT_ = 1 ORDER BY NUMBER_ ASC")
    public List<BusStationObject> getstations();
}
