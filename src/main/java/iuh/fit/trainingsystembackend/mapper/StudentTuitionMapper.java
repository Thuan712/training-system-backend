package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.StudentDTO;
import iuh.fit.trainingsystembackend.dto.StudentSectionDTO;
import iuh.fit.trainingsystembackend.dto.StudentTuitionDTO;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("studentTuitionMapper")
@AllArgsConstructor
public class StudentTuitionMapper {

    private final StudentRepository studentRepository;
    private StudentMapper studentMapper;
    private final TuitionRepository tuitionRepository;
    private final StudentSectionClassRepository studentSectionClassRepository;
    private final StudentSectionRepository studentSectionRepository;
    private StudentSectionMapper studentSectionMapper;
    private final TermRepository termRepository;

    public StudentTuitionDTO mapToDTO(StudentTuition studentTuition) {
        Student student = studentRepository.findById(studentTuition.getStudentId()).orElse(null);

        StudentDTO studentDTO = null;
        if (student != null) {
            studentDTO = studentMapper.mapToDTO(student);
        }

        Tuition tuition = tuitionRepository.findById(studentTuition.getTuitionId()).orElse(null);
        Section section = null;
        Term term = null;
        StudentSectionDTO studentSectionDTO = null;
        List<StudentSectionClass> studentSectionClasses = null;
        if (tuition != null) {
            section = tuition.getSection();
            if (section != null) {
                term = termRepository.findById(section.getTermId()).orElse(null);
                studentSectionDTO = studentSectionMapper.mapToDTO(studentSectionRepository.findBySectionIdAndStudentId(section.getId(), studentTuition.getStudentId()));
                studentSectionClasses = studentSectionClassRepository.findByStudentSectionId(studentSectionDTO.getId());
            }

        }


        return StudentTuitionDTO.builder()
                .id(studentTuition.getId())

                .termId(term != null ? term.getId() : null)
                .termName(term != null ? term.getName() : "")

                .sectionId(section != null ? section.getId() : null)
                .sectionName(section != null ? section.getName() :  "")
                .sectionCode(section != null ? section.getCode() : "")

                .studentId(studentTuition.getStudentId())
                .studentName(studentDTO != null ? studentDTO.getName() : "")
                .studentCode(studentDTO != null ? studentDTO.getCode() : "")

                .tuitionFeeId(studentTuition.getTuitionId())
                .initialFee(tuition != null ? tuition.getInitialFee() : 0D)
                .discountAmount(tuition != null ? tuition.getDiscountAmount() : 0D)
                .discountFee(tuition != null ? tuition.getDiscountFee() : 0D)
                .paymentDeadline(tuition != null ? tuition.getPaymentDeadline() : null)

                .status(studentTuition.getStatus())
                .paymentDate(studentTuition.getPaymentDate())
                .build();
    }

    public List<StudentTuitionDTO> mapToDTO(List<StudentTuition> studentTuitions) {
        return studentTuitions.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<StudentTuitionDTO> mapToDTO(Page<StudentTuition> studentTuitionPage) {
        return studentTuitionPage.map(this::mapToDTO);
    }
}
