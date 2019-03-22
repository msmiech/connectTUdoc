package at.connectTUdoc.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * This class represents the layer between the database and the service layer and transforms the model object to an DTO and vise versa.
 */
public class RegistrationCodeDTO extends AbstractDTO {
    @JsonIgnore
    private LocalDateTime expire;
    private Integer code;

    public RegistrationCodeDTO() {
        this.expire = LocalDateTime.now().plusMinutes(5);
        this.code = new SecureRandom().nextInt(9999-1000+1)+1000;
    }

    public LocalDateTime getExpire() {
        return expire;
    }

    public void setExpire(LocalDateTime expire) {
        this.expire = expire;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationCodeDTO that = (RegistrationCodeDTO) o;
        return Objects.equals(getExpire(), that.getExpire()) &&
                Objects.equals(getCode(), that.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getExpire(), getCode());
    }
}
