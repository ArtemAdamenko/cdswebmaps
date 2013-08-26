package mapper;

import entities.BusObject;
import entities.DetailReportObject;
import entities.Route;
import entities.RouteReportObject;
import entities.SpeedBus;
import java.util.List;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.StatementType;

/**
 *
 * @author Artem Adamenko <adamenko.artem@gmail.com>
 * Интерфейс запросов к базе Data
 */
public interface DataMapper {
    /*запрос на траекторию пути по заданному времени*/
    @Select("SELECT FIRST 190 LON_, LAT_, TIME_ FROM BASEDATA WHERE OBJ_ID_ = #{objId} AND PROJ_ID_ = #{projId} AND TIME_ BETWEEN #{fromTime} AND #{toTime}")
    public List<Route> getRoute(@Param("objId")Integer objId, @Param("projId")Integer projId, @Param("fromTime")String fromTime, @Param("toTime")String toTime);
    
    /*Вызов процедуры для отчета по выходу ТС на маршрут*/
    @Update("EXECUTE PROCEDURE GET_REPORT_4(#{startDate}, #{endDate}, #{route}, #{sid})")
    @Options(statementType = StatementType.CALLABLE)
    public void getReport4(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("route")int route, @Param("sid")String sid);
    
    @Select("SELECT OBJ_NAME_, START_, END_, RCOUNT_, PROJ_NAME_ FROM REPORT2PRINT_4 WHERE UUID_ = #{uuid} AND ROUT_ID_ = #{routeId}")
    public List<RouteReportObject> getDataToRouteReport(@Param("uuid")String uuid, @Param("routeId")int routeId);
    
    
    /*route id*/
    @Select("SELECT ID_ FROM ROUTS WHERE NAME_ = #{name}")
    public int getRouteId(String name);
    
    /*запрос на скорость по времени*/
    @Select("SELECT SPEED_, TIME_ FROM BASEDATA WHERE OBJ_ID_ = #{objId} AND PROJ_ID_ = #{projId} AND TIME_ BETWEEN #{fromTime} AND #{toTime}")
    public List<SpeedBus> getSpeedBus(@Param("objId")Integer objId, @Param("projId")Integer projId, @Param("fromTime")String fromTime, @Param("toTime")String toTime);
    
    /*Вызов процедуры для подробного отчета по ТС*/
    @Update("EXECUTE PROCEDURE GET_REP_DETAIL_MOV_OBJECTS(#{startDate}, #{endDate}, #{route}, #{sid})")
    @Options(statementType = StatementType.CALLABLE)
    public void getRepDetailMovObjects(@Param("startDate")String startDate, @Param("endDate")String endDate, @Param("route")int route, @Param("sid")String sid);
    
    /*данные для подробного отчета*/
    @Select("SELECT a.PID, a.OID\n"
            + ", (SELECT o.name_ FROM OBJECTS o WHERE o.OBJ_ID_ = a.OID and o.PROJ_ID_ = a.PID) as oname_\n"
            + ", (SELECT bs.NAME_ FROM BUS_STATIONS bs WHERE bs.NUMBER_ = a.BSNUM and bs.ROUT_ = a.RID) as bsname_\n"
            + ", (SELECT bs.CONTROL_ from BUS_STATIONS bs WHERE bs.NUMBER_ = a.BSNUM and bs.ROUT_ = a.RID) as bscontrol_\n"
            + ", a.DT as dt FROM REP_FULLMOVE_OBJECTS_OF_ROUTE a\n"
            + " WHERE a.UUID_ = #{sid} and #{wsql}\n"
            + " ORDER BY a.PID ASC, a.OID ASC, a.DT ASC")
    public List<DetailReportObject> getDetailReport(@Param("sid")String sid, @Param("wsql")String wsql);
    
    /*данные по маршруту*/
    @Select("SELECT distinct PROJ_ID_, OBJ_ID_ FROM BUSDATA WHERE ROUT_ = #{route} AND TIME_ BETWEEN #{from} AND #{to}")
    public List<BusObject> getBuses(@Param("route")int route, @Param("from")String from, @Param("to")String to);
    /*имя автобуса*/
    @Select("SELECT NAME_ FROM OBJECTS WHERE PROJ_ID_ = #{projID} AND OBJ_ID_ = #{objID}")
    public String getNameofBus(@Param("projID")int projID, @Param("objID")int objID);
}
