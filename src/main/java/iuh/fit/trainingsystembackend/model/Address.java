package iuh.fit.trainingsystembackend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name="address")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Address implements Serializable {
    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id")
    private Long userId;

    @Column(name = "address_line")
    private String addressLine;

    @Column(name = "region_id")
    private Integer regionId;

    @Column(name = "region_name")
    private String regionName;

    @Column(name = "province_code")
    private String provinceCode;

    @Column(name = "province_name")
    private String provinceName;

    @Column(name = "ward_code")
    private String wardCode;

    @Column(name = "ward_name")
    private String wardName;

    @Column(name="phone")
    private String phone;

    @Column(name="created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt = new Date();

    @Column(name="updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date updatedAt = new Date();

    @Transient
    public String getFormattedAddress() {
        String formattedAddress = "";
        if(this.addressLine != null && !this.addressLine.isEmpty()) {
            formattedAddress += this.addressLine;
        }
        if(this.wardName != null && !this.wardName.isEmpty()) {
            formattedAddress += ", " + this.wardName;
        }
        if(this.provinceName != null && !this.provinceName.isEmpty()) {
            formattedAddress += ", " + this.provinceName;
        }
        if(this.regionName != null && !this.regionName.isEmpty()) {
            formattedAddress += ", " + this.regionName;
        }
        return formattedAddress;
    }


}
