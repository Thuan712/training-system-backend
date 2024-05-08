package iuh.fit.trainingsystembackend.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import java.util.Date;

@Data
@Builder(toBuilder = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AcademicYearDTO {
    private Long id;
    private String name;
    private Integer yearStart;

    private Long firstTermId;
    private String firstTermName;
    private Double costFirstTerm;
    private String firstTermStart;
    private String firstTermEnd;

    private Long secondTermId;
    private Double costSecondTerm;
    private String secondTermName;
    private String secondTermStart;
    private String secondTermEnd;

    private Long thirdTermId;
    private String thirdTermName;
    private Double costThirdTerm;
    private String thirdTermStart;
    private String thirdTermEnd;
}
