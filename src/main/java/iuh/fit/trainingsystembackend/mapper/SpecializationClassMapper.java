package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.SpecializationClassDTO;
import iuh.fit.trainingsystembackend.model.AcademicYear;
import iuh.fit.trainingsystembackend.model.Lecturer;
import iuh.fit.trainingsystembackend.model.Specialization;
import iuh.fit.trainingsystembackend.model.SpecializationClass;
import iuh.fit.trainingsystembackend.repository.AcademicYearRepository;
import iuh.fit.trainingsystembackend.repository.LecturerRepository;
import iuh.fit.trainingsystembackend.repository.SpecializationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("specializationClassMapper")
@AllArgsConstructor
public class SpecializationClassMapper {
    private SpecializationRepository specializationRepository;
    private LecturerRepository lecturerRepository;
    public SpecializationClassDTO mapToDTO(SpecializationClass specializationClass) {

        Specialization specialization = null;
        if(specializationClass.getSpecializationId() != null){
            specialization = specializationRepository.findById(specializationClass.getSpecializationId()).orElse(null);
        }

        String lecturerFullName = "";
        Lecturer lecturer = lecturerRepository.findById(specializationClass.getLecturerId()).orElse(null);
        if(lecturer != null && lecturer.getUserEntity() != null){
            lecturerFullName += lecturer.getUserEntity().getLastName()
                                + " " + lecturer.getUserEntity().getFirstName();
        }

        return SpecializationClassDTO.builder()
                .id(specializationClass.getId())
                .schoolYear(specializationClass.getSchoolYear())
                .name(specializationClass.getName())
                .numberOfStudents(specializationClass.getNumberOfStudents())

                .lecturerId(specializationClass.getLecturerId())
                .lecturerName(lecturerFullName)
                .lecturerCode(lecturer != null && lecturer.getUserEntity() != null ? lecturer.getUserEntity().getCode() : "")

                .specializationId(specialization != null ? specialization.getId() : null)
                .specializationName(specialization != null ? specialization.getName() : "")
                .specializationCode(specialization != null ? specialization.getCode() : "")
                .build();
    }

    public List<SpecializationClassDTO> mapToDTO(List<SpecializationClass> specializationClasses) {
        return specializationClasses.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<SpecializationClassDTO> mapToDTO(Page<SpecializationClass> specializationClassPage) {
        return specializationClassPage.map(this::mapToDTO);
    }
}
