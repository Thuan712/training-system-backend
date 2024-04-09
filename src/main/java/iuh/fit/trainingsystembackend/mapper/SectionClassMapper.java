package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.SectionClassDTO;
import iuh.fit.trainingsystembackend.enums.SectionClassType;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("sectionClassMapper")
@AllArgsConstructor
public class SectionClassMapper {
    private UserRepository userRepository;
    private StudentSectionClassRepository studentSectionClassRepository;
    private TimeAndPlaceRepository timeAndPlaceRepository;
    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;

    public SectionClassDTO mapToDTO(SectionClass sectionClass) {

        UserEntity userEntity = null;
        if (sectionClass.getLecturer() != null) {
            userEntity = userRepository.findById(sectionClass.getLecturer().getUserId()).orElse(null);
        }

        List<TimeAndPlace> timeAndPlaces = timeAndPlaceRepository.findBySectionClassId(sectionClass.getId());

        int numStudentRegisters = 0;

        // Create Status
        String createStatus = "";
        Section section = sectionRepository.findById(sectionClass.getSectionId()).orElse(null);

        if(section != null){
            Course course = courseRepository.findById(section.getCourseId()).orElse(null);

           if(course != null){
               if(course.getCourseDuration().getPractice() > 0 && course.getCourseDuration().getTheory() > 0){
                    if(sectionClass.getSectionClassType().equals(SectionClassType.theory)){

                    }
               }
           }
        }

        return SectionClassDTO.builder()
                .id(sectionClass.getId())

                .lecturerId(sectionClass.getLecturer() != null ? sectionClass.getLecturer().getId() : null)
                .lecturerName(userEntity != null ? userEntity.getFirstName() + " " + userEntity.getLastName() : "")
                .lecturerCode(userEntity != null ? userEntity.getCode() : "")

                .sectionId(sectionClass.getSection() != null ? sectionClass.getSectionId() : null)
                .sectionName(sectionClass.getSection() != null ? sectionClass.getSection().getName() : "")
                .sectionCode(sectionClass.getSection() != null ? sectionClass.getSection().getCode() : "")

                .name((sectionClass.getSection() != null ? sectionClass.getSection().getName() : "") + " - " + (sectionClass.getCode() != null && !sectionClass.getCode().isEmpty() ? sectionClass.getCode() : "") + " (" + (userEntity != null ? userEntity.getFirstName() + " " + userEntity.getLastName() : "") + ")")
                .code(sectionClass.getCode() != null && !sectionClass.getCode().isEmpty() ? sectionClass.getCode() : "")
                .refId(sectionClass.getRefId() != null ? sectionClass.getRefId() : null)
                .note(sectionClass.getNote())
                .sectionClassType(sectionClass.getSectionClassType())
                .sectionClassStatus(sectionClass.getSectionClassStatus())
                .minStudents(sectionClass.getMinStudents())
                .maxStudents(sectionClass.getMaxStudents())
                .timeAndPlaces(timeAndPlaces != null && !timeAndPlaces.isEmpty() ? timeAndPlaces : new ArrayList<>())

                .numberOfStudents(numStudentRegisters)
                .createStatus(sectionClass.getCreateStatus())
                .build();
    }

    public List<SectionClassDTO> mapToDTO(List<SectionClass> sectionClasses) {
        return sectionClasses.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<SectionClassDTO> mapToDTO(Page<SectionClass> sectionClassPage) {
        return sectionClassPage.map(this::mapToDTO);
    }
}
