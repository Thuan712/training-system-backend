package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.dto.SectionDTO;
import iuh.fit.trainingsystembackend.dto.StudentTuitionDTO;
import iuh.fit.trainingsystembackend.dto.UserInfoDTO;
import iuh.fit.trainingsystembackend.enums.TuitionStatus;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.mapper.StudentTuitionMapper;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.StudentTuitionRepository;
import iuh.fit.trainingsystembackend.request.SectionRequest;
import iuh.fit.trainingsystembackend.request.StudentTuitionRequest;
import iuh.fit.trainingsystembackend.specification.StudentTuitionSpecification;
import iuh.fit.trainingsystembackend.utils.Constants;
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
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.STUDENT_TUITION_ENDPOINT)
public class StudentTuitionController {
    private StudentTuitionRepository studentTuitionRepository;
    private StudentTuitionMapper studentTuitionMapper;
    private StudentTuitionSpecification studentTuitionSpecification;
    @PostMapping("/getPage")
    public ResponseEntity<?> getPage(@RequestParam(value = "userId", required = false) Long userId,
                                     @RequestParam("pageNumber") int pageNumber, @RequestParam("pageRows") int pageRows,
                                     @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                     @RequestParam(value = "sortOrder", required = false, defaultValue = "-1") int sortOrder, @RequestBody StudentTuitionRequest filterRequest) {

        Page<StudentTuitionDTO> studentTuitionDTOS = studentTuitionMapper.mapToDTO(studentTuitionRepository.findAll(studentTuitionSpecification.getFilter(filterRequest), PageRequest.of(pageNumber, pageRows, Sort.by(sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "id"))));
        return ResponseEntity.ok(studentTuitionDTOS);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(@RequestParam(value = "userId", required = false) Long userId, @RequestBody StudentTuition data) {
        if(data.getId() == null){
            throw new ValidationException("Mã công nợ của sinh viên không được để trống !!");
        }

        StudentTuition studentTuition = studentTuitionRepository.findById(data.getId()).orElse(null);

        if(studentTuition == null){
            throw new ValidationException("Không tìm thấy công nợ của sinh viên !!");
        }

        if(data.getStatus() == null){
            throw new ValidationException("Trạng thái công nợ của sinh viên không được để trống !!");
        }

        if(data.getStatus().equals(TuitionStatus.paid)){
            studentTuition.setPaymentDate(new Date());
            studentTuition.setInvestigateStatus(false);
        } else {
            studentTuition.setPaymentDate(null);
            studentTuition.setInvestigateStatus(true);
        }

        studentTuition.setStatus(data.getStatus());

        studentTuition = studentTuitionRepository.saveAndFlush(studentTuition);
        if(studentTuition.getId() == null){
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
