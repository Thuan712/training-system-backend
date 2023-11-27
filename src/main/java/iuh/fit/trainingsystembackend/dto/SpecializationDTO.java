package iuh.fit.trainingsystembackend.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SpecializationDTO implements Serializable {
    private Long id;
    private Long facultyId;
    private String facultyName;
    private String name;
    private String code;
}
