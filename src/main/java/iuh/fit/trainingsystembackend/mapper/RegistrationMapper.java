package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.CourseDTO;
import iuh.fit.trainingsystembackend.dto.RegistrationDTO;
import iuh.fit.trainingsystembackend.dto.StudentDTO;
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
    private final ResultRepository resultRepository;
    private StudentMapper studentMapper;
    private final StudentTuitionRepository studentTuitionRepository;

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
        StudentDTO studentDTO = null;
        Student student = studentRepository.findById(studentSection.getStudentId()).orElse(null);
        if (student != null) {
            studentDTO = studentMapper.mapToDTO(student);
        }

        StudentTuition studentTuition = null;
        if(student != null && tuition != null ){
            studentTuition = studentTuitionRepository.findByStudentIdAndTuitionId(student.getId(), tuition.getId());
        }

        if (section != null) {
            course = courseRepository.findById(section.getCourseId()).orElse(null);
        }

        Result result = resultRepository.findById(studentSection.getResultId()).orElse(null);


        double totalPoint = 0;
        if (result != null && result.getFinalPoint() != null && result.getFinalPoint() > 0) {
            int totalRegular = 0;
            int totalRegularPoint = 0;
            if (result.getRegularPoint1() != null) {
                totalRegularPoint += result.getRegularPoint1();
                totalRegular++;
            }
            if (result.getRegularPoint2() != null) {
                totalRegularPoint += result.getRegularPoint2();
                totalRegular++;
            }
            if (result.getRegularPoint3() != null) {
                totalRegularPoint += result.getRegularPoint3();
                totalRegular++;
            }
            if (result.getRegularPoint4() != null) {
                totalRegularPoint += result.getRegularPoint4();
                totalRegular++;
            }
            if (result.getRegularPoint5() != null) {
                totalRegularPoint += result.getRegularPoint5();
                totalRegular++;
            }

            double regular = (double) totalRegularPoint / totalRegular;

            int totalMidTerm = 0;
            int totalMidTermPoint = 0;
            if (result.getMidtermPoint1() != null) {
                totalMidTermPoint += result.getMidtermPoint1();
                totalMidTerm++;
            }
            if (result.getMidtermPoint2() != null) {
                totalMidTermPoint += result.getMidtermPoint2();
                totalMidTerm++;
            }
            if (result.getMidtermPoint3() != null) {
                totalMidTermPoint += result.getMidtermPoint3();
                totalMidTerm++;
            }
            double midterm = (double) totalMidTermPoint / totalMidTerm;

            int totalPractice = 0;
            int totalPracticePoint = 0;
            if (result.getPracticePoint1() != null) {
                totalPracticePoint += result.getPracticePoint1();
                totalPractice++;
            }
            if (result.getPracticePoint2() != null) {
                totalPracticePoint += result.getPracticePoint2();
                totalPractice++;
            }
            double practice = (double) totalPracticePoint / totalPractice;

            double finalPoint = result.getFinalPoint();

            if (totalPractice == 0) {
                totalPoint = (regular * 20 + midterm * 30 + finalPoint * 50) / 100;
            } else {
                if (course != null) {
                    totalPoint = ((((regular * 20 + midterm * 30 + finalPoint * 50) / 100) * course.getCourseDuration().getTheory()) + (practice * course.getCourseDuration().getPractice())) / course.getCredits();
                }
            }
        }

        return RegistrationDTO.builder()
                .id(studentSection.getId())
                .registrationStatus(studentSection.getRegistrationStatus())
                .createdAt(studentSection.getCreatedAt())

                .studentId(studentDTO != null ? studentDTO.getId() : null)
                .studentName(studentDTO != null ? studentDTO.getName() : "")
                .studentCode(studentDTO != null ? studentDTO.getCode() : "")

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

                .tuitionId(tuition != null ? tuition.getId() : null)
                .initialFee(tuition != null ? tuition.getInitialFee() : null)
                .debt(debt)
                .discountAmount(tuition != null ? tuition.getDiscountAmount() : null)
                .discountFee(tuition != null ? tuition.getDiscountFee() : null)
                .paymentDeadline(tuition != null ? tuition.getPaymentDeadline() : null)
                .total(total)
                .tuitionStatus(studentTuition != null ? studentTuition.getStatus() : null)
                .paymentDate(studentTuition != null ? studentTuition.getPaymentDate() : null)

                .resultId(result != null ? result.getId() : null)
                .regularPoint1(result != null ? result.getRegularPoint1() : null)
                .regularPoint2(result != null ? result.getRegularPoint2() : null)
                .regularPoint3(result != null ? result.getRegularPoint3() : null)
                .regularPoint4(result != null ? result.getRegularPoint4() : null)
                .regularPoint5(result != null ? result.getRegularPoint5() : null)
                .midtermPoint1(result != null ? result.getMidtermPoint1() : null)
                .midtermPoint2(result != null ? result.getMidtermPoint2() : null)
                .midtermPoint3(result != null ? result.getMidtermPoint3() : null)
                .finalPoint(result != null ? result.getFinalPoint() : null)
                .practicePoint1(result != null ? result.getPracticePoint1() : null)
                .practicePoint2(result != null ? result.getPracticePoint2() : null)
                .totalPoint(totalPoint)
                .completedStatus(studentSection.getCompletedStatus())
                .build();
    }

    public List<RegistrationDTO> mapToDTO(List<StudentSection> studentSections) {
        return studentSections.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<RegistrationDTO> mapToDTO(Page<StudentSection> studentSections) {
        return studentSections.map(this::mapToDTO);
    }
}
