package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.dto.SpecializationClassDTO;
import iuh.fit.trainingsystembackend.dto.SpecializationDTO;
import iuh.fit.trainingsystembackend.enums.TypeOfEducation;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.mapper.SpecializationClassMapper;
import iuh.fit.trainingsystembackend.mapper.SpecializationMapper;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.*;
import iuh.fit.trainingsystembackend.request.SpecializationClassRequest;
import iuh.fit.trainingsystembackend.request.SpecializationRequest;
import iuh.fit.trainingsystembackend.request.StudentRequest;
import iuh.fit.trainingsystembackend.specification.SpecializationClassSpecification;
import iuh.fit.trainingsystembackend.specification.SpecializationSpecification;
import iuh.fit.trainingsystembackend.specification.StudentSpecification;
import iuh.fit.trainingsystembackend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.SPECIALIZATION_ENDPOINT)
public class SpecializationController {
    private SpecializationRepository specializationRepository;
    private SpecializationSpecification specializationSpecification;
    private FacultyRepository facultyRepository;
    private StudentRepository studentRepository;
    private AcademicYearRepository academicYearRepository;
    private SpecializationClassRepository specializationClassRepository;
    private StudentSpecification studentSpecification;
    private SpecializationClassSpecification specializationClassSpecification;
    private SpecializationMapper specializationMapper;
    private SpecializationClassMapper specializationClassMapper;
    @PostMapping("/createOrUpdate")
    public ResponseEntity<?> createOrUpdateSpecialization(@RequestParam(name = "userId", required = false) Long userId, @RequestBody Specialization data) {
        Specialization toSave = null;
        if (data.getId() != null) {
            toSave = specializationRepository.findById(data.getId()).orElse(null);

            if (toSave == null) {
                throw new ValidationException("Specialization is not found !");
            }
        }

        if (toSave == null) {
            toSave = new Specialization();
        }

        toSave.setName(data.getName());
        toSave.setCode(data.getCode());
        if (data.getFacultyId() == null) {
            throw new ValidationException("Faculty ID is required !");
        }

        Faculty faculty = facultyRepository.findById(data.getFacultyId()).orElse(null);

        if (faculty == null) {
            throw new ValidationException("Faculty is not found !");
        }

        toSave.setFacultyId(faculty.getId());

        toSave = specializationRepository.saveAndFlush(toSave);

        if (toSave.getId() == null) {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(toSave);
    }

    @PostMapping("/getPage")
    public ResponseEntity<?> getPage(@RequestParam(value = "userId", required = false) Long userId,
                                     @RequestParam("pageNumber") int pageNumber, @RequestParam("pageRows") int pageRows,
                                     @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                     @RequestParam(value = "sortOrder", required = false, defaultValue = "-1") int sortOrder,
                                     @RequestBody SpecializationRequest filterRequest) {
        Page<Specialization> specializations = specializationRepository.findAll(specializationSpecification.getFilter(filterRequest), PageRequest.of(pageNumber, pageRows, Sort.by(sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "id")));
        Page<SpecializationDTO> specializationDTOS = specializationMapper.mapToDTO(specializations);
        return ResponseEntity.ok(specializationDTOS);
    }

    @PostMapping("/getList")
    public ResponseEntity<?> getList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody SpecializationRequest filterRequest) {
        List<Specialization> specializations = specializationRepository.findAll(specializationSpecification.getFilter(filterRequest));
        List<SpecializationDTO> specializationDTOS = specializationMapper.mapToDTO(specializations);
        return ResponseEntity.ok(specializationDTOS);
    }

    @PostMapping("/class/createOrUpdate")
    public ResponseEntity<?> createOrUpdateSpecializationClass(@RequestBody SpecializationClass data) {
        SpecializationClass toSave = null;
        if (data.getId() != null) {
            toSave = specializationClassRepository.findById(data.getId()).orElse(null);

            if (toSave == null) {
                throw new ValidationException("Specialization Class is not found !");
            }
        }

        if (toSave == null) {
            toSave = new SpecializationClass();
        }

        if (data.getSpecializationId() == null) {
            throw new ValidationException("Specialization ID is required ");
        }

        Specialization specialization = specializationRepository.findById(data.getSpecializationId()).orElse(null);

        if (specialization == null) {
            throw new ValidationException("Specialization is not found !");
        }

        toSave.setSchoolYear(data.getSchoolYear());
        toSave.setSpecializationId(specialization.getId());
        toSave.setName(data.getName());

        toSave = specializationClassRepository.saveAndFlush(toSave);

        if (toSave.getId() == null) {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(toSave);
    }

    @PostMapping("/class/getPage")
    public ResponseEntity<?> getPage(@RequestParam(value = "userId", required = false) Long userId,
                                     @RequestParam("pageNumber") int pageNumber, @RequestParam("pageRows") int pageRows,
                                     @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                     @RequestParam(value = "sortOrder", required = false, defaultValue = "-1") int sortOrder,
                                     @RequestBody SpecializationClassRequest filterRequest) {
        Page<SpecializationClass> specializationClasses = specializationClassRepository.findAll(specializationClassSpecification.getFilter(filterRequest), PageRequest.of(pageNumber, pageRows, Sort.by(sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "id")));
        Page<SpecializationClassDTO> page = specializationClassMapper.mapToDTO(specializationClasses);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/class/getList")
    public ResponseEntity<?> getList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody SpecializationClassRequest filterRequest) {
        List<SpecializationClass> specializationClasses = specializationClassRepository.findAll(specializationClassSpecification.getFilter(filterRequest));
        List<SpecializationClassDTO> specializationClassDTOS = specializationClassMapper.mapToDTO(specializationClasses);

        return ResponseEntity.ok(specializationClassDTOS);
    }

    // Create Class and Separate Student in
    @GetMapping("/class/separateStudents")
    public ResponseEntity<?> separateStudentsByClass(@RequestParam(name = "schoolYear") String schoolYear) {

        try {
            ExecutorService executor = Executors.newFixedThreadPool(5);
            FutureTask<String> futureTasks = new FutureTask<>(new Runnable() {
                @Override
                public void run() {

                    List<Specialization> specializations = specializationRepository.findAll();

                    for (Specialization specialization : specializations) {
                        SpecializationClassRequest specializationClassRequest = new SpecializationClassRequest();
                        specializationClassRequest.setSpecializationId(specialization.getId());
                        specializationClassRequest.setSchoolYear(schoolYear);
                        // Số lớp của chuyên ngành đó trong niên khoá
                        int countSpecializationClasses = specializationClassRepository.findAll(specializationClassSpecification.getFilter(specializationClassRequest)).size();

                        //#region Separate For University (General)
                        StudentRequest studentGeneralRequest = new StudentRequest();
                        studentGeneralRequest.setSpecializationId(specialization.getId());
                        studentGeneralRequest.setTypeOfEducation(TypeOfEducation.general_program);
                        studentGeneralRequest.setSchoolYear(schoolYear);
                        // Tìm sinh viên thuộc chuyên ngành chưa có lớp và thuộc niên khoá (Hệ đại trà)
                        List<Student> studentGeneralList = studentRepository.findAll(studentSpecification.getFilter(studentGeneralRequest)).stream().filter(student -> student.getSpecializationClassId() == null).collect(Collectors.toList());

                        if (!studentGeneralList.isEmpty()) {
                            // Nếu số lượng sinh viên chưa có lớp > 40
                            int flag = studentGeneralList.size();
                            while (flag > 0) {
                                int temp = 0;
                                if (flag >= 40) {
                                    for (int i = 0; i < (flag / 40); i++) {
                                        SpecializationClass specializationClass = new SpecializationClass();
                                        temp += countSpecializationClasses < 1 ? 0 : countSpecializationClasses;
                                        char key = (char) (temp + 66);
                                        String endYear = schoolYear;
                                        String nameGenerate = "DH" + specialization.getCode() + endYear + key;

                                        specializationClass.setName(nameGenerate);
                                        specializationClass.setSpecializationId(specialization.getId());

                                        specializationClass = specializationClassRepository.saveAndFlush(specializationClass);

                                        if (specializationClass.getId() == null) {
                                            throw new ValidationException("Create Specialization Class fail !");
                                        }

                                        int endList;
                                        if (flag >= 40) {
                                            endList = (i + 1) * 40;
                                        } else {
                                            endList = studentGeneralList.size();
                                        }

                                        for (int j = i * 40; j < endList; j++) {
                                            Student student = studentGeneralList.get(j);
                                            student.setSpecializationClassId(specializationClass.getId());

                                            student = studentRepository.saveAndFlush(student);

                                            if (student.getId() == null) {
                                                throw new ValidationException("Update Specialization Class for Student fail !!");
                                            }
                                        }

                                        flag -= 40;
                                        temp++;
                                    }
                                } else if (flag >= 20) {
                                    SpecializationClass specializationClass = new SpecializationClass();
                                    temp += countSpecializationClasses < 1 ? 0 : countSpecializationClasses;
                                    char key = (char) (temp + 66);
                                    String endYear = schoolYear;
                                    String nameGenerate = "DH" + specialization.getCode() + endYear + key;

                                    specializationClass.setName(nameGenerate);
                                    specializationClass.setSpecializationId(specialization.getId());

                                    specializationClass = specializationClassRepository.saveAndFlush(specializationClass);

                                    if (specializationClass.getId() == null) {
                                        throw new ValidationException("Create Specialization Class fail !");
                                    }

                                    for (int j = (studentGeneralList.size() - flag); j < studentGeneralList.size(); j++) {
                                        Student student = studentGeneralList.get(j);
                                        student.setSpecializationClassId(specializationClass.getId());

                                        student = studentRepository.saveAndFlush(student);

                                        if (student.getId() == null) {
                                            throw new ValidationException("Update Specialization Class for Student fail !!");
                                        }
                                    }

                                    flag = 0;
                                } else {
                                    // Tìm lớp cuối cùng để add hết sv vào
                                    SpecializationClass specializationClass = specializationClassRepository.findFirstByOrderBySpecializationIdDesc();
                                    if(specializationClass == null){
                                        specializationClass = new SpecializationClass();
                                        temp += countSpecializationClasses < 1 ? 0 : countSpecializationClasses;
                                        char key = (char) (temp + 66);
                                        String endYear = schoolYear;
                                        String nameGenerate = "DH" + specialization.getCode() + endYear + key;

                                        specializationClass.setName(nameGenerate);
                                        specializationClass.setSpecializationId(specialization.getId());

                                        specializationClass = specializationClassRepository.saveAndFlush(specializationClass);

                                        if (specializationClass.getId() == null) {
                                            throw new ValidationException("Create Specialization Class fail !");
                                        }
                                    }

                                    for (int j = (studentGeneralList.size() - flag); j < studentGeneralList.size(); j++) {
                                        Student student = studentGeneralList.get(j);
                                        student.setSpecializationClassId(specializationClass.getId());

                                        student = studentRepository.saveAndFlush(student);

                                        if (student.getId() == null) {
                                            throw new ValidationException("Update Specialization Class for Student fail !!");
                                        }
                                    }

                                    flag = 0;
                                }
                            }
                        }
                        //#endregion

                        //#region Separate For University (High Quality)
                        StudentRequest studentHighQualityRequest = new StudentRequest();
                        studentHighQualityRequest.setSpecializationId(specialization.getId());
                        studentHighQualityRequest.setTypeOfEducation(TypeOfEducation.high_quality_program);
                        // Tìm sinh viên thuộc chuyên ngành chưa có lớp và thuộc niên khoá (Hệ chất lượng cao)
                        List<Student> studentHighQualityList = studentRepository.findAll(studentSpecification.getFilter(studentHighQualityRequest)).stream().filter(student -> student.getSpecializationClassId() == null).collect(Collectors.toList());

                        if (!studentHighQualityList.isEmpty()) {
                            // Nếu số lượng sinh viên chưa có lớp > 40
                            int flag = studentHighQualityList.size();
                            while (flag > 0) {
                                int temp = 0;
                                if (flag >= 40) {
                                    for (int i = 0; i < (flag / 40); i++) {
                                        SpecializationClass specializationClass = new SpecializationClass();
                                        temp += countSpecializationClasses < 1 ? 0 : countSpecializationClasses;
                                        char key = (char) (temp + 66);
                                        String endYear = schoolYear;
                                        String nameGenerate = "DH" + specialization.getCode() + endYear + key + "CLC";

                                        specializationClass.setName(nameGenerate);
                                        specializationClass.setSpecializationId(specialization.getId());

                                        specializationClass = specializationClassRepository.saveAndFlush(specializationClass);

                                        if (specializationClass.getId() == null) {
                                            throw new ValidationException("Create Specialization Class fail !");
                                        }

                                        int endList;
                                        if (flag >= 40) {
                                            endList = (i + 1) * 40;
                                        } else {
                                            endList = studentHighQualityList.size();
                                        }

                                        for (int j = i * 40; j < endList; j++) {
                                            Student student = studentHighQualityList.get(j);
                                            student.setSpecializationClassId(specializationClass.getId());

                                            student = studentRepository.saveAndFlush(student);

                                            if (student.getId() == null) {
                                                throw new ValidationException("Update Specialization Class for Student fail !!");
                                            }
                                        }

                                        flag -= 40;
                                        temp++;
                                    }
                                } else if (flag >= 20) {
                                    SpecializationClass specializationClass = new SpecializationClass();
                                    temp += countSpecializationClasses < 1 ? 0 : countSpecializationClasses;
                                    char key = (char) (temp + 66);
                                    String endYear = schoolYear;
                                    String nameGenerate = "DH" + specialization.getCode() + endYear + key + "CLC";

                                    specializationClass.setName(nameGenerate);
                                    specializationClass.setSpecializationId(specialization.getId());

                                    specializationClass = specializationClassRepository.saveAndFlush(specializationClass);

                                    if (specializationClass.getId() == null) {
                                        throw new ValidationException("Create Specialization Class fail !");
                                    }

                                    for (int j = (studentGeneralList.size() - flag); j < studentGeneralList.size(); j++) {
                                        Student student = studentGeneralList.get(j);
                                        student.setSpecializationClassId(specializationClass.getId());

                                        student = studentRepository.saveAndFlush(student);

                                        if (student.getId() == null) {
                                            throw new ValidationException("Update Specialization Class for Student fail !!");
                                        }
                                    }

                                    flag = 0;
                                } else {
                                    // Tìm lớp cuối cùng để add hết sv vào
                                    SpecializationClass specializationClass = specializationClassRepository.findFirstByOrderBySpecializationIdDesc();

                                    if(specializationClass == null){
                                        specializationClass = new SpecializationClass();
                                        temp += countSpecializationClasses < 1 ? 0 : countSpecializationClasses;
                                        char key = (char) (temp + 66);
                                        String endYear = schoolYear;
                                        String nameGenerate = "DH" + specialization.getCode() + endYear + key + "CLC";

                                        specializationClass.setName(nameGenerate);
                                        specializationClass.setSpecializationId(specialization.getId());

                                        specializationClass = specializationClassRepository.saveAndFlush(specializationClass);

                                        if (specializationClass.getId() == null) {
                                            throw new ValidationException("Create Specialization Class fail !");
                                        }
                                    }

                                    for (int j = (studentHighQualityList.size() - flag); j < studentHighQualityList.size(); j++) {
                                        Student student = studentHighQualityList.get(j);
                                        student.setSpecializationClassId(specializationClass.getId());

                                        student = studentRepository.saveAndFlush(student);

                                        if (student.getId() == null) {
                                            throw new ValidationException("Update Specialization Class for Student fail !!");
                                        }
                                    }

                                    flag = 0;
                                }
                            }
                        }
                        //#endregion
                    }
                }
            }, "Done");
            executor.submit(futureTasks);
        } catch (Exception e) {
            System.out.println("Fail to separate class for students !");
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

}
