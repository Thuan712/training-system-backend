package iuh.fit.trainingsystembackend.bean;

import com.google.gson.annotations.Expose;
import iuh.fit.trainingsystembackend.enums.*;
import iuh.fit.trainingsystembackend.model.Address;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
public class UserBean extends AddressBean implements Serializable {
    //#region User
    @Expose
    private Long id;

    private String username;

    private String email;

    private String password;

    private String lastName;

    private String firstName;

    private SystemRole systemRole;
    private Gender gender;

    private String code;

    private String dob;

    private String CINumber;

    private String avatar;
    //#endregion

    //#region Student
    private Long specializationClassId;
    private TypeOfEducation typeOfEducation = TypeOfEducation.general_program;
    private String activationEmail;
    //#endregion

    //#region Lecturer
    private Long specializationId;

    private Title title = Title.unknown;

    private Position position = Position.teacher;
    //#endregion

    private Date entryDate = new Date();
}
