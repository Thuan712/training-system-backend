package iuh.fit.trainingsystembackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "section_class")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SectionClass implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lecturer_id")
    private Long lecturerId;

    @Column(name = "section_id")
    private Long sectionId;

    @Column(name = "class_code")
    private String classCode;

    @Column(name = "room")
    private String room;

    @Column(name = "period_from")
    private Integer periodFrom;

    @Column(name = "period_to")
    private Integer periodTo;

    @Column(name = "note")
    private String note;
}
