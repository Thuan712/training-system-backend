package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.bean.AcademicYearBean;
import iuh.fit.trainingsystembackend.bean.SectionBean;
import iuh.fit.trainingsystembackend.dto.AcademicYearDTO;
import iuh.fit.trainingsystembackend.enums.TermType;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.mapper.AcademicYearMapper;
import iuh.fit.trainingsystembackend.model.AcademicYear;
import iuh.fit.trainingsystembackend.model.Course;
import iuh.fit.trainingsystembackend.model.Section;
import iuh.fit.trainingsystembackend.model.Term;
import iuh.fit.trainingsystembackend.repository.AcademicYearRepository;
import iuh.fit.trainingsystembackend.repository.CourseRepository;
import iuh.fit.trainingsystembackend.repository.SectionRepository;
import iuh.fit.trainingsystembackend.repository.TermRepository;
import iuh.fit.trainingsystembackend.service.SectionService;
import iuh.fit.trainingsystembackend.specification.CourseSpecification;
import iuh.fit.trainingsystembackend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.ACADEMIC_YEAR_ENDPOINT)
public class AcademicYearController {
    private AcademicYearRepository academicYearRepository;
    private TermRepository termRepository;
    private AcademicYearMapper academicYearMapper;
    private CourseRepository courseRepository;
    private CourseSpecification courseSpecification;
    private SectionRepository sectionRepository;
    private SectionService sectionService;

    @PostMapping("/createOrUpdate")
    public ResponseEntity<?> createOrUpdateAcademicYear(@RequestParam(value = "userId", required = false) Long userId, @RequestBody AcademicYearBean data) {
        //#region Create or Update Academic Year
        AcademicYear toSave = null;
        if (data.getId() != null) {
            toSave = academicYearRepository.findById(data.getId()).orElse(null);

            if (toSave == null) {
                throw new ValidationException("Niên khoá không tìm thấy !");
            }
        }

        boolean isCreate = toSave == null;
        if (toSave == null) {
            toSave = new AcademicYear();
            toSave.setActive(true);
        } else {
            toSave.setUpdatedAt(new Date());
        }

        if(data.getYearStart() == null || data.getYearStart() < 1){
            throw new ValidationException("Năm học bắt đầu không được để trống !!");
        }

        if(isCreate){
            boolean isExist = academicYearRepository.existsByYearStart(data.getYearStart());

            if(isExist){
                throw new ValidationException("Năm học có năm bắt đầu này đã tồn tại !!");
            }
        }

        toSave.setYearStart(data.getYearStart());

        toSave = academicYearRepository.saveAndFlush(toSave);

        if (toSave.getId() == null) {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //#endregion

        //#region Create Terms in Academic Year
        int yearEnd = toSave.getYearStart() + 1;
        Term firstTerm;
        Term secondTerm;
        Term thirdTerm;

        // Term Check
        if (isCreate) {
            firstTerm = new Term();
            secondTerm = new Term();
            thirdTerm = new Term();
        } else {
            // Học kì đầu Check
            if (data.getFirstTermId() == null) {
                throw new ValidationException("Học kỳ đầu của năm học này không được trống!!");
            }

            firstTerm = termRepository.findById(data.getFirstTermId()).orElse(null);
            if (firstTerm == null) {
                throw new ValidationException("Học kỳ đầu của năm học này không tìm thấy !!");
            }

            // Học kì hai Check
            if (data.getSecondTermId() == null) {
                throw new ValidationException("Học kỳ ha của năm học này không được trống!!");
            }

            secondTerm = termRepository.findById(data.getSecondTermId()).orElse(null);
            if (secondTerm == null) {
                throw new ValidationException("Học kỳ hai của năm học này không tìm thấy !!");
            }

            // Học kì ba Check
            if (data.getThirdTermId() == null) {
                throw new ValidationException("Học kỳ ba của năm học này không được trống!!");
            }

            thirdTerm = termRepository.findById(data.getThirdTermId()).orElse(null);
            if (thirdTerm == null) {
                throw new ValidationException("Học kỳ ba của năm học này không tìm thấy !!");
            }
        }

        //#region Create or Update First Term
        if (data.getFirstTermName() == null || data.getFirstTermName().isEmpty()) {
            throw new ValidationException("Tên của học kỳ đầu không được để trống !!");
        }

        if (data.getCostFirstTerm() == null || data.getCostFirstTerm() <= 0) {
            throw new ValidationException("Chi phí tín chỉ của học kỳ đầu không được để trống và lớn hơn 0 !!");
        }

        if (data.getFirstTermStart() == null) {
            throw new ValidationException("Thời gian bắt đầu học kỳ đầu không được để trống !!");
        }

        if (data.getFirstTermEnd() == null) {
            throw new ValidationException("Thời gian kết thúc học kỳ đầu không được để trống !!");
        }

        firstTerm.setAcademicYearId(toSave.getId());
        firstTerm.setName(data.getFirstTermName());
        firstTerm.setTermType(TermType.first_term);
        firstTerm.setCostPerCredit(data.getCostFirstTerm());
        firstTerm.setTermStart(data.getFirstTermStart());
        firstTerm.setTermEnd(data.getFirstTermEnd());

        firstTerm = termRepository.saveAndFlush(firstTerm);

        if (firstTerm.getId() == null) {
            throw new ValidationException("Thao tác tạo học kỳ đầu không thành công !!");
        }

        //#endregion


        List<Course> coursesFirstTerm = courseRepository.findAll().stream().filter(course -> course.getTermRegister().contains(TermType.first_term)).collect(Collectors.toList());

        if (!coursesFirstTerm.isEmpty()) {
            for (Course course : coursesFirstTerm) {
                // Section
                SectionBean sectionBean = new SectionBean();
                sectionBean.setCourseId(course.getId());
                sectionBean.setTermId(firstTerm.getId());
                sectionBean.setName(course.getName());
                sectionBean.setDescription(course.getDescription());
                sectionBean.setDeleted(false);

                Section section = sectionService.createOrUpdateSection(sectionBean);

                if (section == null) {
                    System.out.println("Tạo học phần " + sectionBean.getName() + " " + firstTerm.getName() + " không thành công !!");
                }
            }
        }


        //#region Create or Update Second Term
        if (data.getSecondTermName() == null || data.getSecondTermName().isEmpty()) {
            throw new ValidationException("Tên của học kỳ hai không được để trống !!");
        }

        if (data.getCostSecondTerm() == null || data.getCostSecondTerm() <= 0) {
            throw new ValidationException("Chi phí tín chỉ của học kỳ hai không được để trống và lớn hơn 0 !!");
        }

        if (data.getSecondTermStart() == null) {
            throw new ValidationException("Thời gian bắt đầu học kỳ hai không được để trống !!");
        }

        if (data.getSecondTermEnd() == null) {
            throw new ValidationException("Thời gian kết thúc học kỳ hai không được để trống !!");
        }

        secondTerm.setAcademicYearId(toSave.getId());
        secondTerm.setName(data.getSecondTermName());
        secondTerm.setTermType(TermType.second_term);
        secondTerm.setCostPerCredit(data.getCostSecondTerm());
        secondTerm.setTermStart(data.getSecondTermStart());
        secondTerm.setTermEnd(data.getSecondTermEnd());


        secondTerm = termRepository.saveAndFlush(secondTerm);

        if (secondTerm.getId() == null) {
            throw new ValidationException("Thao tác tạo học kỳ hai không thành công !!");
        }


        List<Course> coursesSecondTerm = courseRepository.findAll().stream().filter(course -> course.getTermRegister().contains(TermType.second_term)).collect(Collectors.toList());

        if (!coursesSecondTerm.isEmpty()) {
            for (Course course : coursesSecondTerm) {
                // Section
                SectionBean sectionBean = new SectionBean();
                sectionBean.setCourseId(course.getId());
                sectionBean.setTermId(secondTerm.getId());
                sectionBean.setName(course.getName());
                sectionBean.setDescription(course.getDescription());
                sectionBean.setDeleted(false);

                Section section = sectionService.createOrUpdateSection(sectionBean);

                if (section == null) {
                    System.out.println("Tạo học phần " + sectionBean.getName() + " " + secondTerm.getName() + " không thành công !!");
                }
            }
        }

        //#endregion

        //#region Create or Update Third Term
        if (data.getThirdTermName() == null || data.getThirdTermName().isEmpty()) {
            throw new ValidationException("Tên của học kỳ ba không được để trống !!");
        }

        if (data.getCostThirdTerm() == null || data.getCostThirdTerm() <= 0) {
            throw new ValidationException("Chi phí tín chỉ của học kỳ ba không được để trống và lớn hơn 0 !!");
        }

        if (data.getThirdTermStart() == null) {
            throw new ValidationException("Thời gian bắt đầu học kỳ ba không được để trống !!");
        }

        if (data.getThirdTermEnd() == null) {
            throw new ValidationException("Thời gian kết thúc học kỳ ba không được để trống !!");
        }

        thirdTerm.setAcademicYearId(toSave.getId());
        thirdTerm.setName(data.getThirdTermName());
        thirdTerm.setTermType(TermType.summer_term);
        thirdTerm.setCostPerCredit(data.getCostThirdTerm());
        thirdTerm.setTermStart(data.getThirdTermStart());
        thirdTerm.setTermEnd(data.getThirdTermEnd());

        thirdTerm = termRepository.saveAndFlush(thirdTerm);

        if (thirdTerm.getId() == null) {
            throw new ValidationException("Thao tác tạo học kỳ ba không thành công !!");
        }

        List<Course> coursesThirdTerm = courseRepository.findAll().stream().filter(course -> course.getTermRegister().contains(TermType.summer_term)).collect(Collectors.toList());
        if (!coursesThirdTerm.isEmpty()) {
            for (Course course : coursesThirdTerm) {
                // Section
                SectionBean sectionBean = new SectionBean();
                sectionBean.setCourseId(course.getId());
                sectionBean.setTermId(thirdTerm.getId());
                sectionBean.setName(course.getName());
                sectionBean.setDescription(course.getDescription());
                sectionBean.setDeleted(false);

                Section section = sectionService.createOrUpdateSection(sectionBean);

                if (section == null) {
                    System.out.println("Tạo học phần " + sectionBean.getName() + " " + thirdTerm.getName() + " không thành công !!");
                }
            }
        }

        //#endregion

        //#endregion

        return ResponseEntity.ok(toSave);
    }

    @PostMapping("/getList")
    public ResponseEntity<?> getList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody AcademicYear filterRequest) {
        List<AcademicYearDTO> academicYearDTOSs = academicYearMapper.mapToDTO(academicYearRepository.findAll());
        return ResponseEntity.ok(academicYearDTOSs);
    }

    @PostMapping("/getPage")
    public ResponseEntity<?> getPage(@RequestParam(value = "userId", required = false) Long userId,
                                     @RequestParam("pageNumber") int pageNumber, @RequestParam("pageRows") int pageRows,
                                     @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                     @RequestParam(value = "sortOrder", required = false, defaultValue = "-1") int sortOrder,
                                     @RequestBody AcademicYear filterRequest) {
        Page<AcademicYearDTO> academicYearDTOS = academicYearMapper.mapToDTO(academicYearRepository.findAll(PageRequest.of(pageNumber, pageRows, Sort.by(sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "id"))));
        return ResponseEntity.ok(academicYearDTOS);
    }

    @DeleteMapping("/deleteById")
    public ResponseEntity<?> deleteById(@RequestParam(value = "userId", required = false) Long userId,
                                        @RequestParam(value = "id") Long id) {

        try {
            academicYearRepository.deleteById(id);
        } catch (Exception exception) {
            throw new ValidationException("Academic Year is not found !!");
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

}
