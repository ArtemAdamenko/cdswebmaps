package entities;

import java.util.Date;

/**
 *
 * @author Adamenko Artem <adamenko.artem@gmail.com>
 * Класс для работы с объектами для отчета
 */
public class OwnerReportObject {
    /*гос. номер*/
    public String NAME_;
    /*марка т.с.*/
    public String CBNAME_;
    /*маршрут*/
    public String RNAME_;
    /*Время последнего отклика*/
    public Date LAST_TIME_;
    /*Время прохождения последней остановки*/
    public Date LAST_STATION_TIME_;
    /*установщик*/
    public String PVNAME;
    /*номер SIM*/
    public String PHONE_;

    public void setLAST_STATION_TIME_(Date LAST_STATION_TIME_) {
        this.LAST_STATION_TIME_ = LAST_STATION_TIME_;
    }

    public void setPHONE_(String PHONE_) {
        this.PHONE_ = PHONE_;
    }

    public Date getLAST_STATION_TIME_() {
        return LAST_STATION_TIME_;
    }

    public String getPHONE_() {
        return PHONE_;
    }

    public String getNAME_() {
        return NAME_;
    }

    public String getCBNAME_() {
        return CBNAME_;
    }

    public String getRNAME_() {
        return RNAME_;
    }

    public Date getLAST_TIME_() {
        return LAST_TIME_;
    }

    public Date getLAST_STATION_TIME() {
        return LAST_STATION_TIME_;
    }

    public String getPVNAME() {
        return PVNAME;
    }

    public void setNAME_(String NAME_) {
        this.NAME_ = NAME_;
    }

    public void setCBNAME_(String CBNAME_) {
        this.CBNAME_ = CBNAME_;
    }

    public void setRNAME_(String RNAME_) {
        this.RNAME_ = RNAME_;
    }

    public void setLAST_TIME_(Date LAST_TIME_) {
        this.LAST_TIME_ = LAST_TIME_;
    }

    public void setLAST_STATION_TIME(Date LAST_STATION_TIME_) {
        this.LAST_STATION_TIME_ = LAST_STATION_TIME_;
    }

    public void setPVNAME(String PVNAME) {
        this.PVNAME = PVNAME;
    }

    @Override
    public String toString() {
        return "ReportObjectList{" + "NAME_=" + NAME_ + ", CBNAME_=" + CBNAME_ + ", RNAME_=" + RNAME_ + ", LAST_TIME_=" + LAST_TIME_ + ", LAST_STATION_TIME=" + LAST_STATION_TIME_ + ",PVNAME=" + PVNAME + '}';
    }
    
    
}
