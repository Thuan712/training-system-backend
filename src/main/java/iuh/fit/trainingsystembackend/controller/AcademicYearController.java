package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.model.AcademicYear;
import iuh.fit.trainingsystembackend.model.Course;
import iuh.fit.trainingsystembackend.model.Faculty;
import iuh.fit.trainingsystembackend.model.Term;
import iuh.fit.trainingsystembackend.repository.AcademicYearRepository;
import iuh.fit.trainingsystembackend.repository.TermRepository;
import iuh.fit.trainingsystembackend.request.CourseRequest;
import iuh.fit.trainingsystembackend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.ACADEMIC_YEAR_ENDPOINT)
public class AcademicYearController {
    private AcademicYearRepository academicYearRepository;
    private TermRepository termRepository;

    @PostMapping("/createOrUpdate")
    public ResponseEntity<?> createOrUpdateFaculty(@RequestParam(value = "userId", required = false) Long userId, @RequestBody Faculty data) {
        AcademicYear toSave = null;
        if (data.getId() != null) {
            toSave = academicYearRepository.findById(data.getId()).orElse(null);

            if (toSave == null) {
                throw new ValidationException("Academic Year is not found !");
            }
        }
        boolean isCreate = toSave == null;
        if (toSave == null) {
            toSave = new AcademicYear();
        }

        //TODO: Validate Name of Academic year

        toSave.setName(data.getName());

        toSave = academicYearRepository.saveAndFlush(toSave);

        if(toSave.getId() == null){
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(isCreate){
            String name = toSave.getName();

            for(int i = 1; i <= 3; i++){
                String hkName = "HK" + i  + "-" + name;
                Term term = termRepository.findDistinctByName(hkName);

                if(term != null && term.getId() != null){
                    throw new ValidationException("Term for this academic year is created !");
                } else {
                    term = new Term();

                    term.setName(hkName);
                    term.setAcademicYearId(toSave.getId());
                    term = termRepository.saveAndFlush(term);
                    if(term.getId() == null){
                        throw new ValidationException("Create Term for academic year fail !");
                    }
                }
            }
        }

        return ResponseEntity.ok(toSave);
    }

    @PostMapping("/getList")
    public ResponseEntity<?> getList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody AcademicYear filterRequest) {
        List<AcademicYear> academicYears = academicYearRepository.findAll();
        return ResponseEntity.ok(academicYears);
    }
    @PostMapping("/getPage")
    public ResponseEntity<?> getPage(@RequestParam(value = "userId", required = false) Long userId,
                                     @RequestParam("pageNumber") int pageNumber, @RequestParam("pageRows") int pageRows,
                                     @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                     @RequestParam(value = "sortOrder", required = false, defaultValue = "-1") int sortOrder,
                                     @RequestBody AcademicYear filterRequest) {
        Page<AcademicYear> academicYearPage = academicYearRepository.findAll(PageRequest.of(pageNumber, pageRows, Sort.by(sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "id")));
        return ResponseEntity.ok(academicYearPage);
    }

}
