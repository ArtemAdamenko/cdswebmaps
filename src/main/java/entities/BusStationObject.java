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
