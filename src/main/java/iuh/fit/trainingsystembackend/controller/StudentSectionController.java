package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.dto.CourseDTO;
import iuh.fit.trainingsystembackend.dto.RegistrationDTO;
import iuh.fit.trainingsystembackend.mapper.RegistrationMapper;
import iuh.fit.trainingsystembackend.model.Course;
import iuh.fit.trainingsystembackend.model.StudentSectionClass;
import iuh.fit.trainingsystembackend.repository.StudentSectionClassRepository;
import iuh.fit.trainingsystembackend.request.CourseRequest;
import iuh.fit.trainingsystembackend.request.RegistrationRequest;
import iuh.fit.trainingsystembackend.specification.RegistrationSpecification;
import iuh.fit.trainingsystembackend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.REGISTRATION_ENDPOINT)
public class StudentSectionController {
    private StudentSectionClassRepository studentSectionClassRepository;
    private RegistrationMapper registrationMapper;
    private RegistrationSpecification registrationSpecification;
    @PostMapping("/getPage")
    public ResponseEntity<?> getPage(@RequestParam(value = "userId", required = false) Long userId,
                                     @RequestParam("pageNumber") int pageNumber, @RequestParam("pageRows") int pageRows,
                                     @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                     @RequestParam(value = "sortOrder", required = false, defaultValue = "-1") int sortOrder,
                                     @RequestBody RegistrationRequest filterRequest) {
        Page<StudentSectionClass> studentSectionClasses = studentSectionClassRepository.findAll(registrationSpecification.getFilter(filterRequest), PageRequest.of(pageNumber, pageRows, Sort.by(sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "id")));
        Page<RegistrationDTO> registrationDTOS = registrationMapper.mapToDTO(studentSectionClasses);
        return ResponseEntity.ok(registrationDTOS);
    }

    @PostMapping("/getList")
    public ResponseEntity<?> getList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody RegistrationRequest filterRequest) {
        List<StudentSectionClass> studentSectionClasses = studentSectionClassRepository.findAll(registrationSpecification.getFilter(filterRequest));
        List<RegistrationDTO> registrationDTOS = registrationMapper.mapToDTO(studentSectionClasses);
        return ResponseEntity.ok(registrationDTOS);
    }
}
