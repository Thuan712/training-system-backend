package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.SectionClassDTO;
import iuh.fit.trainingsystembackend.dto.SectionDTO;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.LecturerRepository;
import iuh.fit.trainingsystembackend.repository.SectionRepository;
import iuh.fit.trainingsystembackend.repository.StudentSectionClassRepository;
import iuh.fit.trainingsystembackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("sectionClassMapper")
@AllArgsConstructor
public class SectionClassMapper {
    private LecturerRepository lecturerRepository;
    private SectionRepository sectionRepository;
    private UserRepository userRepository;
    private StudentSectionClassRepository studentSectionClassRepository;
    public SectionClassDTO mapToDTO(SectionClass sectionClass) {
        Lecturer lecturer = null;
        Section section = null;
        UserEntity userEntity= null;
        if(sectionClass.getLecturerId() != null){
            lecturer = lecturerRepository.findById(sectionClass.getLecturerId()).orElse(null);

            if (lecturer != null){
                userEntity = userRepository.findById(lecturer.getUserId()).orElse(null);
            }
        }

        Integer registered = studentSectionClassRepository.countAllBySectionClassId(sectionClass.getId());

        return SectionClassDTO.builder()
                .id(sectionClass.getId())
                .lecturerId(lecturer != null ? lecturer.getId() : null)
                .lecturerName(userEntity != null ? userEntity.getFirstName() + " " + userEntity.getLastName() : "")
                .lecturerCode(userEntity != null ? userEntity.getCode() : "")
                .sectionId(sectionClass.getSection() != null ? sectionClass.getSectionId() : null)
                .sectionName(sectionClass.getSection() != null ? sectionClass.getSection().getName() : "")
                .sectionCode(sectionClass.getSection() != null ? sectionClass.getSection().getCode() : "")
                .classCode(sectionClass.getClassCode())
                .room(sectionClass.getRoom())
                .periodTo(sectionClass.getPeriodTo())
                .periodFrom(sectionClass.getPeriodFrom())
                .numberOfStudents(sectionClass.getNumberOfStudents())
                .dayInWeek(sectionClass.getDayInWeek())
                .note(sectionClass.getNote())
                .sectionClassType(sectionClass.getSectionClassType())
                .startedAt(sectionClass.getStartedAt())
                .registered(registered)
                .build();
    }

    public List<SectionClassDTO> mapToDTO(List<SectionClass> sectionClasses) {
        return sectionClasses.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<SectionClassDTO> mapToDTO(Page<SectionClass> sectionClassPage) {
        return sectionClassPage.map(this::mapToDTO);
    }
}
