package entities;

/**
 *
 * @author Adamenko Artem <adamenko.artem@gmail.com>
 */
public class BusStationObject {
    public Integer Number;
    public String Name;
    public Long Lon;
    public Long Lat;

    @Override
    public String toString() {
        return "BusStationObject{" + "Number=" + Number + ", Name=" + Name + ", Lon=" + Lon + ", Lat=" + Lat + ", Route=" + Route + ", Control=" + Control + '}';
    }

    public void setNumber(Integer Number) {
        this.Number = Number;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setLon(Long Lon) {
        this.Lon = Lon;
    }

    public void setLat(Long Lat) {
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

    public Long getLon() {
        return Lon;
    }

    public Long getLat() {
        return Lat;
    }

    public Integer getRoute() {
        return Route;
    }

    public Integer getControl() {
        return Control;
    }
    public Integer Route;
    public Integer Control;
}
