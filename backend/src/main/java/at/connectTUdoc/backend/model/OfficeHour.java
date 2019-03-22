package at.connectTUdoc.backend.model;

import at.connectTUdoc.backend.helper.MedConnectConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Objects;

/**
 * This class represents the sub-model of the database
 */
@Entity
public class OfficeHour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonFormat(pattern = MedConnectConstants.JSON_FORMAT_LOCALTIME)
    private LocalTime beginTime;
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonFormat(pattern = MedConnectConstants.JSON_FORMAT_LOCALTIME)
    private LocalTime endTime;
    @Enumerated(EnumType.STRING)
    private DayType daytype;

    public OfficeHour() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OfficeHour that = (OfficeHour) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(beginTime, that.beginTime) &&
                Objects.equals(endTime, that.endTime) &&
                daytype == that.daytype;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, beginTime, endTime, daytype);
    }

    public OfficeHour(Office office, LocalTime begin, LocalTime end, DayType daytype) {
        this.beginTime = begin;
        this.endTime = end;
        this.daytype = daytype;
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

    public void setBeginTime(LocalTime begin) {
        this.beginTime = begin;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime end) {
        this.endTime = end;
    }

    public DayType getDaytype() {
        return daytype;
    }

    public void setDaytype(DayType daytype) {
        this.daytype = daytype;
    }
}
