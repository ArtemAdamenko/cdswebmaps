package entities;

/**
 *
 * @author Adamenko Artem <adamenko.artem@gmail.com>
 * Класс хранящий данные о маршруте автобуса
 */
public class Route {
    public double LON_;
    public double LAT_;
    public String TIME_;

    public double getLON_() {
        return LON_;
    }

    public double getLAT_() {
        return LAT_;
    }

    public void setLON_(double LON_) {
        this.LON_ = LON_;
    }

    public void setLAT_(double LAT_) {
        this.LAT_ = LAT_;
    }

    public String getTIME_() {
        return TIME_;
    }

    public void setTIME_(String TIME_) {
        this.TIME_ = TIME_;
    }

    @Override
    public String toString() {
        return "Route{" + "LON_=" + LON_ + ", LAT_=" + LAT_ + ", TIME_=" + TIME_ + '}';
    }
    
    
    
    
}
