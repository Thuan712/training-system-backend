package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.dto.SpecializationDTO;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.model.Course;
import iuh.fit.trainingsystembackend.model.Faculty;
import iuh.fit.trainingsystembackend.model.Specialization;
import iuh.fit.trainingsystembackend.repository.FacultyRepository;
import iuh.fit.trainingsystembackend.request.CourseRequest;
import iuh.fit.trainingsystembackend.request.SpecializationRequest;
import iuh.fit.trainingsystembackend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.FacesWebRequest;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.FACULTY_ENDPOINT)
public class FacultyController {
    private FacultyRepository facultyRepository;

    @PostMapping("/createOrUpdate")
    public ResponseEntity<?> createOrUpdateFaculty(@RequestParam(value = "userId", required = false) Long userId, @RequestBody Faculty data) {
        Faculty toSave = null;
        if (data.getId() != null) {
            toSave = facultyRepository.findById(data.getId()).orElse(null);

            if (toSave == null) {
                throw new ValidationException("Faculty is not found !");
            }
        }

        if (toSave == null) {
            toSave = new Faculty();
        }

        toSave.setName(data.getName());
        toSave.setLogo(data.getLogo());

        toSave = facultyRepository.saveAndFlush(toSave);

        if(toSave.getId() == null){
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(toSave);
    }

    @PostMapping("/getList")
    public ResponseEntity<?> getList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody Faculty filterRequest) {
        List<Faculty> faculties = facultyRepository.findAll();
        return ResponseEntity.ok(faculties);
    }

    @PostMapping("/getPage")
    public ResponseEntity<?> getPage(@RequestParam(value = "userId", required = false) Long userId,
                                     @RequestParam("pageNumber") int pageNumber, @RequestParam("pageRows") int pageRows,
                                     @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                     @RequestParam(value = "sortOrder", required = false, defaultValue = "-1") int sortOrder,
                                     @RequestBody Faculty filterRequest) {
        Page<Faculty> facultyPage = facultyRepository.findAll(PageRequest.of(pageNumber, pageRows, Sort.by(sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "id")));
        return ResponseEntity.ok(facultyPage);
    }
}
