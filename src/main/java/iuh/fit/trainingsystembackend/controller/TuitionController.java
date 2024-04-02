package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.dto.RegistrationDTO;
import iuh.fit.trainingsystembackend.model.StudentSectionClass;
import iuh.fit.trainingsystembackend.model.Tuition;
import iuh.fit.trainingsystembackend.repository.TuitionRepository;
import iuh.fit.trainingsystembackend.request.RegistrationRequest;
import iuh.fit.trainingsystembackend.request.TuitionRequest;
import iuh.fit.trainingsystembackend.specification.TuitionSpecification;
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
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.TUITION_ENDPOINT)
public class TuitionController {
    private TuitionRepository tuitionRepository;
    private TuitionSpecification tuitionSpecification;
    @PostMapping("/getPage")
    public ResponseEntity<?> getPage(@RequestParam(value = "userId", required = false) Long userId,
                                     @RequestParam("pageNumber") int pageNumber, @RequestParam("pageRows") int pageRows,
                                     @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                     @RequestParam(value = "sortOrder", required = false, defaultValue = "-1") int sortOrder,
                                     @RequestBody TuitionRequest filterRequest) {
        Page<Tuition> tuitionPage = tuitionRepository.findAll(tuitionSpecification.getFilter(filterRequest), PageRequest.of(pageNumber, pageRows, Sort.by(sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "id")));
        return ResponseEntity.ok(tuitionPage);
    }

    @PostMapping("/getList")
    public ResponseEntity<?> getList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody TuitionRequest filterRequest) {
        List<Tuition> tuitionList = tuitionRepository.findAll(tuitionSpecification.getFilter(filterRequest));
        return ResponseEntity.ok(tuitionList);
    }

}
