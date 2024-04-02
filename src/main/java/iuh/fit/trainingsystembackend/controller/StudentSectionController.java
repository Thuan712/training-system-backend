package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.bean.RegistrationSectionBean;
import iuh.fit.trainingsystembackend.bean.SectionClassBean;
import iuh.fit.trainingsystembackend.dto.CourseDTO;
import iuh.fit.trainingsystembackend.dto.RegistrationDTO;
import iuh.fit.trainingsystembackend.enums.RegistrationStatus;
import iuh.fit.trainingsystembackend.enums.RegistrationType;
import iuh.fit.trainingsystembackend.enums.SectionClassStatus;
import iuh.fit.trainingsystembackend.enums.TuitionStatus;
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
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private TuitionRepository tuitionRepository;
    private TimeAndPlaceRepository timeAndPlaceRepository;
    private SectionRepository sectionRepository;
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

        if (data.getSectionId() == null) {
            throw new ValidationException("Mã học phần không được để trống !!");
        }

        Section section = sectionRepository.findById(data.getSectionId()).orElse(null);

        if (section == null) {
            throw new ValidationException("Không tìm thấy học phần của lớp học phần này !!");
        }

        Student student = studentRepository.getStudentByUserId(userId);

        if (student == null) {
            throw new ValidationException("Không tìm thấy sinh viên đăng ký !!");
        }

        StudentSectionClass toSave = null;
        if (data.getSectionClassId() != null) {
            toSave = studentSectionClassRepository.findByStudentIdAndSectionClassId(student.getId(), data.getSectionClassId());

            if (toSave == null) {
                throw new ValidationException("không tìm thấy lớp học phần này !!");
            }
        }

        boolean isCreate = false;
        if (toSave == null) {
            toSave = new StudentSectionClass();
            isCreate = true;
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

        if (data.getTermId() == null) {
            throw new ValidationException("Học kỳ không được để trống !!");
        }

        Term term = termRepository.findById(data.getTermId()).orElse(null);

        if (term == null) {
            throw new ValidationException("Không tìm thây học kỳ này !!");
        }

        if (data.getTimeAndPlaceId() == null) {
            throw new ValidationException("Thời gian học của lớp học không được để trống !!");
        }

        TimeAndPlace timeAndPlaceTheory = timeAndPlaceRepository.findById(data.getTimeAndPlaceId()).orElse(null);

        if (timeAndPlaceTheory == null) {
            throw new ValidationException("Không tìm thấy thời gian học của lớp học phần này !!");
        }

        toSave.setTimeAndPlaceId(timeAndPlaceTheory.getId());
        toSave.setTermId(term.getId());
        toSave.setTimeAndPlaceId(data.getTimeAndPlaceId());
        toSave.setRegistrationType(data.getType());
        toSave.setStatus(RegistrationStatus.registered);
        toSave.setStudentId(student.getId());



        //#region CreateOrUpdate Tuition For Student
        Tuition tuition = null;
        if (!isCreate) {
            tuition = tuitionRepository.findById(toSave.getTuitionId()).orElse(null);

            if (tuition == null) {
                throw new ValidationException("Không tìm thấy thông tin học phí mà lớp học phần mà sinh viên này đã đăng ký !!");
            }

        } else {
            tuition = new Tuition();
        }

        Double fee = term.getCostPerCredit() * section.getCostCredits();
        tuition.setInitialFee(fee);

        // Later
        tuition.setDiscountAmount(0D);
        tuition.setDiscountFee(0D);
        tuition.setPlusDeductions(0D);
        tuition.setMinusDeductions(0D);
        tuition.setStatus(TuitionStatus.unpaid);
        tuition.setInvestigateStatus(false);

        tuition.setInitialFee(term.getCostPerCredit() * section.getCostCredits());

        tuition = tuitionRepository.saveAndFlush(tuition);

        if (tuition.getId() == null) {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        toSave.setTuitionId(tuition.getId());
        toSave = studentSectionClassRepository.saveAndFlush(toSave);

        if (toSave.getId() == null) {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //#endregion

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeRegistrationStatus(@RequestParam(value = "userId") Long userId, @RequestBody Map<String, Object> data) {

        if(data.get("id") == null){
            throw new ValidationException("Học phần đăng ký không được trống !!");
        }

        StudentSectionClass studentSectionClass = studentSectionClassRepository.findById(Long.parseLong(String.valueOf(data.get("id")))).orElse(null);

        if(studentSectionClass == null){
            throw new ValidationException("Không tìm thấy học phần đăng ký này !!");
        }

        if(data.get("status") == null){
            throw new ValidationException("Trạng thái đăng ký không được để trống !!");
        }

        RegistrationStatus registrationStatus = RegistrationStatus.valueOf((String) data.get("status"));

        if(registrationStatus.equals(RegistrationStatus.canceled)){
            List<SectionClass> sectionClassPractices = sectionClassRepository.findByRefId(studentSectionClass.getSectionClass().getId());

            if(!sectionClassPractices.isEmpty()){
                for(Long sectionClassId : sectionClassPractices.stream().map(SectionClass::getId).collect(Collectors.toList())){
                    StudentSectionClass studentSectionClassPractice = studentSectionClassRepository.findByStudentIdAndSectionClassId(studentSectionClass.getStudentId(), sectionClassId);

                    if(studentSectionClassPractice != null){
                        studentSectionClassPractice.setStatus(registrationStatus);
                        studentSectionClassPractice = studentSectionClassRepository.saveAndFlush(studentSectionClassPractice);

                        if(studentSectionClassPractice.getId() == null){
                            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                    }
                }
            }
        }

        studentSectionClass.setStatus(registrationStatus);

        studentSectionClass = studentSectionClassRepository.saveAndFlush(studentSectionClass);

        if(studentSectionClass.getId() == null){
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
