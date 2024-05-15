package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.RegistrationDTO;
import iuh.fit.trainingsystembackend.dto.SectionClassDTO;
import iuh.fit.trainingsystembackend.dto.StudentDTO;
import iuh.fit.trainingsystembackend.dto.StudentSectionDTO;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("studentSectionMapper")
@AllArgsConstructor
public class StudentSectionMapper {

    private final StudentRepository studentRepository;
    private final SectionRepository sectionRepository;
    private StudentMapper studentMapper;
    private final ResultRepository resultRepository;
    private final CourseRepository courseRepository;
    private final StudentSectionClassRepository studentSectionClassRepository;

    public StudentSectionDTO mapToDTO(StudentSection studentSection) {
        StudentDTO studentDTO = null;
        Student student = studentRepository.findById(studentSection.getStudentId()).orElse(null);

        if (student != null) {
            studentDTO = studentMapper.mapToDTO(student);
        }

        List<StudentSectionClass> studentSectionClasses = studentSectionClassRepository.findByStudentSectionId(studentSection.getSectionId());

        List<SectionClass> sectionClass = studentSectionClasses.stream().map(StudentSectionClass::getSectionClass).collect(Collectors.toList());

        Section section = sectionRepository.findById(studentSection.getSectionId()).orElse(null);
        Course course = null;
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

        return StudentSectionDTO.builder()
                .id(studentSection.getId())

                .studentId(studentDTO != null ? studentDTO.getId() : null)
                .studentName(studentDTO != null ? studentDTO.getName() : "")
                .studentCode(studentDTO != null ? studentDTO.getCode() : "")

                .sectionId(section != null ? section.getId() : null)
                .sectionName(section != null ? section.getName() : "")
                .sectionCode(section != null ? section.getCode() : "")

                .sectionClasses(sectionClass)

                .termId(section != null ? section.getTermId() : null)
                .termName(section != null && section.getTerm() != null ? section.getTerm().getName() : "")

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
                .registrationStatus(studentSection.getRegistrationStatus())
                .completedStatus(studentSection.getCompletedStatus())
                .build();
    }

    public List<StudentSectionDTO> mapToDTO(List<StudentSection> studentSections) {
        return studentSections.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<StudentSectionDTO> mapToDTO(Page<StudentSection> studentSectionPage) {
        return studentSectionPage.map(this::mapToDTO);
    }
}