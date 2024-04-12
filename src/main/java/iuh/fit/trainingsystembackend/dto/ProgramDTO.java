package iuh.fit.trainingsystembackend.dto;

import iuh.fit.trainingsystembackend.model.ProgramTerm;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import java.util.Date;
import java.util.List;

@Data
@Builder(toBuilder = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProgramDTO {
    private Long id;
    private String name;

    private Long specializationId;
    private String specializationName;
    private String specializationCode;

    private Long academicYearId;
    private String academicYearName;

    private List<ProgramTermDTO> programTerms;

    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private Boolean deleted;
}
