package entities;

import java.util.Date;

/**
 *
 * @author Artem Adamenko  <adamenko.artem@gmail.com>
 * Класс для хранения данных скорости по времени для диаграммы
 */
public class SpeedBus {
    
    public Integer speed_;
    public Date time_;

    public Integer getSpeed_() {
        return speed_;
    }

    public Date getTime_() {
        return time_;
    }

    public void setSpeed_(Integer speed_) {
        this.speed_ = speed_;
    }

    public void setTime_(Date time_) {
        this.time_ = time_;
    }

    @Override
    public String toString() {
        return "SpeedBus{" + "speed_=" + speed_ + ", time_=" + time_ + '}';
    }
    
    
    
}
