package iuh.fit.trainingsystembackend.dto;

import iuh.fit.trainingsystembackend.data.RequireSection;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder(toBuilder = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CourseDTO {
    private Long id;

    private String name;

    private String code;

    private String description;

    private Date createdAt;

    private Date deletedAt;

    private Boolean deleted;
}
