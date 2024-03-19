package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.UserInfoDTO;
import iuh.fit.trainingsystembackend.enums.Gender;
import iuh.fit.trainingsystembackend.enums.SystemRole;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.*;
import iuh.fit.trainingsystembackend.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public UserInfoDTO mapToDTO(UserEntity userEntity) {

        Address userAddress = addressService.getUserAddress(userEntity.getId());

        Lecturer lecturer = null;
        Student student = null;
        Specialization specialization = null;
        SpecializationClass specializationClass = null;

        if (userEntity.getSystemRole() == SystemRole.lecturer) {
            lecturer = lecturerRepository.getLecturersByUserId(userEntity.getId());
            if (lecturer != null) {
                specialization = specializationRepository.findById(lecturer.getSpecializationId()).orElse(null);
            }
        } else if (userEntity.getSystemRole() == SystemRole.student) {
            student = studentRepository.getStudentByUserId(userEntity.getId());
            if (student != null) {
                specialization = specializationRepository.findById(student.getSpecializationId()).orElse(null);

                if(student.getSpecializationClassId() != null){
                    specializationClass =  specializationClassRepository.findById(student.getSpecializationClassId()).orElse(null);
                }
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
                .CINumber(userEntity.getCINumber())
                //Address
                .addressId(userAddress != null ? userAddress.getId() : null)

                .addressLine(userAddress != null ? userAddress.getAddressLine() : "")

                .regionId(userAddress != null ? userAddress.getRegionId() : null)
                .regionName(userAddress != null ? userAddress.getRegionName() : "")

                .districtName(userAddress != null ? userAddress.getDistrictName() : "")
                .districtCode(userAddress != null ? userAddress.getDistrictCode() : "")

                .provinceCode(userAddress != null ? userAddress.getProvinceCode() : "")
                .provinceName(userAddress != null ? userAddress.getProvinceName() : "")

                .wardCode(userAddress != null ? userAddress.getWardCode() : "")
                .wardName(userAddress != null ? userAddress.getWardName() : "")

                .phone(userAddress != null ? userAddress.getPhone() : "")
                .formattedAddress(userAddress != null ? userAddress.getFormattedAddress() : "")
                // Student
                .typeOfEducation(student != null ? student.getTypeOfEducation().getValue() : "")
                .specializationId(specialization != null ? specialization.getId() : null)
                .specializationName(specialization != null ? specialization.getName() : "")
                .specializationClassName(specializationClass != null ? specializationClass.getName() : "")
                .schoolYear(student != null ? student.getSchoolYear() : "")
                // Lecturer
                .titleValue(lecturer != null ? lecturer.getTitle().getValue() : "")
                .title(lecturer != null ? lecturer.getTitle() : null)
                .positionValue(lecturer != null ? lecturer.getPosition().getValue() : "")
                .position(lecturer != null ? lecturer.getPosition() : null)
                .build();
    }

    public List<UserInfoDTO> mapToDTO(List<UserEntity> userEntityList) {
        return userEntityList.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<UserInfoDTO> mapToDTO(Page<UserEntity> userEntityPage) {
        return userEntityPage.map(this::mapToDTO);
    }
}
