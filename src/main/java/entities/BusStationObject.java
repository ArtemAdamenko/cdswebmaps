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
    
    /*@Override
    public String toString() {
        return "BusStationObject{" + "Number=" + Number + ", Name=" + Name + ", Lon=" + Lon + ", Lat=" + Lat + ", Route=" + Route + ", Control=" + Control + '}';
    }*/

    public void setNumber(Integer Number) {
        this.Number = Number;
    }

    public void setName(String Name) {
        this.Name = Name;
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
    public void setLON_(double LON_) {
        this.LON_ = decimalDegrees(LON_);
    }

    public void setLat(double Lat) {
        this.Lat = decimalDegrees(Lat);
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
