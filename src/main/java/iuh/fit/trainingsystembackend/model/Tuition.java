package iuh.fit.trainingsystembackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import iuh.fit.trainingsystembackend.enums.TuitionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Data
@Table(name = "tuition_fee")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tuition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "initial_fee")
    private Double initialFee;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Column(name = "discount_fee")
    private Double discountFee;

    @Column(name = "plus_deductions")
    private Double plusDeductions;

    @Column(name = "minus_deductions")
    private Double minusDeductions;

    @Column(name = "other_information")
    private String otherInformation;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TuitionStatus status;

    @Column(name = "investigate_status")
    private Boolean investigateStatus;

}
