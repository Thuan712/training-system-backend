package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.SectionClassDTO;
import iuh.fit.trainingsystembackend.dto.StudentDTO;
import iuh.fit.trainingsystembackend.dto.StudentSectionDTO;
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

    private StudentSectionMapper studentSectionMapper;
    private final ScheduleRepository scheduleRepository;
    private final TermRepository termRepository;
    private StudentMapper studentMapper;

    public SectionClassDTO mapToDTO(SectionClass sectionClass) {
        Term term = null;

        if(sectionClass.getSection() != null){
            term = termRepository.findById(sectionClass.getSection().getTermId()).orElse(null);
        }

        UserEntity userEntity = null;
        if (sectionClass.getLecturer() != null) {
            userEntity = userRepository.findById(sectionClass.getLecturer().getUserId()).orElse(null);
        }

        List<TimeAndPlace> timeAndPlaces = timeAndPlaceRepository.findBySectionClassId(sectionClass.getId());

        int numStudentRegisters = 0;

        List<Schedule> schedules = scheduleRepository.findScheduleBySectionClassId(sectionClass.getId()).stream()
                .sorted((o1, o2) -> o1.getLearningDate().compareTo(o2.getLearningDate())).collect(Collectors.toList());

        List<Student> students = studentSectionClassRepository.findBySectionClassId(sectionClass.getId()).stream().map(StudentSectionClass::getStudent).collect(Collectors.toList());
        List<StudentDTO> studentDTOS = studentMapper.mapToDTO(students);

        String sectionClassType = sectionClass.getSectionClassType().equals(SectionClassType.theory) ? " - Lý thuyết" : " - Thực hành";
        List<StudentSection> studentSections = studentSectionClassRepository.findBySectionClassId(sectionClass.getId()).stream().map(StudentSectionClass::getStudentSection).collect(Collectors.toList());
        List<StudentSectionDTO> studentSectionDTOS = studentSectionMapper.mapToDTO(studentSections);

        // Section Class Status
        return SectionClassDTO.builder()
                .id(sectionClass.getId())

                .termId(term != null ? term.getId() : null)
                .termName(term != null ? term.getName() : "")

                .lecturerId(sectionClass.getLecturer() != null ? sectionClass.getLecturer().getId() : null)
                .lecturerName(userEntity != null ? userEntity.getFirstName() + " " + userEntity.getLastName() : "")
                .lecturerCode(userEntity != null ? userEntity.getCode() : "")

                .sectionId(sectionClass.getSection() != null ? sectionClass.getSectionId() : null)
                .sectionName(sectionClass.getSection() != null ? sectionClass.getSection().getName() : "")
                .sectionCode(sectionClass.getSection() != null ? sectionClass.getSection().getCode() : "")

                .name((sectionClass.getSection() != null ? sectionClass.getSection().getName() : "") + sectionClassType + " - " + (sectionClass.getCode() != null && !sectionClass.getCode().isEmpty() ? sectionClass.getCode() : "") + " (" + (userEntity != null ? userEntity.getFirstName() + " " + userEntity.getLastName() : "") + ")")
                .code(sectionClass.getCode() != null && !sectionClass.getCode().isEmpty() ? sectionClass.getCode() : "")
                .refId(sectionClass.getRefId() != null ? sectionClass.getRefId() : null)
                .note(sectionClass.getNote())
                .sectionClassType(sectionClass.getSectionClassType())
                .sectionClassStatus(null)
                .minStudents(sectionClass.getMinStudents())
                .maxStudents(sectionClass.getMaxStudents())
                .timeAndPlaces(timeAndPlaces != null && !timeAndPlaces.isEmpty() ? timeAndPlaces : new ArrayList<>())

                .numberOfStudents(studentDTOS.size())
                .students(studentDTOS)
                .studentSections(studentSectionDTOS)
                .createStatus(sectionClass.getCreateStatus())
                .inputResultEnable(sectionClass.getInputResultEnable())

                .startDate(!schedules.isEmpty() ? schedules.get(0).getLearningDate() : null)
                .endDate(!schedules.isEmpty() ? schedules.get(schedules.size() - 1).getLearningDate() : null)
                .timeAndPlaceStatus(timeAndPlaces != null && !timeAndPlaces.isEmpty() && !schedules.isEmpty())
                .numberOfPeriods(schedules.size())
                .build();
    }

    public List<SectionClassDTO> mapToDTO(List<SectionClass> sectionClasses) {
        return sectionClasses.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<SectionClassDTO> mapToDTO(Page<SectionClass> sectionClassPage) {
        return sectionClassPage.map(this::mapToDTO);
    }
}
