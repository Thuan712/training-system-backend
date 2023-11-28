package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.SpecializationDTO;
import iuh.fit.trainingsystembackend.dto.StudentDTO;
import iuh.fit.trainingsystembackend.model.Faculty;
import iuh.fit.trainingsystembackend.model.Specialization;
import iuh.fit.trainingsystembackend.model.Student;
import iuh.fit.trainingsystembackend.model.UserEntity;
import iuh.fit.trainingsystembackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("studentMapper")
@AllArgsConstructor
public class StudentMapper {
    private UserRepository userRepository;
    public StudentDTO mapToDTO(Student student) {
        UserEntity userEntity = userRepository.findById(student.getUserId()).orElse(null);

        return StudentDTO.builder()
                .id(student.getId())
                .specializationId(student.getSpecializationId())
                .specializationId(student.getSpecializationId())
                .userId(userEntity != null ? userEntity.getId() : null)
                .name(userEntity != null ? userEntity.getFirstName() + " " + userEntity.getLastName() : "")
                .code(userEntity != null ? userEntity.getCode() : "")
                .schoolYear(student.getSchoolYear())
                .typeOfEducation(student.getTypeOfEducation())
                .specializationClassId(student.getSpecializationClassId())
                .entryDate(student.getEntryDate())
                .build();
    }

    public List<StudentDTO> mapToDTO(List<Student> students) {
        return students.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<StudentDTO> mapToDTO(Page<Student> studentPage) {
        return studentPage.map(this::mapToDTO);
    }
}
