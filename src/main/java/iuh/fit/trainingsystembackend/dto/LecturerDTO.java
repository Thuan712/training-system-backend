package iuh.fit.trainingsystembackend.dto;

import iuh.fit.trainingsystembackend.enums.Position;
import iuh.fit.trainingsystembackend.enums.Title;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder(toBuilder = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LecturerDTO {
    private Long id;
    private Long userId;
    private String name;
    private String code;
    private Long specializationId;
    private Title title = Title.unknown;
    private Position position = Position.teacher;
    private Date entryDate = new Date();

    // Options
    private String fullName;
}
