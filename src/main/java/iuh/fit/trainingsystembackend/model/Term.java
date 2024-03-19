package iuh.fit.trainingsystembackend.model;

import iuh.fit.trainingsystembackend.enums.TermType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "term")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Term implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "academic_year_id")
    private Long academicYearId;

    @Column(name = "name")
    private String name;

    @Column(name = "term_type")
    @Enumerated(EnumType.STRING)
    private TermType termType;

    @Column(name = "term_start")
    private Date termStart;

    @Column(name = "term_end")
    private Date termEnd;

    @Column(name = "cost_per_credit")
    private Double costPerCredit;
}
