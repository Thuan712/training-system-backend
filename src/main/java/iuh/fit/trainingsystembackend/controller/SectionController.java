package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.bean.SectionBean;
import iuh.fit.trainingsystembackend.enums.SectionType;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.model.Course;
import iuh.fit.trainingsystembackend.model.Section;
import iuh.fit.trainingsystembackend.model.Term;
import iuh.fit.trainingsystembackend.repository.CourseRepository;
import iuh.fit.trainingsystembackend.repository.SectionRepository;
import iuh.fit.trainingsystembackend.repository.TermRepository;
import iuh.fit.trainingsystembackend.request.CourseRequest;
import iuh.fit.trainingsystembackend.request.SectionRequest;
import iuh.fit.trainingsystembackend.specification.SectionSpecification;
import iuh.fit.trainingsystembackend.utils.Constants;
import iuh.fit.trainingsystembackend.utils.StringUtils;
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
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.SECTION_ENDPOINT)
public class SectionController {

    private SectionRepository sectionRepository;
    private CourseRepository courseRepository;
    private TermRepository termRepository;
    private SectionSpecification sectionSpecification;

    @PostMapping("/createOrUpdate")
    public ResponseEntity<?> createOrUpdateSection(@RequestParam(value = "userId") Long userId, @RequestBody SectionBean data) {
        Section toSave = null;

        if (data.getId() != null) {
            toSave = sectionRepository.findById(data.getId()).orElse(null);

            if(toSave == null){
                throw new ValidationException("Section is not found !!");
            }
        }

        if(data.getCourseId() == null){
            throw new ValidationException("Course ID is required !!");
        }

        Course course = courseRepository.findById(data.getCourseId()).orElse(null);

        if(course == null){
            throw new ValidationException("Course is not found !!");
        }

        if(data.getTermId() == null){
            throw new ValidationException("Term ID is required !!");
        }

        Term term = termRepository.findById(data.getTermId()).orElse(null);

        if(term == null){
            throw new ValidationException("Term is not found !!");
        }

        if(toSave == null){
            toSave = new Section();
            toSave.setCourseId(course.getId());
            toSave.setTermId(term.getId());

        }

        toSave.setName(data.getName());
        boolean isExist = true;
        String code = "";

        while (isExist){
            code = StringUtils.randomNumberGenerate(12);
            Section section = sectionRepository.findSectionByCode(code);

            if(section != null){
                isExist = true;
            } else {
                isExist = false;
            }
        }

        toSave.setCode(code);
        if(data.getTheoryPeriods() == null || data.getTheoryPeriods() < 1){
            throw new ValidationException("Theory Periods should be greater than 2 !");
        }

        toSave.setTheoryPeriods(data.getTheoryPeriods());
        toSave.setPracticePeriods(data.getPracticePeriods());
        toSave.setSectionType(data.getSectionType());

        toSave = sectionRepository.saveAndFlush(toSave);
        if(toSave.getId() == null){
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(toSave);
    }

    @PostMapping("/getPage")
    public ResponseEntity<?> getPage(@RequestParam(value = "userId", required = false) Long userId,
                                     @RequestParam("pageNumber") int pageNumber, @RequestParam("pageRows") int pageRows,
                                     @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                     @RequestParam(value = "sortOrder", required = false, defaultValue = "-1") int sortOrder,
                                     @RequestBody SectionRequest filterRequest) {
        Page<Section> sections = sectionRepository.findAll(sectionSpecification.getFilter(filterRequest), PageRequest.of(pageNumber, pageRows, Sort.by(sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "id")));
        return ResponseEntity.ok(sections);
    }

    @PostMapping("/getList")
    public ResponseEntity<?> getList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody SectionRequest filterRequest) {
        List<Section> sections = sectionRepository.findAll(sectionSpecification.getFilter(filterRequest));
        return ResponseEntity.ok(sections);
    }


}
