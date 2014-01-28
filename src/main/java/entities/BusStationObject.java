package entities;

/**
 *
 * @author Adamenko Artem <adamenko.artem@gmail.com>
 * Класс остановок
 */
public class BusStationObject {
    public Integer Number;
    public String Name;
    public double LON_;
    public double Lat;
    public Integer Route;
    public Integer Control;
    

    public void setNumber(Integer Number) {
        this.Number = Number;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    
    public void setLON_(double LON_) {
        this.LON_ = LON_;
    }

    public void setLat(double Lat) {
        this.Lat = Lat;
    }

    public void setRoute(Integer Route) {
        this.Route = Route;
    }

    public void setControl(Integer Control) {
        this.Control = Control;
    }

    public Integer getNumber() {
        return Number;
    }

    public String getName() {
        return Name;
    }

    public double getLON_() {
        return LON_;
    }

    public double getLat() {
        return Lat;
    }

    public Integer getRoute() {
        return Route;
    }

    public Integer getControl() {
        return Control;
    }
    
}
