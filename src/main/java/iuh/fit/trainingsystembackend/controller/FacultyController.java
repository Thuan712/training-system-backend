package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.model.Faculty;
import iuh.fit.trainingsystembackend.repository.FacultyRepository;
import iuh.fit.trainingsystembackend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getList")
    public ResponseEntity<?> getList(){
        return ResponseEntity.ok(facultyRepository.findAll());
    }
}
