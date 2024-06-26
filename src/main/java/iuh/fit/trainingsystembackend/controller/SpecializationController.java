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
import iuh.fit.trainingsystembackend.utils.StringUtils;
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
    private LecturerRepository lecturerRepository;
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
                throw new ValidationException("Không tìm thấy chuyên ngành !");
            }
        }
        boolean isCreate = toSave == null;

        if(data.getCode() != null && !data.getCode().isEmpty()){
            boolean isExistSpecializationCode = specializationRepository.existsByCode(data.getCode());

            if(isExistSpecializationCode){
                throw new ValidationException("Mã chuyên ngành này đã tồn tại !!");
            }
        }

        if(!isCreate){
            boolean isExistSpecializationName = specializationRepository.existsByNameIgnoreCase(data.getName());

            if(isExistSpecializationName){
                throw new ValidationException("Tên chuyên ngành này đã tồn tại !!");
            }
        }

        if (toSave == null) {
            toSave = new Specialization();
        }

        toSave.setName(data.getName());

        if(isCreate){
            if (data.getCode() == null || data.getCode().isEmpty()) {
                boolean isExist = true;
                String code = "";

                while(isExist){
                    code = StringUtils.randomNumberGenerate(8);
                    isExist = specializationRepository.existsByCode(code);
                }

                toSave.setCode(code);
            } else {
                boolean isDuplicate = specializationRepository.existsByCode(data.getCode());

                if(isDuplicate){
                    throw new ValidationException("Mã chuyên ngành đã tồn tại !!");
                } else {
                    toSave.setCode(data.getCode());
                }
            }
        }


        if (data.getFacultyId() == null) {
            throw new ValidationException("Mã khoa không dược để trống !");
        }

        Faculty faculty = facultyRepository.findById(data.getFacultyId()).orElse(null);

        if (faculty == null) {
            throw new ValidationException("Không tìm thấy khoa !");
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
                throw new ValidationException("Không tìm thấy lớp chuyên ngành !");
            }
        }
        boolean isCreate = toSave == null;

        if (data.getSpecializationId() == null) {
            throw new ValidationException("Mã chuyên ngành không được để trống !!");
        }

        Specialization specialization = specializationRepository.findById(data.getSpecializationId()).orElse(null);

        if (specialization == null) {
            throw new ValidationException("Không tìm thấy chuyên ngành !!");
        }

        if(data.getLecturerId() == null){
            throw new ValidationException("Mã giãng viên chủ nhiệm của lớp chuyên ngành không được để trống !!");
        }

        Lecturer lecturer = lecturerRepository.findById(data.getLecturerId()).orElse(null);

        if(lecturer == null){
            throw new ValidationException("Không tìm thấy giảng viên !!");
        }

        if(!isCreate){
            SpecializationClass specializationClass = specializationClassRepository.findByLecturerIdAndSpecializationIdAndSchoolYear(lecturer.getId(), specialization.getId(), data.getSchoolYear());

            if(specializationClass != null){
                throw new ValidationException("Lớp chuyên ngành của năm học này đã tồn tại !!");
            }
        }

        if (toSave == null) {
            toSave = new SpecializationClass();
        }

        toSave.setLecturerId(lecturer.getId());
        toSave.setSchoolYear(data.getSchoolYear());
        toSave.setSpecializationId(specialization.getId());

        if(data.getName() == null || data.getName().isEmpty()){
            int numOfSpecializationClass = specializationClassRepository.countBySpecializationId(specialization.getId());

            if(numOfSpecializationClass == 0){
                char key = (char) (65);
                String endYear = data.getSchoolYear();
                String nameGenerate = "DH" + specialization.getCode() + endYear + key;

                toSave.setName(nameGenerate);
            } else {
                char key = (char) (numOfSpecializationClass + 65);
                String endYear = data.getSchoolYear();
                String nameGenerate = "DH" + specialization.getCode() + endYear + key;

                toSave.setName(nameGenerate);
            }
        } else {
            toSave.setName(data.getName());
        }

        if(data.getNumberOfStudents() == null || data.getNumberOfStudents() < 1){
            throw new ValidationException("Sỉ số tối đa sinh viên của lớp chuyên ngành không được để trống và phải lớn hơn 1 !!");
        }

        toSave.setNumberOfStudents(data.getNumberOfStudents());

        toSave = specializationClassRepository.saveAndFlush(toSave);

        if (toSave.getId() == null) {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        SpecializationClassDTO specializationClassDTO = specializationClassMapper.mapToDTO(toSave);

        return ResponseEntity.ok(specializationClassDTO);
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
                                            throw new ValidationException("Tạo lớp chuyên ngành không thành công !");
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
                                                throw new ValidationException("Cập nhật lớp chuyên ngành cho sinh viên không thành công !!");
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
                                        throw new ValidationException("Tạo lớp chuyên ngành không thành công !!");
                                    }

                                    for (int j = (studentGeneralList.size() - flag); j < studentGeneralList.size(); j++) {
                                        Student student = studentGeneralList.get(j);
                                        student.setSpecializationClassId(specializationClass.getId());

                                        student = studentRepository.saveAndFlush(student);

                                        if (student.getId() == null) {
                                            throw new ValidationException("Cập nhật lớp chuyên ngành cho sinh viên không thành công !!");
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
                                            throw new ValidationException("Tạo lớp chuyên ngành không thành công !");
                                        }
                                    }

                                    for (int j = (studentGeneralList.size() - flag); j < studentGeneralList.size(); j++) {
                                        Student student = studentGeneralList.get(j);
                                        student.setSpecializationClassId(specializationClass.getId());

                                        student = studentRepository.saveAndFlush(student);

                                        if (student.getId() == null) {
                                            throw new ValidationException("Cập nhật lớp chuyên ngành cho sinh viên không thành công !!");
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
                                            throw new ValidationException("Tạo lớp chuyên ngành không thành công !");
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
                                                throw new ValidationException("Cập nhật lớp chuyên ngành cho sinh viên không thành công !!");
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
                                        throw new ValidationException("Tạo lớp chuyên ngành không thành công !");
                                    }

                                    for (int j = (studentGeneralList.size() - flag); j < studentGeneralList.size(); j++) {
                                        Student student = studentGeneralList.get(j);
                                        student.setSpecializationClassId(specializationClass.getId());

                                        student = studentRepository.saveAndFlush(student);

                                        if (student.getId() == null) {
                                            throw new ValidationException("Cập nhật lớp chuyên ngành cho sinh viên không thành công !!");
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
                                            throw new ValidationException("Tạo lớp chuyên ngành không thành công !");
                                        }
                                    }

                                    for (int j = (studentHighQualityList.size() - flag); j < studentHighQualityList.size(); j++) {
                                        Student student = studentHighQualityList.get(j);
                                        student.setSpecializationClassId(specializationClass.getId());

                                        student = studentRepository.saveAndFlush(student);

                                        if (student.getId() == null) {
                                            throw new ValidationException("Cập nhật lớp chuyên ngành cho sinh viên không thành công !!");
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
            System.out.println("Có lỗi xảy ra! Không thể phân chia lớp cho sinh viên!");
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

}
