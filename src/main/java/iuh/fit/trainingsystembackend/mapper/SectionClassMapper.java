package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.SectionClassDTO;
import iuh.fit.trainingsystembackend.model.SectionClass;
import iuh.fit.trainingsystembackend.model.TimeAndPlace;
import iuh.fit.trainingsystembackend.model.UserEntity;
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
    public SectionClassDTO mapToDTO(SectionClass sectionClass) {

        UserEntity userEntity = null;
        if (sectionClass.getLecturer() != null) {
            userEntity = userRepository.findById(sectionClass.getLecturer().getUserId()).orElse(null);
        }

        List<TimeAndPlace> timeAndPlaces = timeAndPlaceRepository.findBySectionClassId(sectionClass.getId());

        return SectionClassDTO.builder()
                .id(sectionClass.getId())
                .termId(sectionClass.getTermId())
                .termName(sectionClass.getTerm() != null ? sectionClass.getTerm().getName() : "")

                .lecturerId(sectionClass.getLecturer() != null ? sectionClass.getLecturer().getId() : null)
                .lecturerName(userEntity != null ? userEntity.getFirstName() + " " + userEntity.getLastName() : "")
                .lecturerCode(userEntity != null ? userEntity.getCode() : "")

                .sectionId(sectionClass.getSection() != null ? sectionClass.getSectionId() : null)
                .sectionName(sectionClass.getSection() != null ? sectionClass.getSection().getName() : "")
                .sectionCode(sectionClass.getSection() != null ? sectionClass.getSection().getCode() : "")

                .name((sectionClass.getSection() != null ? sectionClass.getSection().getName() : "") + " - " + (sectionClass.getSection() != null ? sectionClass.getSection().getCode() : "") + " (" + (userEntity != null ? userEntity.getFirstName() + " " + userEntity.getLastName() : "") + ")")
                .code(sectionClass.getCode() != null && !sectionClass.getCode().isEmpty() ? sectionClass.getCode() : "")
                .refId(sectionClass.getRefId() != null ? sectionClass.getRefId() : null)
                .numberOfStudents(sectionClass.getNumberOfStudents() != null ? sectionClass.getNumberOfStudents() : 0)
                .note(sectionClass.getNote())
                .sectionClassType(sectionClass.getSectionClassType())
                .sectionClassStatus(sectionClass.getSectionClassStatus())
                .timeAndPlaces(timeAndPlaces != null && !timeAndPlaces.isEmpty() ? timeAndPlaces : new ArrayList<>())
                .build();
    }

    public List<SectionClassDTO> mapToDTO(List<SectionClass> sectionClasses) {
        return sectionClasses.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<SectionClassDTO> mapToDTO(Page<SectionClass> sectionClassPage) {
        return sectionClassPage.map(this::mapToDTO);
    }
}
