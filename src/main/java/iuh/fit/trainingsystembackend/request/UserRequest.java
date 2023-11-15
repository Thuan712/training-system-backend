package iuh.fit.trainingsystembackend.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import iuh.fit.trainingsystembackend.enums.*;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest {
    //#region User
    @Expose
    private Long id;

    private String username;

    private String email;

    private String firstname;

    private String lastname;

    private String password;

    private SystemRole systemRole;

    private String code;

    private String dob;

    private String CINumber;

    private String avatar;

    private boolean active;

    private boolean deleted;

    private Date createdAt;

    private Date deletedAt;

    private Date updatedAt;

    //#region Student
    private Long specializationClassId;

    private TrainingLevel trainingLevel;

    private TypeOfEducation typeOfEducation;
    //#endregion

    //#region Lecturer
    private Long specializationId;

    private Title title;

    private Position position;

    private Date entryDate;
    //#endregion
}
