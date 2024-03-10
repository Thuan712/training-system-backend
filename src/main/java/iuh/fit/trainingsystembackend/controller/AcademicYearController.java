package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.bean.AcademicYearBean;
import iuh.fit.trainingsystembackend.dto.AcademicYearDTO;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.mapper.AcademicYearMapper;
import iuh.fit.trainingsystembackend.model.AcademicYear;
import iuh.fit.trainingsystembackend.model.Term;
import iuh.fit.trainingsystembackend.repository.AcademicYearRepository;
import iuh.fit.trainingsystembackend.repository.TermRepository;
import iuh.fit.trainingsystembackend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.ACADEMIC_YEAR_ENDPOINT)
public class AcademicYearController {
    private AcademicYearRepository academicYearRepository;
    private TermRepository termRepository;
    private AcademicYearMapper academicYearMapper;
    @PostMapping("/createOrUpdate")
    public ResponseEntity<?> createOrUpdate(@RequestParam(value = "userId", required = false) Long userId, @RequestBody AcademicYearBean data) {
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
        }


        if (data.getName() == null || data.getName().isEmpty()) {
            throw new ValidationException("Tên niên khoá không được để trống !!");
        }

        Boolean isMatched = null;
        try {
            isMatched = !data.getName().matches("^[0-9]{4}-[0-9]{4}$");
        } catch (Exception exception) {
            throw new ValidationException("Niên khoá không hợp lệ!!");

        }

        if (isMatched) {
            throw new ValidationException("Niên khoá không hợp lệ !! Phải là số và theo định dạng là \"2024-2025\"");
        }

        toSave.setName(data.getName());

        toSave = academicYearRepository.saveAndFlush(toSave);

        if (toSave.getId() == null) {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Term firstTerm;
        Term secondTerm;
        Term thirdTerm;
        if (isCreate) {
            firstTerm = new Term();
            secondTerm = new Term();
            thirdTerm = new Term();
        } else {
            firstTerm = termRepository.findById(data.getFirstTermId()).orElse(null);
            if (firstTerm == null) {
                throw new ValidationException("Học kỳ đầu của năm học này không tìm thấy !!");
            }

            secondTerm = termRepository.findById(data.getFirstTermId()).orElse(null);
            if (secondTerm == null) {
                throw new ValidationException("Học kỳ hai của năm học này không tìm thấy !!");
            }

            thirdTerm = termRepository.findById(data.getFirstTermId()).orElse(null);
            if (thirdTerm == null) {
                throw new ValidationException("Học kỳ ba của năm học này không tìm thấy !!");
            }

            toSave.setUpdatedAt(new Date());
        }

        if (data.getFirstTermName() == null || data.getFirstTermName().isEmpty()) {
            throw new ValidationException("Tên của học kỳ đầu không được để trống !!");
        }

        if (data.getFirstTermStart() == null) {
            throw new ValidationException("Thời gian bắt đầu học kỳ đầu không được để trống !!");
        }

        if (data.getFirstTermEnd() == null) {
            throw new ValidationException("Thời gian kết thúc học kỳ đầu không được để trống !!");
        }

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        firstTerm.setAcademicYearId(toSave.getId());
        firstTerm.setName(data.getFirstTermName());

        try {
            firstTerm.setTermStart(data.getFirstTermStart());
            firstTerm.setTermEnd(data.getFirstTermEnd());
        } catch (Exception exception){
            System.out.println("Parsed Date fail !!");
        }
        firstTerm = termRepository.saveAndFlush(firstTerm);

        if (firstTerm.getId() == null) {
            throw new ValidationException("Thao tác tạo học kỳ đầu không thành công !!");
        }

        if (data.getSecondTermName() == null || data.getSecondTermName().isEmpty()) {
            throw new ValidationException("Tên của học kỳ hai không được để trống !!");
        }

        if (data.getSecondTermStart() == null) {
            throw new ValidationException("Thời gian bắt đầu học kỳ hai không được để trống !!");
        }

        if (data.getSecondTermEnd() == null) {
            throw new ValidationException("Thời gian kết thúc học kỳ hai không được để trống !!");
        }

        secondTerm.setAcademicYearId(toSave.getId());
        secondTerm.setName(data.getFirstTermName());

        try {
            secondTerm.setTermStart(data.getSecondTermStart());
            secondTerm.setTermEnd(data.getSecondTermEnd());
        } catch (Exception exception){
            System.out.println("Parsed Date fail !!");
        }

        secondTerm = termRepository.saveAndFlush(secondTerm);

        if (secondTerm.getId() == null) {
            throw new ValidationException("Thao tác tạo học kỳ hai không thành công !!");
        }

        if (data.getThirdTermName() == null || data.getThirdTermName().isEmpty()) {
            throw new ValidationException("Tên của học kỳ ba không được để trống !!");
        }

        if (data.getThirdTermStart() == null) {
            throw new ValidationException("Thời gian bắt đầu học kỳ ba không được để trống !!");
        }

        if (data.getThirdTermEnd() == null) {
            throw new ValidationException("Thời gian kết thúc học kỳ ba không được để trống !!");
        }

        thirdTerm.setAcademicYearId(toSave.getId());
        thirdTerm.setName(data.getFirstTermName());
        try {
            thirdTerm.setTermStart(data.getThirdTermStart());
            thirdTerm.setTermEnd(data.getThirdTermEnd());
        } catch (Exception exception){
            System.out.println("Parsed Date fail !!");
        }


        thirdTerm = termRepository.saveAndFlush(thirdTerm);

        if (thirdTerm.getId() == null) {
            throw new ValidationException("Thao tác tạo học kỳ ba không thành công !!");
        }

        if (isCreate) {
            toSave.setFirstTermId(firstTerm.getId());
            toSave.setSecondTermId(secondTerm.getId());
            toSave.setThirdTermId(thirdTerm.getId());
            toSave = academicYearRepository.saveAndFlush(toSave);

            if (toSave.getId() == null) {
                throw new ValidationException("Thao tác cập nhật học kỳ cho niên khoá không thành công !!");
            }
        }

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
