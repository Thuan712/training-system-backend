package iuh.fit.trainingsystembackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import iuh.fit.trainingsystembackend.enums.Position;
import iuh.fit.trainingsystembackend.enums.Title;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "lecturer")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Lecturer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinFormula(value = "user_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private UserEntity userEntity;


    @Column(name = "specialization_id")
    private Long specializationId;

    @Column(name = "title")
    @Enumerated(EnumType.STRING)
    private Title title = Title.unknown;

    @Column(name = "position")
    @Enumerated(EnumType.STRING)
    private Position position = Position.teacher;

    @Column(name = "entry_date")
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date entryDate = new Date();
}
