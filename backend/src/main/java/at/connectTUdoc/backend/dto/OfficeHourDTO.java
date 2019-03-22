package at.connectTUdoc.backend.dto;

import at.connectTUdoc.backend.helper.MedConnectConstants;
import at.connectTUdoc.backend.model.DayType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;

import java.time.LocalTime;
import java.util.Objects;

/**
 * This class represents the layer between the database and the service layer and transforms the model object to an DTO and vise versa.
 */
public class OfficeHourDTO extends AbstractDTO{

    private Long id;
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonFormat(pattern = MedConnectConstants.JSON_FORMAT_LOCALTIME)
    private LocalTime beginTime;
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonFormat(pattern = MedConnectConstants.JSON_FORMAT_LOCALTIME)
    private LocalTime endTime;
    private DayType daytype;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OfficeHourDTO that = (OfficeHourDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(beginTime, that.beginTime) &&
                Objects.equals(endTime, that.endTime) &&
                daytype == that.daytype;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, beginTime, endTime, daytype);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public DayType getDaytype() {
        return daytype;
    }

    public void setDaytype(DayType daytype) {
        this.daytype = daytype;
    }

    @Override
    public String toString(){
        return "ID: " + getId() +
                "\nB-Time:" + getBeginTime() +
                "\nE-Time:" + getEndTime() +
                "\nDayType: " + getDaytype();

    }
}
