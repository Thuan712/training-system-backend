package iuh.fit.trainingsystembackend.dto;

import iuh.fit.trainingsystembackend.enums.TermType;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import java.util.Date;

@Data
@Builder(toBuilder = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TermDTO {
    private Long id;
    private Long academicYearId;
    private String academicYearName;

    private TermType termType;
    private String name;

    private Date termStart;
    private Date termEnd;

    private Double costPerCredit;
}
