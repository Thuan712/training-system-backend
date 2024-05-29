package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.data.CourseDuration;
import iuh.fit.trainingsystembackend.data.RequireCourse;
import iuh.fit.trainingsystembackend.dto.SectionDTO;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.AcademicYearRepository;
import iuh.fit.trainingsystembackend.repository.CourseRepository;
import iuh.fit.trainingsystembackend.repository.SpecializationRepository;
import iuh.fit.trainingsystembackend.repository.TermRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("sectionMapper")
@AllArgsConstructor
public class SectionMapper {
    private SpecializationRepository specializationRepository;
    private final CourseRepository courseRepository;
    private final TermRepository termRepository;
    private final AcademicYearRepository academicYearRepository;

    public SectionDTO mapToDTO(Section section) {
        Specialization specialization = null;
        Course course = courseRepository.findById(section.getCourseId()).orElse(null);
        String courseDurationValue = "";
        if(course != null){
            courseDurationValue = course.getCredits() + "(" + course.getCourseDuration().getTheory() + "," +
                                         course.getCourseDuration().getPractice() + "," +
                                         course.getCourseDuration().getSelfLearning() + ")" ;

            if(course.getSpecializationId() != null){
                specialization = specializationRepository.findById(course.getSpecializationId()).orElse(null);
            }
        }
        Term term  = termRepository.findById(section.getTermId()).orElse(null);

        String year = "";
        if(term != null){
            AcademicYear academicYear = academicYearRepository.findById(term.getAcademicYearId()).orElse(null);

            if(academicYear != null){
                int yearEnd = (academicYear.getYearStart() + 1);
                year = academicYear.getYearStart() + "-" + yearEnd;
            }
        }

        return SectionDTO.builder()
                .id(section.getId())

                .termId(term != null ? term.getId() : null)
                .termName(term != null ? term.getName() + " " + year : "")

                .specializationId(specialization != null ? specialization.getId() : null)
                .specializationName(specialization != null ? specialization.getName() : "")
                .specializationCode(specialization != null ? specialization.getCode() : "")

                .courseId(section.getCourseId())
                .courseName(course != null ? course.getName() : "")
                .courseCode(course != null ? course.getCode() : "")
                .courseDuration(course != null ? course.getCourseDuration() : new CourseDuration())
                .termRegister(course !=  null ? course.getTermRegister() : new ArrayList<>())
                .requireCourse(course != null ? course.getRequireCourse() : new RequireCourse())
                .credits(course != null ? course.getCredits() : 0)
                .costCredits(course != null ? course.getCostCredits() : 0)
                .courseType(course != null ? course.getCourseType() : null)
                .typeOfKnowledge(course != null ? course.getTypeOfKnowledge() : null)
                .courseDurationValue(courseDurationValue)

                .name(section.getName())
                .fullName(section.getName() + " - " + section.getCode())
                .code(section.getCode())
                .description(section.getDescription())

                .createdAt(section.getCreatedAt())
                .updatedAt(section.getUpdatedAt())
                .deletedAt(section.getDeletedAt())
                .lockDate(section.getLockDate())
                .openDate(section.getOpenDate())

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
