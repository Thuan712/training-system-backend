package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.SectionDTO;
import iuh.fit.trainingsystembackend.model.Course;
import iuh.fit.trainingsystembackend.model.Section;
import iuh.fit.trainingsystembackend.model.Term;
import iuh.fit.trainingsystembackend.repository.CourseRepository;
import iuh.fit.trainingsystembackend.repository.TermRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("sectionMapper")
@AllArgsConstructor
public class SectionMapper {
    private CourseRepository courseRepository;
    private TermRepository termRepository;

    public SectionDTO mapToDTO(Section section) {
        Course course = null;
        Term term = null;
        if (section.getCourseId() != null) {
            course = courseRepository.findById(section.getCourseId()).orElse(null);
        }

        if (section.getTermId() != null) {
            term = termRepository.findById(section.getTermId()).orElse(null);
        }


        return SectionDTO.builder()
                .id(section.getId())
                .courseId(course != null ? course.getId() : null)
                .courseName(course != null ? course.getName() : "")
                .courseCode(course != null ? course.getCode() : "")
                .termId(term != null ? term.getId() : null)
                .termName(term != null ? term.getName() : "")
                .name(section.getName())
                .code(section.getCode())
                .theoryPeriods(section.getTheoryPeriods())
                .practicePeriods(section.getPracticePeriods())
                .sectionType(section.getSectionType().getValue() )
                .createdAt(section.getCreatedAt())
                .updatedAt(section.getUpdatedAt())
                .deletedAt(section.getDeletedAt())
                .deleted(section.getDeleted())
                .build();
    }

    public List<SectionDTO> mapToDTO(List<Section> sectionList) {
        return sectionList.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<SectionDTO> mapToDTO(Page<Section> sectionPage) {
        return sectionPage.map(this::mapToDTO);
    }
}
