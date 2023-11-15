package com.thinkvitals.dto;

import com.google.gson.annotations.Expose;
import com.thinkvitals.address.enums.AddressType;
import com.thinkvitals.data.PhoneExtension;
import com.thinkvitals.enums.Gender;
import com.thinkvitals.enums.SystemRole;
import com.thinkvitals.enums.Title;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder(toBuilder = true)
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserInfoDTO implements Serializable {
    //#region User
    @Expose
    private Long id;
    private String username;
    private String email;
    private String password;
    private SystemRole systemRole;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Title title;
    private String avatar;
    private String dob;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private boolean active;
    private boolean deleted;
    //#endregion

    //#region Address
    private Long addressId;
    private AddressType type;
    private String address;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressLine4;
    private String city;
    private String state;
    private String refState;
    private String country;
    private String refCountry;
    private String postalCode;
    private String phone;
    private PhoneExtension phoneExtension;
    //#endregion
}
