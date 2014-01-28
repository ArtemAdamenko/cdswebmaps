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
    public int Station;

    public void setStation(int Station) {
        this.Station = Station;
    }

    public int getStation() {
        return Station;
    }

    public double getLON_() {
        return LON_;
    }

    public double getLAT_() {
        return LAT_;
    }

    public void setLON_(double LON_) {
        this.LON_ = convertCoord(LON_);
    }

    public void setLAT_(double LAT_) {
        this.LAT_ = convertCoord(LAT_);
    }

    public String getTIME_() {
        return TIME_;
    }

    public void setTIME_(String TIME_) {
        this.TIME_ = TIME_;
    }

    /*перевод координат для яндекс карт
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
        return "Route{" + "LON_=" + LON_ + ", LAT_=" + LAT_ + ", TIME_=" + TIME_ + '}';
    }
    
    
    
    
}
