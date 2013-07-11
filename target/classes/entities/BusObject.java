package entities;

import java.util.Date;

/**
 *
 * @author Adamenko Artem <adamenko.artem@gmail.com>
 * Класс хранящий данные об автобусах
 */
public class BusObject {
    String name_;
    double last_lon_;
    double last_lat_;
    Date last_time_;
    int obj_id_;
    int proj_id_;

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
        this.last_lon_ = last_lon_;
    }

    public void setLast_lat_(double last_lat_) {
        this.last_lat_ = last_lat_;
    }

    public void setLast_time_(Date last_time_) {
        this.last_time_ = last_time_;
    }

    @Override
    public String toString() {
        return "Object{" + "name_=" + name_ + ", last_lon_=" + last_lon_ + ", last_lat_=" + last_lat_ + ", last_time_=" + last_time_ + '}';
    }
    
    
}
