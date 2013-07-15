package mapper;

import entities.Route;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author Artem Adamenko <adamenko.artem@gmail.com>
 * Интерфейс запросов к базе Data
 */
public interface DataMapper {
    /*запрос на траекторию пути*/
    @Select("SELECT FIRST 190 LON_, LAT_, TIME_ FROM BASEDATA WHERE OBJ_ID_ = #{objId} AND PROJ_ID_ = #{projId} AND TIME_ BETWEEN #{fromTime} AND #{toTime}")
    public List<Route> getRoute(@Param("objId")Integer objId, @Param("projId")Integer projId, @Param("fromTime")String fromTime, @Param("toTime")String toTime);
}
