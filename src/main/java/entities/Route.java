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
private double decimalDegrees(double coord){
        double long_kostil = 0;
        double lat_kostil = 0;
        
        double myCoord = coord/100;
        //градусы
        int degrees = (int)myCoord;
        
        //минуты
        double temp = (myCoord - degrees)*100;
        
        int min = (int)temp;
        if ((min < 60) && (degrees > 40))
            long_kostil = 0.006423;
        if ((min < 15) && (degrees < 40))
            lat_kostil = 0.007058;
        //секунды
        double temp1 = (temp - min)*100;
        Integer sec1 = (int)temp1;
        double temp2 = (temp1 - sec1)*100;
        Integer sec2 = (int)temp2;
        double sec = Double.valueOf(sec1.toString() + "." + sec2.toString());
        if (sec <=59.9 )
            sec = sec/10;
        
        double min1 = Double.valueOf(min)/60;
        double result = degrees + min1 + sec/3600;
        
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance(java.util.Locale.UK);
        nf.setMaximumFractionDigits(5);
        nf.setMinimumFractionDigits(4);
        
        result = Double.valueOf(nf.format(result));
        //Double result = coord/100;
        if (result > 40)
            result = result + long_kostil;//ебаный костыль для ебаных гранитов
        if (result < 40)
            result = result - lat_kostil;
        return result;
    }
    public double getLON_() {
        return LON_;
    }

    public double getLAT_() {
        return LAT_;
    }

    public void setLON_(double LON_) {
        this.LON_ = decimalDegrees(LON_);
    }

    public void setLAT_(double LAT_) {
        this.LAT_ = decimalDegrees(LAT_);
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
