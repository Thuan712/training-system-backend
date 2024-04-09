package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.bean.FacultyBean;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.model.Faculty;
import iuh.fit.trainingsystembackend.repository.FacultyRepository;
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
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.FACULTY_ENDPOINT)
public class FacultyController {
    private FacultyRepository facultyRepository;

    @PostMapping("/createOrUpdate")
    public ResponseEntity<?> createOrUpdateFaculty(@RequestParam(value = "userId", required = false) Long userId, @RequestBody FacultyBean data) {
        Faculty toSave = null;
        if (data.getId() != null) {
            toSave = facultyRepository.findById(data.getId()).orElse(null);

            if (toSave == null) {
                throw new ValidationException("Không tìm thấy khoa!");
            }
        }
        boolean isCreate = toSave == null;

        if (toSave == null) {
            toSave = new Faculty();
        }

        if (data.getName() == null || data.getName().isEmpty()) {
            throw new ValidationException("Tên của khoa không được để trống !!");
        }

        if(isCreate){
            if (data.getCode() == null || data.getCode().isEmpty()) {
                boolean isExist = true;
                String code = "";

                while(isExist){
                    code = StringUtils.randomNumberGenerate(8);
                    isExist = facultyRepository.existsByCode(code);
                }

                toSave.setCode(code);

            } else {
                boolean isDuplicate = facultyRepository.existsByCode(data.getCode());

                if(isDuplicate){
                    throw new ValidationException("Mã khoa đã tồn tại !!");
                } else {
                    toSave.setCode(data.getCode());
                }
            }
        }

        if (data.getLogo() == null || data.getLogo().isEmpty()) {
            throw new ValidationException("Logo của khoa không được để trống !!");
        }

        if(data.getHeadName() == null || data.getHeadName().isEmpty()){
            throw new ValidationException("Họ và tên trưởng khoa không được đế trống !!");
        }

        if(data.getHeadEmail() == null || data.getHeadEmail().isEmpty()){
            throw new ValidationException("Email trưởng khoa không được đế trống !!");
        }

        toSave.setName(data.getName());
        toSave.setLogo(data.getLogo());
        toSave.setHeadName(data.getHeadName());
        toSave.setHeadPhone(data.getHeadPhone());
        toSave.setHeadEmail(data.getHeadEmail());
        toSave.setEstablishmentDate(data.getEstablishmentDate());

        toSave = facultyRepository.saveAndFlush(toSave);

        if (toSave.getId() == null) {
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
