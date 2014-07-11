package entities;

import java.util.Date;

/**
 *
 * @author Adamenko Artem <adamenko.artem@gmail.com>
 * Класс хранящий данные об автобусах
 */


public class BusObject {
    /*идентификатор объекта*/
    int obj_id_;
    /*идентификатор проекта*/
    int proj_id_;
    /*название перевозчика*/
    String projName;
    /*текущая скорость*/
    int last_speed_;
    /*долгота*/
    double last_lon_;
    /*широта*/
    double last_lat_;
    /*номер*/
    String name_;
    /*время последнего отклика*/
    Date last_time_;
    /*последняя остановка*/
    Date last_station_time_;
    /*название маршрута*/
    String route_name_;
    /*остановка*/
    String bus_station_;
    /*физический адрес местонахождения*/
    String address;
    /*Тип ТС(обычный, спецтехника)*/
    int type_proj;

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public void setType_proj(int type_proj) {
        this.type_proj = type_proj;
    }

    public String getProjName() {
        return projName;
    }

    public int getType_proj() {
        return type_proj;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getAddress() {
        return address;
    }

    public void setBus_station_(String bus_station_) {
        this.bus_station_ = bus_station_;
    }

    public String getBus_station_() {
        return bus_station_;
    }

    public void setRoute_name_(String route_name_) {
        this.route_name_ = route_name_;
    }

    public String getRoute_name_() {
        return route_name_;
    }

    public void setLast_station_time_(Date last_station_time_) {
        this.last_station_time_ = last_station_time_;
    }

    public Date getLast_station_time_() {
        return last_station_time_;
    }

    public void setLast_speed_(int last_speed_) {
        this.last_speed_ = last_speed_;
    }

    public int getLast_speed_() {
        return last_speed_;
    }

    public void setObj_id_(int obj_id_) {
        this.obj_id_ = obj_id_;
    }

    public void setProj_id_(int proj_id_) {
        this.proj_id_ = proj_id_;
    }

    public int getObj_id_() {
        return obj_id_;
    }

    public int getProj_id_() {
        return proj_id_;
    }

    public String getName_() {
        return name_;
    }

    public double getLast_lon_() {
        return last_lon_;
    }

    public double getLast_lat_() {
        return last_lat_;
    }

    public Date getLast_time_() {
        return last_time_;
    }

    public void setName_(String name_) {
        this.name_ = name_;
    }

    public void setLast_lon_(double last_lon_) {
        this.last_lon_ = convertCoord(last_lon_);
    }

    public void setLast_lat_(double last_lat_) {
        this.last_lat_ = convertCoord(last_lat_);
    }

    public void setLast_time_(Date last_time_) {
        this.last_time_ = last_time_;
    }
    
      /**перевод координат для яндекс карт
    * @param double coord
    * @return double convert coord
    */
    private static Double convertCoord(Double coord){    
        double x = coord;
        double y = x;
        y = (int)x/100;
        x=x-y*100;
        double x1=(int)x;
        y=y+x1/60+(x-x1)/60;
        return y;
    }
    
    @Override
    public String toString() {
        return "BusObject{" + "obj_id_=" + obj_id_ + ", proj_id_=" + proj_id_ + ", last_speed_=" + last_speed_ + ", last_lon_=" + last_lon_ + ", last_lat_=" + last_lat_ + ", name_=" + name_ + ", last_time_=" + last_time_ + ", last_station_time_=" + last_station_time_ + ", route_name_=" + route_name_ + ", bus_station_=" + bus_station_ + '}';
    }
}
