package entities;

/**
 *
 * @author Adamenko Artem <adamenko.artem@gmail.com>
 */
public class MoveBusStationDataObject {
    Integer Route;
    String routeName;
    String CurrStationName;
    String ObjectName;
    String trendName;
    String Time_;

    @Override
    public String toString() {
        return "MoveBusStationDataObject{" + "Route=" + Route + ", routeName=" + routeName + ", CurrStationName=" + CurrStationName + ", ObjectName=" + ObjectName + ", trendName=" + trendName + ", Time_=" + Time_ + '}';
    }

    public void setRoute(Integer Route) {
        this.Route = Route;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public void setCurrStationName(String CurrStationName) {
        this.CurrStationName = CurrStationName;
    }

    public void setObjectName(String ObjectName) {
        this.ObjectName = ObjectName;
    }

    public void setTrendName(String trendName) {
        this.trendName = trendName;
    }

    public void setTime_(String Time_) {
        this.Time_ = Time_;
    }

    public Integer getRoute() {
        return Route;
    }

    public String getRouteName() {
        return routeName;
    }

    public String getCurrStationName() {
        return CurrStationName;
    }

    public String getObjectName() {
        return ObjectName;
    }

    public String getTrendName() {
        return trendName;
    }

    public String getTime_() {
        return Time_;
    }
}
