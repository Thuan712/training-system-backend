package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.model.AcademicYear;
import iuh.fit.trainingsystembackend.model.Term;
import iuh.fit.trainingsystembackend.repository.AcademicYearRepository;
import iuh.fit.trainingsystembackend.repository.TermRepository;
import iuh.fit.trainingsystembackend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.TERM_ENDPOINT)
public class TermController {
    private TermRepository termRepository;
    private AcademicYearRepository academicYearRepository;

    @PostMapping("/createTerm")
    public ResponseEntity<?> createTerm(@RequestParam(value = "userId", required = false) Long userId, @RequestBody Term data) {
        if (data.getAcademicYearId() == null) {
            throw new ValidationException("Academic Year is required !");
        }

        AcademicYear academicYear = academicYearRepository.findById(data.getAcademicYearId()).orElse(null);

        if (academicYear == null) {
            throw new ValidationException("Academic Year is not found !");
        }

        String name = academicYear.getName();

        for(int i = 0; i < 3; i++){
            String hkName = "HK" + i + name;
            Term term = termRepository.findDistinctByName(hkName);

            if(term != null && term.getId() != null){
                throw new ValidationException("Term for this academic year is created !");
            } else {
                term = new Term();

                term.setName(hkName);
                term.setAcademicYearId(academicYear.getId());
                term = termRepository.saveAndFlush(term);
                if(term.getId() == null){
                    throw new ValidationException("Create Term for academic year fail !");
                }
            }
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
