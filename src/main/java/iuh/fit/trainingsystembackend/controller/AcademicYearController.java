package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.model.AcademicYear;
import iuh.fit.trainingsystembackend.model.Faculty;
import iuh.fit.trainingsystembackend.repository.AcademicYearRepository;
import iuh.fit.trainingsystembackend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.ACADEMIC_YEAR_ENDPOINT)
public class AcademicYearController {
    private AcademicYearRepository academicYearRepository;

    @PostMapping("/createOrUpdate")
    public ResponseEntity<?> createOrUpdateFaculty(@RequestParam(value = "userId", required = false) Long userId, @RequestBody Faculty data) {
        AcademicYear toSave = null;
        if (data.getId() != null) {
            toSave = academicYearRepository.findById(data.getId()).orElse(null);

            if (toSave == null) {
                throw new ValidationException("Academic Year is not found !");
            }
        }

        if (toSave == null) {
            toSave = new AcademicYear();
        }

        //TODO: Validate Name of Academic year

        toSave.setName(data.getName());

        toSave = academicYearRepository.saveAndFlush(toSave);

        if(toSave.getId() == null){
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(toSave);
    }

    @GetMapping("/getList")
    public ResponseEntity<?> getList(){
        return ResponseEntity.ok(academicYearRepository.findAll());
    }
}
