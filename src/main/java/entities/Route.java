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
        
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance(java.util.Locale.UK);
        nf.setMaximumFractionDigits(6);
        nf.setMinimumFractionDigits(4);
        
        String aastring = nf.format(result);
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
