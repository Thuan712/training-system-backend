package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.CourseDTO;
import iuh.fit.trainingsystembackend.dto.LecturerDTO;
import iuh.fit.trainingsystembackend.model.Course;
import iuh.fit.trainingsystembackend.model.Lecturer;
import iuh.fit.trainingsystembackend.model.UserEntity;
import iuh.fit.trainingsystembackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("courseMapper")
@AllArgsConstructor
public class CourseMapper {
    public CourseDTO mapToDTO(Course course) {

        return CourseDTO.builder()
                .id(course.getId())
                .name(course.getName())
                .code(course.getCode())
                .description(course.getDescription())
                .prerequisite(course.getPrerequisite())
                .specializationId(course.getSpecializationId())
                .courseType(course.getCourseType())
                .credit(course.getCredit())
                .createdAt(course.getCreatedAt())
                .deletedAt(course.getDeletedAt())
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
