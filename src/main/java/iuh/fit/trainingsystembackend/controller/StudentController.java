package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.bean.UserBean;
import iuh.fit.trainingsystembackend.dto.StudentDTO;
import iuh.fit.trainingsystembackend.enums.SystemRole;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.mapper.StudentMapper;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.StudentRepository;
import iuh.fit.trainingsystembackend.request.DebtRequest;
import iuh.fit.trainingsystembackend.request.StudentRequest;
import iuh.fit.trainingsystembackend.request.UserRequest;
import iuh.fit.trainingsystembackend.service.DebtService;
import iuh.fit.trainingsystembackend.specification.StudentSpecification;
import iuh.fit.trainingsystembackend.utils.Constants;
import iuh.fit.trainingsystembackend.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.STUDENT_ENDPOINT)
public class StudentController {
    private StudentRepository studentRepository;
    private StudentSpecification studentSpecification;
    private StudentMapper studentMapper;
    private DebtService debtService;
    @PostMapping("/getPage")
    public ResponseEntity<?> getPage(@RequestParam(value = "userId", required = false) Long userId,
                                     @RequestParam("pageNumber") int pageNumber, @RequestParam("pageRows") int pageRows,
                                     @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                     @RequestParam(value = "sortOrder", required = false, defaultValue = "-1") int sortOrder,
                                     @RequestBody StudentRequest filterRequest){
        Page<Student> students = studentRepository.findAll(studentSpecification.getFilter(filterRequest), PageRequest.of(pageNumber, pageRows, Sort.by(sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "id")));
        Page<StudentDTO> page = studentMapper.mapToDTO(students);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/getList")
    public ResponseEntity<?> getList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody StudentRequest filterRequest) {
        List<Student> students = studentRepository.findAll(studentSpecification.getFilter(filterRequest));
        List<StudentDTO> studentDTOS = studentMapper.mapToDTO(students);
        return ResponseEntity.ok(studentDTOS);
    }
    @GetMapping("/getListSchoolYear")
    public ResponseEntity<?> getListSchoolYear(@RequestParam(value = "userId", required = false) Long userId) {
        Set<String> schoolYears = studentRepository.findAll().stream().map(Student::getSchoolYear).collect(Collectors.toSet());
        return ResponseEntity.ok(schoolYears);
    }
}
