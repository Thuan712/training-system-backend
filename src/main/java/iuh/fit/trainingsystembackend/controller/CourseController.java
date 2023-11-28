package iuh.fit.trainingsystembackend.controller;

import com.google.gson.Gson;
import iuh.fit.trainingsystembackend.bean.CourseBean;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.model.Course;
import iuh.fit.trainingsystembackend.model.UserEntity;
import iuh.fit.trainingsystembackend.repository.CourseRepository;
import iuh.fit.trainingsystembackend.request.CourseRequest;
import iuh.fit.trainingsystembackend.request.UserRequest;
import iuh.fit.trainingsystembackend.specification.CourseSpecification;
import iuh.fit.trainingsystembackend.utils.Constants;
import iuh.fit.trainingsystembackend.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.COURSE_ENDPOINT)
public class CourseController {
    private CourseRepository courseRepository;
    private CourseSpecification courseSpecification;

    @PostMapping("/createOrUpdate")
    public ResponseEntity<?> createOrUpdate(@RequestParam(value = "userId", required = false) Long userId, @RequestBody CourseBean data) {
        Course toSave = null;

        if (data.getId() != null) {
            toSave = courseRepository.findById(data.getId()).orElse(null);

            if (toSave == null) {
                throw new ValidationException("Course is not found !");
            }
        }

        if (toSave == null) {
            toSave = new Course();
            //TODO: Generate code for course
            String code = "";
            boolean isExist = true;
            while (isExist){
                code = StringUtils.randomNumberGenerate(12);
                isExist = courseRepository.existsCourseByCode(code);
            }

            if(!code.isEmpty()){
                toSave.setCode(code);
            }
        }

        toSave.setName(data.getName());

        if(data.getCredit() == null || data.getCredit() < 1){
            throw new ValidationException("Credits of course should be greater than 0!");
        }

        toSave.setCredit(data.getCredit());

        boolean isError = false;
        for(Long courseId : data.getRequireCourse()){
            Course course = courseRepository.findById(courseId).orElse(null);

            if(course == null){
                isError = true;
                break;
            }
        }

        if(isError){
            throw new ValidationException("Have require courses not found !");
        }

        toSave.setRequireCourseString(new Gson().toJson(data.getRequireCourse()));
        toSave.setCourseType(data.getCourseType());
        toSave = courseRepository.saveAndFlush(toSave);

        if(toSave.getId() == null){
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //TODO: Map to DTO

        return ResponseEntity.ok(toSave);
    }

    @DeleteMapping("/deleteById")
    public ResponseEntity<?> deleteById(@RequestParam(value = "userId", required = false) Long userId, @RequestParam(value = "id") Long id){
        Course course= courseRepository.findById(id).orElse(null);

        if(course == null){
            throw new ValidationException("Course is not found !");
        }

        course.setDeleted(true);
        course.setDeletedAt(new Date());

        course = courseRepository.saveAndFlush(course);

        if(course.getId() == null){
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //TODO: Map to DTO

        return ResponseEntity.ok(course);
    }

    @PostMapping("/getPage")
    public ResponseEntity<?> getPage(@RequestParam(value = "userId", required = false) Long userId,
                                     @RequestParam("pageNumber") int pageNumber, @RequestParam("pageRows") int pageRows,
                                     @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                     @RequestParam(value = "sortOrder", required = false, defaultValue = "-1") int sortOrder,
                                     @RequestBody CourseRequest filterRequest) {
        Page<Course> courses = courseRepository.findAll(courseSpecification.getFilter(filterRequest), PageRequest.of(pageNumber, pageRows, Sort.by(sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "id")));
        return ResponseEntity.ok(courses);
    }

        @PostMapping("/getList")
    public ResponseEntity<?> getList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody CourseRequest filterRequest) {
        List<Course> courses = courseRepository.findAll(courseSpecification.getFilter(filterRequest));
        return ResponseEntity.ok(courses);
    }

}
