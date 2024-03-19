package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.CourseDTO;
import iuh.fit.trainingsystembackend.dto.RegistrationDTO;
import iuh.fit.trainingsystembackend.enums.SectionClassStatus;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.SectionClassRepository;
import iuh.fit.trainingsystembackend.repository.SpecializationRepository;
import iuh.fit.trainingsystembackend.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("registrationMapper")
@AllArgsConstructor
public class RegistrationMapper {
    private SectionClassRepository sectionClassRepository;
    private StudentRepository studentRepository;
    public RegistrationDTO mapToDTO(StudentSectionClass studentSectionClass) {
        SectionClass sectionClass = null;
        Student student = null;
        Section section = null;
        Lecturer lecturer = null;
        double totalCost = 0D;


        if(studentSectionClass.getSectionClassId() != null){
            sectionClass = sectionClassRepository.findById(studentSectionClass.getSectionClassId()).orElse(null);

            if(sectionClass != null && sectionClass.getSection() != null){
                section = sectionClass.getSection();
            }

            if(section != null && sectionClass.getLecturer() != null){
                lecturer = sectionClass.getLecturer();
            }

            if(section != null && sectionClass.getTerm() != null){
                totalCost = section.getCostCredits() * sectionClass.getTerm().getCostPerCredit();
            }
        }

        if(studentSectionClass.getStudentId() != null){
            student = studentRepository.findById(studentSectionClass.getStudentId()).orElse(null);
        }

        return RegistrationDTO.builder()
                .id(studentSectionClass.getId())

                .studentId(student != null ? student.getId() : null)

                .sectionClassId(sectionClass != null ? sectionClass.getId() : null)
                .sectionClassCode(sectionClass != null ? sectionClass.getCode() : "")
                .sectionClassStatus(sectionClass != null ? sectionClass.getSectionClassStatus() : SectionClassStatus.open)

                .sectionId(section != null ? section.getId() : null)
                .sectionName(section != null ? section.getName() : "")
                .sectionCode(section != null ? section.getCode() : "")
                .credits(section != null ? section.getCredits() : 0)
                .costCredits(section != null ? section.getCostCredits() : 0)

                .lecturerId(lecturer != null ? lecturer.getId() : null)
                .lecturerName(lecturer != null && lecturer.getUserEntity() != null ? lecturer.getUserEntity().getFirstName() + " " + lecturer.getUserEntity().getLastName() : "")

                .total(totalCost)
                .status(studentSectionClass.getStatus())
                .type(studentSectionClass.getRegistrationType())
                .createdAt(studentSectionClass.getCreatedAt())
                .build();
    }

    public List<RegistrationDTO> mapToDTO(List<StudentSectionClass> studentSectionClasses) {
        return studentSectionClasses.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<RegistrationDTO> mapToDTO(Page<StudentSectionClass> studentSectionClassPage) {
        return studentSectionClassPage.map(this::mapToDTO);
    }
}
