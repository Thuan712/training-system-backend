package iuh.fit.trainingsystembackend.controller;

import com.google.gson.Gson;
import iuh.fit.trainingsystembackend.bean.CourseBean;
import iuh.fit.trainingsystembackend.data.RequireCourse;
import iuh.fit.trainingsystembackend.dto.CourseDTO;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.mapper.CourseMapper;
import iuh.fit.trainingsystembackend.model.Course;
import iuh.fit.trainingsystembackend.model.Specialization;
import iuh.fit.trainingsystembackend.repository.CourseRepository;
import iuh.fit.trainingsystembackend.repository.SpecializationRepository;
import iuh.fit.trainingsystembackend.request.CourseRequest;
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
    private CourseMapper courseMapper;
    private SpecializationRepository specializationRepository;
    @PostMapping("/createOrUpdate")
    public ResponseEntity<?> createOrUpdate(@RequestParam(value = "userId", required = false) Long userId, @RequestBody CourseBean data) {
        Course toSave = null;

        if (data.getId() != null) {
            toSave = courseRepository.findById(data.getId()).orElse(null);

            if (toSave == null) {
                throw new ValidationException("Không tìm thấy môn học !");
            }
        }

        if(data.getSpecializationId() == null) {
            throw new ValidationException("Mã chuyên ngành không được để trống !!");
        }

        Specialization specialization = specializationRepository.findById(data.getSpecializationId()).orElse(null);

        if(specialization == null){
             throw new ValidationException("Không tìm thấy chuyên ngành của môn học này !!");
        }

        if (toSave == null) {
            toSave = new Course();

            // Tạo mã môn học
            String code = "";
            boolean isExist = true;
            while (isExist){
                code = StringUtils.randomNumberGenerate(6);
                isExist = courseRepository.existsCourseByCode(code);
            }

            if(!code.isEmpty()){
                toSave.setCode(code);
            }
        }

        toSave.setSpecializationId(specialization.getId());

        if(data.getName() == null || data.getName().isEmpty()){
            throw new ValidationException("Tên môn học không được để trống !!");
        }

        toSave.setName(data.getName());
        toSave.setDescription(data.getDescription());

        if(data.getCourseType() == null){
            throw new ValidationException("Loại môn học không được để trống !!");
        }

        toSave.setCourseType(data.getCourseType());

        // Loại học kì có thể đăng ký
        if (data.getTermRegister() == null || data.getTermRegister().isEmpty()) {
            throw new ValidationException("Loại học kì để đăng ký môn học không được để trống !!");
        }

        toSave.setTermRegisterString(new Gson().toJson(data.getTermRegister()));

        if (data.getCredits() != null) {
            if (data.getCredits() < 0) {
                throw new ValidationException("Số tín chỉ học tập của học phần phải lớn hơn bằng 0 !!");
            } else {
                int totalCreditsDuration = data.getCourseDuration().getTheory() + data.getCourseDuration().getPractice();
                if(!data.getCredits().equals(totalCreditsDuration)) {
                    throw new ValidationException("Tổng hệ số thời lượng tiết học thực hành và lý thuyết không được vượt quá hoặc thấp hơn số tín chỉ học tập !!");
                }
            }

            toSave.setCredits(data.getCredits());
        } else {
            toSave.setCredits(0);
        }

        if (data.getCostCredits() == null || data.getCostCredits() < 1) {
            throw new ValidationException("Số tín chỉ học phí không được để trống và phải lớn hơn 0 !!");
        }

        toSave.setCostCredits(data.getCostCredits());

        if (data.getCourseDuration() == null) {
            throw new ValidationException("Thời lương tín chỉ của các công việc trong môn học không được để trống !!");
        }

        if (data.getCourseDuration().getTheory() < 0
            || data.getCourseDuration().getPractice() < 0
            || data.getCourseDuration().getSelfLearning() < 0
            || (data.getCourseDuration().getTheory() == 0
                && data.getCourseDuration().getPractice() == 0
                && data.getCourseDuration().getSelfLearning() == 0)) {
            throw new ValidationException("Tín chỉ thời lượng của tiết học phải lớn hơn hoặc bằng 0");
        }

        toSave.setCourseDurationString(new Gson().toJson(data.getCourseDuration()));
        toSave.setRequireCourseString(new Gson().toJson(data.getRequireCourse() != null ? data.getRequireCourse() : new RequireCourse()));

        if(data.getTypeOfKnowledge() == null){
            throw new ValidationException("Loại kiến thức của môn học không được trống !!");
        }

        toSave.setTypeOfKnowledge(data.getTypeOfKnowledge());

        toSave = courseRepository.saveAndFlush(toSave);

        if(toSave.getId() == null){
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CourseDTO courseDTO = courseMapper.mapToDTO(toSave);

        return ResponseEntity.ok(courseDTO);
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

        CourseDTO courseDTO = courseMapper.mapToDTO(course);
        return ResponseEntity.ok(courseDTO);
    }

    @PostMapping("/getPage")
    public ResponseEntity<?> getPage(@RequestParam(value = "userId", required = false) Long userId,
                                     @RequestParam("pageNumber") int pageNumber, @RequestParam("pageRows") int pageRows,
                                     @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                     @RequestParam(value = "sortOrder", required = false, defaultValue = "-1") int sortOrder,
                                     @RequestBody CourseRequest filterRequest) {
        Page<Course> courses = courseRepository.findAll(courseSpecification.getFilter(filterRequest), PageRequest.of(pageNumber, pageRows, Sort.by(sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "id")));
        Page<CourseDTO> courseDTOS = courseMapper.mapToDTO(courses);
        return ResponseEntity.ok(courseDTOS);
    }

    @PostMapping("/getList")
    public ResponseEntity<?> getList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody CourseRequest filterRequest) {
        List<Course> courses = courseRepository.findAll(courseSpecification.getFilter(filterRequest));
        List<CourseDTO> courseDTOS = courseMapper.mapToDTO(courses);
        return ResponseEntity.ok(courseDTOS);
    }

}
