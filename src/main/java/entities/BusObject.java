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
        this.last_lon_ = decimalDegrees(last_lon_);
    }

    public void setLast_lat_(double last_lat_) {
        this.last_lat_ = decimalDegrees(last_lat_);
    }

    public void setLast_time_(Date last_time_) {
        this.last_time_ = last_time_;
    }
    
    private double decimalDegrees(double coord){
        double myCoord = coord/100;
        //градусы
        int degrees = (int)myCoord;
        //минуты
        double temp = (myCoord - degrees)*100;
        int min = (int)temp;
        //секунды
        double temp1 = (temp - min)*100;
        Integer sec1 = (int)temp1;
        double temp2 = (temp1 - sec1)*100;
        Integer sec2 = (int)temp2;
        Double sec = Double.valueOf(sec1.toString() + "." + sec2.toString());
        
        
        /*String temp = String.valueOf(coord/100);
        String temp2 = temp.replace(".", " ");
        String[] coords = temp2.split(" ");
        String temp3 = coords[1];
        String min = temp3.substring(0, 2);
        String sec1 = temp3.substring(2, 2);
        String sec2 = temp3.substring(4, 2);
        String sec = sec1 + "." + sec2;*/
        Double min1 = Double.valueOf(min)/60;
        Double result = degrees + min1 + sec/3600;
        
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance(java.util.Locale.UK);
        nf.setMaximumFractionDigits(7);
        nf.setMinimumFractionDigits(4);
        
        result = Double.valueOf(nf.format(result));
        return result;
    }

    @Override
    public String toString() {
        return "BusObject{" + "obj_id_=" + obj_id_ + ", proj_id_=" + proj_id_ + ", last_speed_=" + last_speed_ + ", last_lon_=" + last_lon_ + ", last_lat_=" + last_lat_ + ", name_=" + name_ + ", last_time_=" + last_time_ + ", last_station_time_=" + last_station_time_ + ", route_name_=" + route_name_ + ", bus_station_=" + bus_station_ + '}';
    }
}
