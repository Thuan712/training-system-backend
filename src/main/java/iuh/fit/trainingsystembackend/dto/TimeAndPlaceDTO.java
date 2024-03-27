package iuh.fit.trainingsystembackend.dto;

import iuh.fit.trainingsystembackend.enums.DayInWeek;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder(toBuilder = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TimeAndPlaceDTO {
    private Long id;

    private Long sectionClassId;

    private String room;

    private DayInWeek dayOfTheWeek;

    private Integer periodStart;

    private Integer periodEnd;

    private String note;

    private String name;
}
