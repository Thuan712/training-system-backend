package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.model.Lecturer;
import iuh.fit.trainingsystembackend.repository.LecturerRepository;
import iuh.fit.trainingsystembackend.request.LecturerRequest;
import iuh.fit.trainingsystembackend.specification.LecturerSpecification;
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
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.LECTURER_ENDPOINT)
public class LecturerController {
    private LecturerRepository lecturerRepository;
    private LecturerSpecification lecturerSpecification;

    @PostMapping("/getPage")
    public ResponseEntity<?> getPage(@RequestParam(value = "userId", required = false) Long userId,
                                     @RequestParam("pageNumber") int pageNumber, @RequestParam("pageRows") int pageRows,
                                     @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                     @RequestParam(value = "sortOrder", required = false, defaultValue = "-1") int sortOrder,
                                     @RequestBody LecturerRequest filterRequest){
        Page<Lecturer> lecturers = lecturerRepository.findAll(lecturerSpecification.getFilter(filterRequest), PageRequest.of(pageNumber, pageRows, Sort.by(sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "id")));
        return ResponseEntity.ok(lecturers);
    }

    @PostMapping("/getList")
    public ResponseEntity<?> getList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody LecturerRequest filterRequest) {
        List<Lecturer> lecturers = lecturerRepository.findAll(lecturerSpecification.getFilter(filterRequest));
        return ResponseEntity.ok(lecturers);
    }
}
