package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.bean.ProgramBean;
import iuh.fit.trainingsystembackend.bean.ProgramTermBean;
import iuh.fit.trainingsystembackend.dto.ProgramDTO;
import iuh.fit.trainingsystembackend.enums.CompletedStatus;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.mapper.ProgramMapper;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.AcademicYearRepository;
import iuh.fit.trainingsystembackend.repository.ProgramRepository;
import iuh.fit.trainingsystembackend.repository.SpecializationRepository;
import iuh.fit.trainingsystembackend.repository.StudentRepository;
import iuh.fit.trainingsystembackend.request.ProgramRequest;
import iuh.fit.trainingsystembackend.service.ProgramTermService;
import iuh.fit.trainingsystembackend.specification.ProgramSpecification;
import iuh.fit.trainingsystembackend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.PROGRAM_ENDPOINT)
public class ProgramController implements Serializable {
    private ProgramRepository programRepository;
    private final AcademicYearRepository academicYearRepository;
    private ProgramTermService programTermService;
    private final SpecializationRepository specializationRepository;
    private ProgramSpecification programSpecification;
    private ProgramMapper programMapper;
    private final StudentRepository studentRepository;
    private final ProgramCourseRepository programCourseRepository;
    private final StudentCourseRepository studentCourseRepository;

    @PostMapping("/createOrUpdate")
    public ResponseEntity<?> createOrUpdateProgram(@RequestParam(value = "userId", required = false) Long userId, @RequestBody ProgramBean data) {
        Program toSave = null;

        AcademicYear academicYear = academicYearRepository.findById(data.getAcademicYearId()).orElse(null);

        if(academicYear == null){
            throw new ValidationException("Không tìm thấy năm học của chương trình đào tạo này !!");
        }


        if (data.getSpecializationId() == null){
            throw  new ValidationException("Chuyên ngành của chương trình đào tạo không được để trống !!");
        }

        Specialization specialization = specializationRepository.findById(data.getSpecializationId()).orElse(null);

        if(specialization == null){
            throw new ValidationException("Không tìm thấy chuyên ngành này của chương trinh đào tạo !!");
        }

        if(data.getId() != null){
            toSave = programRepository.findById(data.getId()).orElse(null);

            if(toSave == null){
                throw new ValidationException("Không tìm thấy chương trình đào tạo này !!");
            }

            toSave.setUpdatedAt(new Date());
        } else {
            toSave = programRepository.findByAcademicYearIdAndSpecializationId(academicYear.getId(), specialization.getId());

            if(toSave != null){
                throw new ValidationException("Chương trình đào tạo của chuyên ngành niên khoá này đã tồn tại !!");
            }
        }

        boolean isCreate = toSave == null;
        if(toSave == null){
            toSave = new Program();
        }

        if(data.getAcademicYearId() == null){
            throw new ValidationException("Năm học của chương trình đào tạo không được để trống !!");
        }

        toSave.setAcademicYearId(academicYear.getId());
        toSave.setSpecializationId(specialization.getId());
        toSave.setName(specialization.getName());
        toSave.setTrainingTime(data.getTrainingTime() != null ? data.getTrainingTime() :  0);
        toSave = programRepository.saveAndFlush(toSave);

        if(toSave.getId() == null){
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        for(ProgramTermBean programTermBean : data.getProgramTerms()){
            ProgramTerm programTermSave = programTermService.createOrUpdateProgramTerm(toSave.getId(), programTermBean);

            if(programTermSave == null){
                return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return ResponseEntity.ok(toSave);
    }

    @PostMapping("/getPage")
    public ResponseEntity<?> getPage(@RequestParam(value = "userId", required = false) Long userId,
                                     @RequestParam("pageNumber") int pageNumber, @RequestParam("pageRows") int pageRows,
                                     @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                     @RequestParam(value = "sortOrder", required = false, defaultValue = "-1") int sortOrder,
                                     @RequestBody ProgramRequest filterRequest) {
        Page<Program> programs = programRepository.findAll(programSpecification.getFilter(filterRequest), PageRequest.of(pageNumber, pageRows, Sort.by(sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "id")));
        Page<ProgramDTO> programDTOS = programMapper.mapToDTO(programs);
        return ResponseEntity.ok(programDTOS);
    }

    @PostMapping("/getList")
    public ResponseEntity<?> getList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody ProgramRequest filterRequest) {
        List<Program> programs = programRepository.findAll(programSpecification.getFilter(filterRequest));
        List<ProgramDTO> programDTOS = programMapper.mapToDTO(programs);
        return ResponseEntity.ok(programDTOS);
    }

    @PostMapping("/getListByStudent")
    public ResponseEntity<?> getListByStudent(@RequestParam(value = "userId") Long userId, @RequestParam(value = "studentId", required = false) Long studentId){
        Student student = studentRepository.findById(studentId).orElse(null);

        if(student == null){
            throw new ValidationException("Không tìm thấy sinh viên này !!");
        }

        Specialization specialization = student.getSpecialization();

        if(specialization ==  null){
            throw new ValidationException("Không tìm thấy chuyên ngành của sinh viên này !!");
        }

        Program program = programRepository.findById(student.getProgramId()).orElse(null);

        if(program == null){
            throw new ValidationException("Không tìm thấy chương trình đào tạo của sinh viên này  !!");
        }

        ProgramDTO programDTO = programMapper.mapToDTO(program);

        List<Course> courses = studentCourseRepository.findByStudentIdAndCompletedStatus(student.getId(), CompletedStatus.completed).stream().map(StudentCourse::getCourse).collect(Collectors.toList());

        Map<String, Object> map = new HashMap<>();
        map.put("studentId", student.getId());
        map.put("program", programDTO);
        map.put("courses", !courses.isEmpty() ? courses : new ArrayList<>());

        return ResponseEntity.ok(map);
    }

    @PostMapping("/getLatestProgram")
    public ResponseEntity<?> getLatestProgram(@RequestParam(value = "userId") Long userId, @RequestParam(value = "specializationId", required = false) Long specializationId){
        Program program = programRepository.findFirstBySpecializationIdOrderByCreatedAtDesc(specializationId);

        if(program == null){
            return ResponseEntity.ok(null);
        }

        ProgramDTO programDTO = programMapper.mapToDTO(program);

        return ResponseEntity.ok(programDTO);
    }
}
