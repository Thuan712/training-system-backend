package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.bean.UserBean;
import iuh.fit.trainingsystembackend.dto.UserInfoDTO;
import iuh.fit.trainingsystembackend.enums.SystemRole;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.mapper.UserInfoMapper;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.*;
import iuh.fit.trainingsystembackend.request.UserRequest;
import iuh.fit.trainingsystembackend.service.AddressService;
import iuh.fit.trainingsystembackend.specification.UserSpecification;
import iuh.fit.trainingsystembackend.utils.Constants;
import iuh.fit.trainingsystembackend.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.USER_ENDPOINT)
public class UserController {
    private UserRepository userRepository;
    private LecturerRepository lecturerRepository;
    private StudentRepository studentRepository;
    private UserSpecification userSpecification;
    private AcademicYearRepository academicYearRepository;
    private SpecializationRepository specializationRepository;
    private AddressService addressService;
    private UserInfoMapper userInfoMapper;
    private SpecializationClassRepository specializationClassRepository;
    @PostMapping("/createOrUpdate")
    public ResponseEntity<?> createOrUpdate(@RequestParam(value = "userId") Long userId, @RequestBody UserBean data) {
        UserEntity toSave = null;

        if (data == null) {
            throw new ValidationException("Data is required !");
        }

        if (data.getDob() != null && !data.getDob().isEmpty()) {
            //Converting the input String to Date
            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.setLenient(false);

            try {
                Date date = sdf.parse(data.getDob());
                Date now = new Date();
                if (!date.before(now)) {
                    throw new ValidationException("Date of Birth is greater than current year !");
                }
            } catch (ParseException e) {
                throw new ValidationException("Invalid Date of Birth !");
            }
        }

        if (data.getId() != null) {
            toSave = userRepository.findById(data.getId()).orElse(null);
            if (toSave == null) {
                throw new ValidationException("User is not found !");
            }
        }

        boolean isCreate = toSave == null;

        if (!isCreate) {
            toSave.setUpdatedAt(new Date());

            if (toSave.getSystemRole().equals(SystemRole.lecturer)) {
                Lecturer lecturer = lecturerRepository.getLecturersByUserId(toSave.getId());

                if (lecturer == null) {
                    throw new ValidationException("Lecturer is not found !");
                }

                lecturer.setPosition(data.getPosition());
                lecturer.setTitle(data.getTitle());
            } else if (toSave.getSystemRole().equals(SystemRole.student)) {
                Student student = studentRepository.getStudentByUserId(toSave.getId());

                if (student == null) {
                    throw new ValidationException("Student is not found !");
                }

                student.setTypeOfEducation(data.getTypeOfEducation());
            }
        } else {
            toSave = new UserEntity();

            toSave.setSystemRole(data.getSystemRole());

            String code = "";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy");
            String year = simpleDateFormat.format(toSave.getCreatedAt());

            if (data.getSystemRole().equals(SystemRole.student)) {
                code = year + StringUtils.randomNumberGenerate(5);

                boolean isExist = true;
                while (isExist) {
                    code = year + StringUtils.randomNumberGenerate(5);
                    isExist = userRepository.existsByCode(code);
                }

            } else if (data.getSystemRole().equals(SystemRole.lecturer)) {
                code = "GV" + StringUtils.randomNumberGenerate(6);

                boolean isExist = true;
                while (isExist) {
                    code = "GV" + StringUtils.randomNumberGenerate(6);
                    isExist = userRepository.existsByCode(code);
                }
            } else if (data.getSystemRole().equals(SystemRole.admin)) {
                code = "AD" + StringUtils.randomNumberGenerate(6);

                boolean isExist = true;
                while (isExist) {
                    code = "AD" + StringUtils.randomNumberGenerate(6);
                    isExist = userRepository.existsByCode(code);
                }
            }

            if (!code.isEmpty()) {
                toSave.setCode(code);
                toSave.setUsername(code);
                toSave.setEmail(code + "." + data.getFirstName().toLowerCase() + "@iuh.edu.com");
            }

            String encodedPassword = new BCryptPasswordEncoder().encode("1111");
            toSave.setPassword(encodedPassword);

            toSave.setActive(true);
            toSave.setDeleted(false);
        }

        toSave.setFirstName(StringUtils.capitalize(data.getFirstName()));
        toSave.setLastName(StringUtils.capitalize(data.getLastName()));
        toSave.setAvatar(data.getAvatar());
        toSave.setDob(data.getDob());
        toSave.setCINumber(data.getCINumber());
        toSave.setGender(data.getGender());

        toSave = userRepository.saveAndFlush(toSave);

        if (toSave.getId() == null) {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(isCreate){
            if(data.getSpecializationId() == null){
                throw new ValidationException("Specialization ID is required !");
            }

            Specialization specialization = specializationRepository.findById(data.getSpecializationId()).orElse(null);

            if (specialization == null){
                throw new ValidationException("Specialization is not found !");
            }

            if (toSave.getSystemRole().equals(SystemRole.lecturer)) {
                Lecturer lecturer = new Lecturer();

                lecturer.setPosition(data.getPosition());
                lecturer.setTitle(data.getTitle());
                lecturer.setUserId(toSave.getId());
                lecturer.setSpecializationId(specialization.getId());

                lecturer = lecturerRepository.saveAndFlush(lecturer);

                if (lecturer.getId() == null) {
                    throw new ValidationException("Create lecturer fail");
                }
            } else if (toSave.getSystemRole().equals(SystemRole.student)) {
                Student student = new Student();
                student.setSpecializationId(specialization.getId());
                student.setTypeOfEducation(data.getTypeOfEducation());
                student.setUserId(toSave.getId());

                if(data.getSpecializationClassId() != null){
                    SpecializationClass specializationClass = specializationClassRepository.findById(data.getSpecializationClassId()).orElse(null);

                    if(specializationClass == null){
                        throw new ValidationException("Specialization Class is not found !");
                    }
                    student.setSpecializationClassId(specializationClass.getId());
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
                String year = simpleDateFormat.format(student.getEntryDate());
                student.setSchoolYear(year);
                student = studentRepository.saveAndFlush(student);

                if (student.getId() == null) {
                    throw new ValidationException("Create Student fail !");
                }
            }

        }

        //TODO: Create Or Update Address
        data.setUserId(toSave.getId());

        Address toSaveAddress = addressService.saveAddress(data);
        if (toSaveAddress.getId() == null) {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(toSave);
    }

    @GetMapping("/getById")
    public ResponseEntity<?> getById(@RequestParam(value = "userId", required = false) Long
                                             userId, @RequestParam(value = "id") Long id) {
        UserEntity userEntity = userRepository.findById(id).orElse(null);

        if(userEntity == null){
            throw new ValidationException("User is not found !");
        }

        UserInfoDTO userInfoDTO = userInfoMapper.mapToDTO(userEntity);
        return ResponseEntity.ok(userInfoDTO);
    }

    @PostMapping("/getPage")
    public ResponseEntity<?> getPage(@RequestParam(value = "userId", required = false) Long userId,
                                     @RequestParam("pageNumber") int pageNumber, @RequestParam("pageRows") int pageRows,
                                     @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                     @RequestParam(value = "sortOrder", required = false, defaultValue = "-1") int sortOrder,
                                     @RequestBody UserRequest filterRequest) {
        Page<UserEntity> userEntities = userRepository.findAll(userSpecification.getFilter(filterRequest), PageRequest.of(pageNumber, pageRows, Sort.by(sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "id")));

        Page<UserInfoDTO> userInfoDTO = userInfoMapper.mapToDTO(userEntities);
        return ResponseEntity.ok(userInfoDTO);
    }

    @PostMapping("/getList")
    public ResponseEntity<?> getList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody UserRequest filterRequest) {
        List<UserEntity> userEntities = userRepository.findAll(userSpecification.getFilter(filterRequest));
        return ResponseEntity.ok(userEntities);
    }
}
