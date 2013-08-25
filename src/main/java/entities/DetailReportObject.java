package entities;

/**
 *
 * @author Adamenko Artem <adamenko.artem@gmail.com>
 */
public class DetailReportObject {
    public String oname_;
    public String bsname_;
    public String bscontrol_;
    public String dt;

    @Override
    public String toString() {
        return "DetailReportObject{" + "oname_=" + oname_ + ", bsname_=" + bsname_ + ", bscontrol_=" + bscontrol_ + ", date=" + dt + '}';
    }

    public void setOname_(String oname_) {
        this.oname_ = oname_;
    }

    public void setBsname_(String bsname_) {
        this.bsname_ = bsname_;
    }

    public void setBscontrol_(String bscontrol_) {
        this.bscontrol_ = bscontrol_;
    }

    public void setDate(String date) {
        this.dt = date;
    }

    public String getOname_() {
        return oname_;
    }

    public String getBsname_() {
        return bsname_;
    }

    public String getBscontrol_() {
        return bscontrol_;
    }

    public String getDate() {
        return dt;
    }
}
