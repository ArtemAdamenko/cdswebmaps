package mapper;

import entities.Route;
import entities.RouteReportObject;
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
}
