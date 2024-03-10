package iuh.fit.trainingsystembackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "faculty")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Faculty implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "logo")
    private String logo;

    @Column(name = "head_name")
    private String headName;

    @Column(name = "head_email")
    private String headEmail;

    @Column(name = "head_phone")
    private String headPhone;

    @Column(name = "establishment_date")
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date establishmentDate = new Date();
}
