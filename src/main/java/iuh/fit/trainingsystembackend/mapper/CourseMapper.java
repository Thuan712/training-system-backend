package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.CourseDTO;
import iuh.fit.trainingsystembackend.enums.TermType;
import iuh.fit.trainingsystembackend.model.Course;
import iuh.fit.trainingsystembackend.model.Specialization;
import iuh.fit.trainingsystembackend.repository.SpecializationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("courseMapper")
@AllArgsConstructor
public class CourseMapper {
    private SpecializationRepository specializationRepository;
    public CourseDTO mapToDTO(Course course) {

        String courseDurationValue = course.getCredits() + "(" + course.getCourseDuration().getTheory() + "," +
                                     course.getCourseDuration().getPractice() + "," +
                                     course.getCourseDuration().getSelfLearning() + ")" ;

        StringBuilder requireCourseValue = new StringBuilder();
        if(!course.getRequireCourse().getParallel().isEmpty()){
            course.getRequireCourse().getParallel().forEach(courseCode -> {
                requireCourseValue.append(courseCode).append("(a), ");
            });
        }

        if(!course.getRequireCourse().getStudyFirst().isEmpty()){
            course.getRequireCourse().getStudyFirst().forEach(courseCode -> {
                requireCourseValue.append(courseCode).append("(b), ");
            });
        }

        if(!course.getRequireCourse().getPrerequisite().isEmpty()){
            course.getRequireCourse().getPrerequisite().forEach(courseCode -> {
                requireCourseValue.append(courseCode).append("(c), ");
            });
        }

        return CourseDTO.builder()
                .id(course.getId())
                .specializationId(course.getSpecialization() != null ? course.getSpecialization().getId() : null)
                .specializationName(course.getSpecialization() != null ? course.getSpecialization().getName() : "")
                .specializationCode(course.getSpecialization() != null ? course.getSpecialization().getCode() : "")

                .name(course.getName())
                .code(course.getCode())
                .description(course.getDescription())

                .courseDuration(course.getCourseDuration())
                .courseDurationValue(courseDurationValue)

                .requireCourse(course.getRequireCourse())
                .requireCourseValue(requireCourseValue.toString())

                .termRegister(course.getTermRegister().stream().map(TermType::name).collect(Collectors.toList()))
                .typeOfKnowledge(course.getTypeOfKnowledge())

                .courseType(course.getCourseType())

                .credits(course.getCredits())
                .costCredits(course.getCostCredits())

                .createdAt(course.getCreatedAt())
                .deletedAt(course.getDeletedAt())
                .updatedAt(course.getUpdatedAt())
                .deleted(course.getDeleted())
                .build();
    }

    public List<CourseDTO> mapToDTO(List<Course> courses) {
        return courses.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<CourseDTO> mapToDTO(Page<Course> coursePage) {
        return coursePage.map(this::mapToDTO);
    }
}
