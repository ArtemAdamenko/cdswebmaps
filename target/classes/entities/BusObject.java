/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.util.Date;

/**
 *
 * @author Администратор
 */
public class BusObject {
    String name_;
    double last_lon_;
    double last_lat_;
    Date last_time_;

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
