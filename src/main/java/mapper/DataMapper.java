package mapper;

import entities.BusObject;
import entities.DetailReportObject;
import entities.MoveBusStationDataObject;
import entities.Route;
import entities.RouteReportObject;
import entities.SpeedBus;
import java.util.List;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.StatementType;
import servlets.DetailReport;

/**
 *
 * @author Artem Adamenko <adamenko.artem@gmail.com>
 * Интерфейс запросов к базе Data
 */
public interface DataMapper {
    
    /**
     * запрос на траекторию пути по заданному времени
     * @param objId 
     * @param projId
     * @param fromTime
     * @param toTime
     * @return List<Route>
     */
    @Select("SELECT LON_, LAT_, TIME_ FROM BASEDATA WHERE OBJ_ID_ = #{objId} AND PROJ_ID_ = #{projId} AND TIME_ BETWEEN #{fromTime} AND #{toTime} ORDER BY TIME_ ASC")
    public List<Route> getRoute(@Param("objId")Integer objId, @Param("projId")Integer projId, @Param("fromTime")String fromTime, @Param("toTime")String toTime);
    
    /**
     * Вызов процедуры для отчета по выходу ТС на маршрут
     * @param startDate
     * @param endDate
     * @param route
     * @param sid 
     */
    @Update("EXECUTE PROCEDURE GET_REPORT_4(#{startDate}, #{endDate}, #{route}, #{sid})")
    @Options(statementType = StatementType.CALLABLE)
    public void getReport4(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("route")int route, @Param("sid")String sid);
    
    /**
     * данные для отчета по рейсам
     * @param uuid
     * @param routeId
     * @return List<RouteReportObject>
     */
    @Select("SELECT OBJ_NAME_, START_, END_, RCOUNT_, PROJ_NAME_ FROM REPORT2PRINT_4 WHERE UUID_ = #{uuid} AND ROUT_ID_ = #{routeId}")
    public List<RouteReportObject> getDataToRouteReport(@Param("uuid")String uuid, @Param("routeId")int routeId);
    
    
    /**
     * route id
     * @param name
     * @return route id
     */
    @Select("SELECT ID_ FROM ROUTS WHERE NAME_ = #{name}")
    public int getRouteId(String name);
    
    /**
     * запрос на скорость по времени
     * @param objId
     * @param projId
     * @param fromTime
     * @param toTime
     * @return List<Speed>
     */
    @Select("SELECT SPEED_, TIME_ FROM BASEDATA WHERE OBJ_ID_ = #{objId} AND PROJ_ID_ = #{projId} AND TIME_ BETWEEN #{fromTime} AND #{toTime} ORDER BY TIME_")
    public List<SpeedBus> getSpeedBus(@Param("objId")Integer objId, @Param("projId")Integer projId, @Param("fromTime")String fromTime, @Param("toTime")String toTime);
    
    /**
     * Вызов процедуры для подробного отчета по ТС
     * @param startDate
     * @param endDate
     * @param route
     * @param sid 
     */
    @Update("EXECUTE PROCEDURE GET_REP_DETAIL_MOV_OBJECTS(#{startDate}, #{endDate}, #{route}, #{sid})")
    @Options(statementType = StatementType.CALLABLE)
    public void getRepDetailMovObjects(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("route")int route, @Param("sid")String sid);
    
    /**
     * данные для подробного отчета*
     * @param sid
     * @param wsql
     * @return List<DetailReportObject>
     */
    @Select("SELECT a.PID, a.OID\n"
            + ", (SELECT o.name_ FROM OBJECTS o WHERE o.OBJ_ID_ = a.OID and o.PROJ_ID_ = a.PID) as oname_\n"
            + ", (SELECT bs.NAME_ FROM BUS_STATIONS bs WHERE bs.NUMBER_ = a.BSNUM and bs.ROUT_ = a.RID) as bsname_\n"
            + ", (SELECT bs.CONTROL_ from BUS_STATIONS bs WHERE bs.NUMBER_ = a.BSNUM and bs.ROUT_ = a.RID) as bscontrol_\n"
            + ", a.DT as dt FROM REP_FULLMOVE_OBJECTS_OF_ROUTE a\n"
            + " WHERE a.UUID_ = #{sid} and #{wsql}\n"
            + " ORDER BY a.PID ASC, a.OID ASC, a.DT ASC")
    public List<DetailReportObject> getDetailReport(@Param("sid")String sid, @Param("wsql")String wsql);
    
    /**
     * данные по маршруту
     * @param route
     * @param from
     * @param to
     * @return List<BusObject>
     */
    @Select("SELECT distinct PROJ_ID_, OBJ_ID_ FROM BUSDATA WHERE ROUT_ = #{route} AND TIME_ BETWEEN #{from} AND #{to}")
    public List<BusObject> getBuses(@Param("route")int route, @Param("from")String from, @Param("to")String to);
    
    /**
     * имя автобуса
     * @param projID
     * @param objID
     * @return String
     */
    @Select("SELECT NAME_ FROM OBJECTS WHERE PROJ_ID_ = #{projID} AND OBJ_ID_ = #{objID}")
    public String getNameofBus(@Param("projID")int projID, @Param("objID")int objID);
    
    /**
     * Тип ТС
     * @param projID
     * @param objID
     * @return int
     */
    @Select("SELECT TYPE_PROJ_ FROM OBJECTS WHERE PROJ_ID_ = #{projID} AND OBJ_ID_ = #{objID}")
    public Integer getTypeofBus(@Param("projID")int projID, @Param("objID")int objID);
    
    /**
     * выполнение пользовательского динамически составного запроса из класса DetailReport
     * @param sid
     * @param wsql
     * @return List<DetailReportObject>
     */
    @SelectProvider(type = DetailReport.class, method = "selectPersonSql")
    public List<DetailReportObject> selectPersonSql(@Param("sid")String sid,@Param("wsql")String wsql);
    
    /**
     * Получение данных для отчета по контролю движения транспорта
     * @param from
     * @param to
     * @param routeID
     * @param stationID
     * @return List<MoveBusStationDataObject>
     */
    @Select("SELECT b.rout_ as Route"
            + ",(SELECT r.name_ FROM routs r WHERE r.id_ = b.rout_) as routeName"
            + ",(SELECT s.name_ FROM bus_stations s WHERE s.number_ = b.station_ AND s.rout_ = b.rout_) as currStationName"
            + ",(SELECT o.name_ FROM objects o WHERE o.obj_id_ = b.obj_id_ AND o.proj_id_ = b.proj_id_) as objectName"
            + ",(SELECT FIRST 1 s.name_ FROM bus_stations s WHERE s.number_ > b.station_ AND s.rout_ = b.rout_ AND s.control_ <> 0 ORDER BY s.number_ ASC) as trendName"
            + ",b.time_ FROM busdata as b WHERE (b.time_ >= #{from} AND b.time_ <= #{to}) AND (b.rout_ = #{routeID} AND b.station_ = #{stationID})")
    public List<MoveBusStationDataObject> getMoveBusControlReportData(@Param("from")String from, @Param("to")String to, @Param("routeID")int routeID, @Param("stationID")int stationID); 
            
}
