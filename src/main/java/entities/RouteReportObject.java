package entities;

/**
 *
 * @author Adamenko Artem <adamenko.artem@gmail.com>
 * Класс хранящий данные о рейсах тс
 */
public class RouteReportObject {
    public String obj_name_;
    public String start_;
    public String end_;
    public int rcount_;
    public String proj_name_;

    public String getObj_name_() {
        return obj_name_;
    }

    public String getStart_() {
        return start_;
    }

    public String getEnd_() {
        return end_;
    }

    public int getRcount_() {
        return rcount_;
    }

    public String getProj_name_() {
        return proj_name_;
    }

    public void setObj_name_(String obj_name_) {
        this.obj_name_ = obj_name_;
    }

    public void setStart_(String start_) {
        this.start_ = start_;
    }

    public void setEnd_(String end_) {
        this.end_ = end_;
    }

    public void setRcount_(int rcount_) {
        this.rcount_ = rcount_;
    }

    public void setProj_name_(String proj_name_) {
        this.proj_name_ = proj_name_;
    }

    @Override
    public String toString() {
        return "RouteReportObject{" + "obj_name_=" + obj_name_ + ", start_=" + start_ + ", end_=" + end_ + ", rcount_=" + rcount_ + ", proj_name_=" + proj_name_ + '}';
    }
    
    
    
}
