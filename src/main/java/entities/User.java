package Entities;

/**
 *
 * @author Artem Adamenko <adamenko.artem@gmail.com>
 * 
 * Класс информации о пользователе
 */
public class User {
    int ID_;
    String NAME_;
    String PASS_;
    int DISTR_TYPE_;
    String DISTR_PATH_;

    public int getID_() {
        return ID_;
    }

    public String getNAME_() {
        return NAME_;
    }

    public String getPASS_() {
        return PASS_;
    }

    public int getDISTR_TYPE_() {
        return DISTR_TYPE_;
    }

    public String getDISTR_PATH_() {
        return DISTR_PATH_;
    }

    public void setID_(int ID_) {
        this.ID_ = ID_;
    }

    public void setNAME_(String NAME_) {
        this.NAME_ = NAME_;
    }

    public void setPASS_(String PASS_) {
        this.PASS_ = PASS_;
    }

    public void setDISTR_TYPE_(int DISTR_TYPE_) {
        this.DISTR_TYPE_ = DISTR_TYPE_;
    }

    public void setDISTR_PATH_(String DISTR_PATH_) {
        this.DISTR_PATH_ = DISTR_PATH_;
    }

    @Override
    public String toString() {
        return "User{" + "ID_=" + ID_ + ", NAME_=" + NAME_ + ", PASS_=" + PASS_ + ", DISTR_TYPE_=" + DISTR_TYPE_ + ", DISTR_PATH_=" + DISTR_PATH_ + '}';
    }
    
    
}
