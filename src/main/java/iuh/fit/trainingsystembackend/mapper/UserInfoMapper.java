package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.UserInfoDTO;
import iuh.fit.trainingsystembackend.enums.Gender;
import iuh.fit.trainingsystembackend.enums.SystemRole;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.*;
import iuh.fit.trainingsystembackend.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("userInfoMapper")
@AllArgsConstructor
public class UserInfoMapper {
    private AddressService addressService;
    private LecturerRepository lecturerRepository;
    private StudentRepository studentRepository;
    private SpecializationRepository specializationRepository;
    private SpecializationClassRepository specializationClassRepository;
    private AcademicYearRepository academicYearRepository;
    public UserInfoDTO mapToDTO(UserEntity userEntity){

        Address userAddress = addressService.getUserAddress(userEntity.getId());

        Lecturer lecturer = null;
        Student student = null;
        Specialization specialization = null;
        SpecializationClass specializationClass = null;
        AcademicYear academicYear = null;

        if(userEntity.getSystemRole() == SystemRole.lecturer){
            lecturer = lecturerRepository.getLecturersByUserId(userEntity.getId());
            if(lecturer != null){
                specialization = specializationRepository.findById(lecturer.getSpecializationId()).orElse(null);
            }
        } else if (userEntity.getSystemRole() == SystemRole.student){
            student = studentRepository.getStudentByUserId(userEntity.getId());
            if(student != null){
                specialization = specializationRepository.findById(student.getSpecializationId()).orElse(null);

                if(student.getSpecializationClassId() != null){
                    specializationClass = specializationClassRepository.findById(student.getSpecializationClassId()).orElse(null);
                }

                academicYear = academicYearRepository.findById(student.getAcademicYearId()).orElse(null);
            }
        }

        return UserInfoDTO.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .fullName(userEntity.getFirstName() + " " + userEntity.getLastName())
                .email(userEntity.getEmail())
                .avatar((userEntity.getAvatar()))
                .username(userEntity.getUsername())
                .gender(userEntity.getGender() != null ? userEntity.getGender().name() : Gender.unknown.name())
                .dob(userEntity.getDob())
                .active(userEntity.isActive())
                .deleted(userEntity.isDeleted())
                .deletedAt(userEntity.getDeletedAt())
                .systemRole(userEntity.getSystemRole())
                .code(userEntity.getCode())
                //Address
                .address(userAddress)
                // Student
                .typeOfEducation(student != null ? student.getTypeOfEducation().getValue() : "")
                .specializationName(specialization != null ? specialization.getName() : "")
                .specializationClassName(specializationClass != null ? specializationClass.getName() : "")
                .academicYearName(academicYear != null ? academicYear.getName() : "")
                // Lecturer
                .title(lecturer != null ? lecturer.getTitle().getValue() : "")

                .build();
    }

    public List<UserInfoDTO> mapToDTO(List<UserEntity> userEntityList){
        return userEntityList.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<UserInfoDTO> mapToDTO(Page<UserEntity> userEntityPage){
        return userEntityPage.map(this::mapToDTO);
    }
}
