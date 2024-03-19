package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.CourseDTO;
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
        Specialization specialization = null;


        return CourseDTO.builder()
                .id(course.getId())
                .name(course.getName())
                .code(course.getCode())
                .description(course.getDescription())
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
