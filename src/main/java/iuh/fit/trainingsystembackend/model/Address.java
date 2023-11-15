package com.thinkvitals.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.thinkvitals.data.PhoneExtension;
import com.thinkvitals.enums.AddressRefType;
import com.thinkvitals.enums.AddressType;
import com.thinkvitals.utils.DateUtils;
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

    @Column(name="ref_id")
    private Long refId;

    @Column(name="ref_type")
    @Enumerated(EnumType.STRING)
    private AddressRefType refType = AddressRefType.none;

    @Column(name="type")
    @Enumerated(EnumType.STRING)
    private AddressType type = AddressType.other;

    @Column(name="main")
    @Expose
    private boolean main = false;

    @Column(name="address_line_1")
    private String addressLine1;

    @Column(name="address_line_2")
    private String addressLine2;

    @Column(name="address_line_3")
    private String addressLine3;

    @Column(name="address_line_4")
    private String addressLine4;

    @Column(name="city")
    private String city;

    @Column(name="state")
    private String state;

    @Column(name="ref_state")
    private String refState;

    @Column(name="country")
    private String country;

    @Column(name="ref_country")
    private String refCountry;

    @Column(name="postal_code")
    private String postalCode;

    @Column(name="phone")
    private String phone;

    @Column(name="phone_extension")
    @JsonIgnore
    private String phoneExtensionString;

    @Transient
    private PhoneExtension phoneExtension;

    @Transient
    public PhoneExtension getPhoneExtension() {
        if(this.phoneExtensionString != null && !this.phoneExtensionString.isEmpty()) {
            return new Gson().fromJson(this.phoneExtensionString, new TypeToken<PhoneExtension>(){}.getType());
        }

        return null;
    }

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
        if(this.addressLine1 != null && !this.addressLine1.isEmpty()) {
            formattedAddress += this.addressLine1;
        }
        if(this.addressLine2 != null && !this.addressLine2.isEmpty()) {
            formattedAddress += ", " + this.addressLine2;
        }
        if(this.addressLine3!= null && !this.addressLine3.isEmpty()) {
            formattedAddress += ", " + this.addressLine3;
        }
        if(this.addressLine4 != null && !this.addressLine4.isEmpty()) {
            formattedAddress += ", " + this.addressLine4;
        }
        if(this.city != null && !this.city.isEmpty()) {
            formattedAddress += ", " + this.city;
        }
        if(this.state != null && !this.state.isEmpty()) {
            formattedAddress += ", " + this.state;
        }
        if(this.country != null && !this.country.isEmpty()) {
            formattedAddress += ", " + this.country;
        }
        if(this.postalCode != null && !this.postalCode.isEmpty()) {
            formattedAddress += ", " + this.postalCode;
        }
        return formattedAddress;
    }


}
