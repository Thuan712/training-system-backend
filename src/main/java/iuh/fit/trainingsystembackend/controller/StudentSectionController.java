package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.bean.RegistrationSectionBean;
import iuh.fit.trainingsystembackend.bean.SectionClassBean;
import iuh.fit.trainingsystembackend.dto.CourseDTO;
import iuh.fit.trainingsystembackend.dto.RegistrationDTO;
import iuh.fit.trainingsystembackend.enums.RegistrationStatus;
import iuh.fit.trainingsystembackend.enums.RegistrationType;
import iuh.fit.trainingsystembackend.enums.SectionClassStatus;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.mapper.RegistrationMapper;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.*;
import iuh.fit.trainingsystembackend.request.CourseRequest;
import iuh.fit.trainingsystembackend.request.RegistrationRequest;
import iuh.fit.trainingsystembackend.specification.RegistrationSpecification;
import iuh.fit.trainingsystembackend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.RegistrationBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.REGISTRATION_ENDPOINT)
public class StudentSectionController {
    private StudentSectionClassRepository studentSectionClassRepository;
    private RegistrationMapper registrationMapper;
    private RegistrationSpecification registrationSpecification;
    private StudentRepository studentRepository;
    private SectionClassRepository sectionClassRepository;
    private TermRepository termRepository;
    private TimeAndPlaceRepository timeAndPlaceRepository;
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

    @PostMapping("/createOrUpdate")
    public ResponseEntity<?> createOrUpdateRegistration(@RequestParam(value = "userId") Long userId, @RequestBody RegistrationSectionBean data) {

        //#region Student - Section Class (Registration Class)
        Student student = studentRepository.getStudentByUserId(userId);

        if (student == null) {
            throw new ValidationException("Không tìm thấy sinh viên đăng ký !!");
        }

        StudentSectionClass toSave = null;
        if(data.getSectionClassId() != null){
            toSave = studentSectionClassRepository.findByStudentIdAndSectionClassId(student.getId(), data.getSectionClassId());

            if(toSave == null){
                throw new ValidationException("không tìm thấy lớp học phần này !!");
            }
        }

        if(toSave == null){
            toSave = new StudentSectionClass();
        }

        if (data.getSectionClassId() == null) {
            throw new ValidationException("Lớp học phần của sinh viên không được để trống khi đăng ký !!");
        }

        SectionClass sectionClassTheory = sectionClassRepository.findById(data.getSectionClassId()).orElse(null);

        if (sectionClassTheory == null) {
            throw new ValidationException("Không tìm thấy lớp học phần này !!");
        }

        if (sectionClassTheory.getSectionClassStatus().equals(SectionClassStatus.closed)) {
            throw new ValidationException("Lớp học phần đã đóng đăng ký !!");
        }

        toSave.setSectionClassId(sectionClassTheory.getId());

        if(data.getTermId() == null){
            throw new ValidationException("Học kỳ không được để trống !!");
        }

        Term term = termRepository.findById(data.getTermId()).orElse(null);

        if(term == null){
            throw new ValidationException("Không tìm thây học kỳ này !!");
        }

        if(data.getTimeAndPlaceId() == null){
            throw new ValidationException("Thời gian học của lớp học không được để trống !!");
        }

        TimeAndPlace timeAndPlaceTheory = timeAndPlaceRepository.findById(data.getTimeAndPlaceId()).orElse(null);

        if(timeAndPlaceTheory == null){
            throw new ValidationException("Không tìm thấy thời gian học của lớp học phần này !!");
        }

        toSave.setTimeAndPlaceId(timeAndPlaceTheory.getId());
        toSave.setTermId(term.getId());
        toSave.setTimeAndPlaceId(data.getTimeAndPlaceId());
        toSave.setRegistrationType(data.getType());
        toSave.setStatus(RegistrationStatus.registered);
        toSave.setStudentId(student.getId());
        toSave = studentSectionClassRepository.saveAndFlush(toSave);

        if (toSave.getId() == null) {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //#region CreateOrUpdate Tuition For Student

        //#endregion

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/changeType")
    public ResponseEntity<?> changeRegistrationType(@RequestParam(value = "userId") Long userId, @RequestBody Map<String, Object> data) {

        if(data.get("id") == null){
            throw new ValidationException("Học phần đăng ký không được trống !!");
        }

        StudentSectionClass studentSectionClass = studentSectionClassRepository.findById((Long) data.get("id")).orElse(null);

        if(studentSectionClass == null){
            throw new ValidationException("Không tìm thấy học phần đăng ký này !!");
        }

        if(data.get("type") == null){
            throw new ValidationException("Trạng thái đăng ký không được để trống !!");
        }

        studentSectionClass.setRegistrationType((RegistrationType) data.get("type"));

        studentSectionClass = studentSectionClassRepository.saveAndFlush(studentSectionClass);

        if(studentSectionClass.getId() == null){
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
