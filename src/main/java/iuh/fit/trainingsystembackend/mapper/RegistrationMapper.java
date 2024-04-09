package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.CourseDTO;
import iuh.fit.trainingsystembackend.dto.RegistrationDTO;
import iuh.fit.trainingsystembackend.enums.DayInWeek;
import iuh.fit.trainingsystembackend.enums.SectionClassStatus;
import iuh.fit.trainingsystembackend.enums.TuitionStatus;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("registrationMapper")
@AllArgsConstructor
public class RegistrationMapper {
    private SectionClassRepository sectionClassRepository;
    private StudentRepository studentRepository;
    private TimeAndPlaceRepository timeAndPlaceRepository;
    private ScheduleRepository scheduleRepository;
    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;
    private final TuitionRepository tuitionRepository;
    private final StudentSectionClassRepository studentSectionClassRepository;
    private final TermRepository termRepository;

    public RegistrationDTO mapToDTO(StudentSection studentSection) {
        Course course = null;
        Tuition tuition = null;
        Term term = null;

        // Student Section
        Section section = sectionRepository.findById(studentSection.getSectionId()).orElse(null);

        // Tuition
        double totalCost = 0D;

        // Student Section Class
        List<StudentSectionClass> studentSectionClasses = studentSectionClassRepository.findByStudentSectionId(studentSection.getId());

        // Tuition
        double total = 0;
        double debt = 0;
        if(section != null){
            term = termRepository.findById(section.getTermId()).orElse(null);
            course = courseRepository.findById(section.getCourseId()).orElse(null);
            tuition = tuitionRepository.findBySectionId(section.getId());

            if(tuition != null){
                total = (tuition.getInitialFee() - tuition.getDiscountFee());
//                if(tuition.get().equals(TuitionStatus.paid)){
//                    debt = 0D;
//                } else {
//                    debt = total;
//                }
            }
        }

        return RegistrationDTO.builder()
                .id(studentSection.getId())
                .registrationStatus(studentSection.getRegistrationStatus())
                .createdAt(studentSection.getCreatedAt())

                .sectionId(section != null ? section.getId() : null)
                .sectionName(section != null ? section.getName() : "")
                .sectionCode(section != null ? section.getCode() : "")

                // Course
                .courseId(course != null ? course.getId() : null)
                .courseCode(course != null ? course.getCode() : "")
                .courseName(course != null ? course.getName() : "")
                .credits(course != null ? course.getCredits() : 0)
                .costCredits(course != null ? course.getCostCredits() : 0)

                // Term
                .termId(term != null ? term.getId() : null)
                .termName(term != null ? term.getName() : "")

                // TODO: Tuition
                .tuitionId(tuition != null ? tuition.getId() : null)
                .initialFee(tuition != null ? tuition.getInitialFee() : null)
                .debt(debt)
                .discountAmount(tuition != null ? tuition.getDiscountAmount() : null)
                .discountFee(tuition != null ? tuition.getDiscountFee() : null)
                .paymentDeadline(tuition != null ? tuition.getPaymentDeadline() : null)
                .total(total)

                .build();
    }

    public List<RegistrationDTO> mapToDTO(List<StudentSection> studentSections) {
        return studentSections.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<RegistrationDTO> mapToDTO(Page<StudentSection> studentSections) {
        return studentSections.map(this::mapToDTO);
    }
}
