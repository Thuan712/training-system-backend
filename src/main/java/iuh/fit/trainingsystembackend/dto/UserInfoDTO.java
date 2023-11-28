package iuh.fit.trainingsystembackend.dto;

import com.google.gson.annotations.Expose;

import iuh.fit.trainingsystembackend.enums.*;
import iuh.fit.trainingsystembackend.model.Address;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
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
    private String firstName;
    private String lastName;
    private String fullName;
    private String password;

    private SystemRole systemRole;
    private String gender;
    private String code;

    private String dob;

    private String CINumber;

    private String avatar;

    private boolean active;

    private boolean deleted;

    private Date createdAt;

    private Date deletedAt;

    private Date updatedAt;
    //#endregion

    //#region Address
    private Long addressId;

    private String addressLine;

    private Integer regionId;
    private String regionName;

    private String districtName;
    private String districtCode;

    private String provinceCode;
    private String provinceName;

    private String wardCode;
    private String wardName;

    private String phone;
    private String formattedAddress;
    //#endregion

    //#region Student
    private Long specializationClassId;
    private String specializationClassName;

    private TrainingLevel trainingLevel;

    private String typeOfEducation;
    private String schoolYear;
    //#endregion

    //#region Lecturer
    private Long specializationId;
    private String specializationName;

    private String titleValue;
    private Title title;
    private String positionValue;
    private Position position;
    private Date entryDate;
    //#endregion
}
