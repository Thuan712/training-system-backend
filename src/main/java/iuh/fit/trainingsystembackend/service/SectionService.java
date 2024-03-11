package iuh.fit.trainingsystembackend.service;

import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.*;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectionService {
    private SectionRepository sectionRepository;
    private CourseRepository courseRepository;
    private ResultRepository resultRepository;
    private StudentRepository studentRepository;
    private SectionClassRepository sectionClassRepository;

    public boolean isEnoughCourseRequireForRegister(Long studentId, Long sectionId){

        if(sectionId == null){
            throw new ValidationException("Section ID is required !");
        }

        Section section = sectionRepository.findById(sectionId).orElse(null);

        if(section == null){
            throw new ValidationException("Section is not found !");
        }

        Course course = courseRepository.findById(section.getCourseId()).orElse(null);

        if(course == null){
            throw new ValidationException("Course is not found !");
        }

        List<Long> courseRequireIds = course.getPrerequisite();

        if(courseRequireIds.isEmpty()){
            return true;
        }

        List<Long> resultsStudent = resultRepository.findAllByStudentId(studentId).stream().map(Result::getCourseId).collect(Collectors.toList());

        return new HashSet<>(resultsStudent).containsAll(courseRequireIds);
    }
}
