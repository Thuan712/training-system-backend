package iuh.fit.trainingsystembackend.controller;

import com.google.gson.Gson;
import feign.Response;
import iuh.fit.trainingsystembackend.bean.SectionBean;
import iuh.fit.trainingsystembackend.bean.SectionClassBean;
import iuh.fit.trainingsystembackend.data.RequireSection;
import iuh.fit.trainingsystembackend.dto.SectionClassDTO;
import iuh.fit.trainingsystembackend.dto.SectionDTO;
import iuh.fit.trainingsystembackend.enums.SectionClassStatus;
import iuh.fit.trainingsystembackend.enums.SectionClassType;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.mapper.SectionClassMapper;
import iuh.fit.trainingsystembackend.mapper.SectionMapper;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.*;
import iuh.fit.trainingsystembackend.request.SectionClassRequest;
import iuh.fit.trainingsystembackend.request.SectionRequest;
import iuh.fit.trainingsystembackend.service.ScheduleService;
import iuh.fit.trainingsystembackend.specification.SectionClassSpecification;
import iuh.fit.trainingsystembackend.specification.SectionSpecification;
import iuh.fit.trainingsystembackend.utils.Constants;
import iuh.fit.trainingsystembackend.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.json.HTTP;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.SECTION_ENDPOINT)
public class SectionController {

    private SectionRepository sectionRepository;
    private CourseRepository courseRepository;
    private SpecializationRepository specializationRepository;
    private SectionSpecification sectionSpecification;
    private LecturerRepository lecturerRepository;
    private SectionClassRepository sectionClassRepository;
    private SectionClassSpecification sectionClassSpecification;
    private TermRepository termRepository;
    private SectionMapper sectionMapper;
    private SectionClassMapper sectionClassMapper;
    private TimeAndPlaceRepository timeAndPlaceRepository;
    private StudentSectionClassRepository studentSectionClassRepository;
    private UserRepository userRepository;
    private StudentRepository studentRepository;
    private ScheduleService scheduleService;
    private ScheduleRepository scheduleRepository;
    @PostMapping("/createOrUpdate")
    public ResponseEntity<?> createOrUpdateSection(@RequestParam(value = "userId") Long userId, @RequestBody SectionBean data) {
        Section toSave = null;

        if (data.getId() != null) {
            toSave = sectionRepository.findById(data.getId()).orElse(null);

            if (toSave == null) {
                throw new ValidationException("Không tìm thấy học phần !!");
            }
        }

        if (toSave == null) {
            toSave = new Section();
            boolean isExist = true;
            String code = "";

            while (isExist) {
                code = StringUtils.randomNumberGenerate(12);
                Section section = sectionRepository.findSectionByCode(code);

                isExist = section != null;
            }

            toSave.setCode(code);
        }

        if (data.getSpecializationId() == null) {
            throw new ValidationException("Chuyên ngành của học phần không được để trống !!");
        }

        Specialization specialization = specializationRepository.findById(data.getSpecializationId()).orElse(null);

        if (specialization == null) {
            throw new ValidationException("Không tìm thấy chuyên ngành của học phần !!");
        }

        toSave.setSpecializationId(specialization.getId());

        if (data.getName() == null || data.getName().isEmpty()) {
            throw new ValidationException("Tên của học phần không được để trống !!");
        }

        toSave.setName(data.getName());
        toSave.setDescription(data.getDescription());

        if (data.getCourseIds() == null || data.getCourseIds().isEmpty()) {
            throw new ValidationException("Học phần phải bao gồm ít nhất một môn học cấu thành !!");
        } else {
            for (Long courseId : data.getCourseIds()) {
                Course course = courseRepository.findById(courseId).orElse(null);

                if (course == null) {
                    throw new ValidationException("Không tìm thấy môn học có ID là " + courseId + "!!");
                }
            }

            toSave.setCourseIdsString(new Gson().toJson(data.getCourseIds()));
        }

        if (data.getSectionType() == null) {
            throw new ValidationException("Loại học phần không được để trống !!");
        }
        toSave.setSectionType(data.getSectionType());

        if (data.getTermRegister() == null || data.getTermRegister().isEmpty()) {
            throw new ValidationException("Học kì để đăng ký học phần không được để trống !!");
        }

        toSave.setTermRegisterString(new Gson().toJson(data.getTermRegister()));

        if (data.getCredits() != null) {
            if (data.getCredits() < 0) {
                throw new ValidationException("Số tín chỉ học tập của học phần phải lớn hơn bằng 0 !!");
            }

            toSave.setCredits(data.getCredits());
        } else {
            toSave.setCredits(0);
        }

        if (data.getCostCredits() == null || data.getCostCredits() < 1) {
            throw new ValidationException("Số tín chỉ học phí không được để trống và phải lớn hơn 0 !!");
        }

        toSave.setCostCredits(data.getCostCredits());

        if (data.getSectionDuration() == null) {
            throw new ValidationException("Thời lương tín chỉ của các công việc trong học phần không được để trống !!");
        }

        if (data.getSectionDuration().getTheory() < 0
            || data.getSectionDuration().getPractice() < 0
            || data.getSectionDuration().getDiscussionExercises() < 0
            || data.getSectionDuration().getSelfLearning() < 0
            || (data.getSectionDuration().getTheory() == 0
                && data.getSectionDuration().getPractice() == 0
                && data.getSectionDuration().getDiscussionExercises() == 0
                && data.getSectionDuration().getSelfLearning() == 0)) {
            throw new ValidationException("Tín chỉ thời lượng của tiết học phải lớn hơn hoặc bằng 0");
        }

        toSave.setSectionDurationString(new Gson().toJson(data.getSectionDuration()));

        toSave.setRequireSectionString(new Gson().toJson(data.getRequireSection() != null ? data.getRequireSection() : new RequireSection()));

        toSave = sectionRepository.saveAndFlush(toSave);
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
                                     @RequestBody SectionRequest filterRequest) {
        Page<Section> sections = sectionRepository.findAll(sectionSpecification.getFilter(filterRequest), PageRequest.of(pageNumber, pageRows, Sort.by(sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "id")));

        Page<SectionDTO> sectionDTO = sectionMapper.mapToDTO(sections);
        return ResponseEntity.ok(sectionDTO);
    }

    @PostMapping("/getList")
    public ResponseEntity<?> getList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody SectionRequest filterRequest) {

        List<Section> sections = sectionRepository.findAll(sectionSpecification.getFilter(filterRequest));

        if(filterRequest.getTermId() != null){
            Term term = termRepository.findById(filterRequest.getTermId()).orElse(null);

            if(term == null){
                throw new ValidationException("Không tìm thấy dữ liệu học kì này !!");
            }

            sections = sections.stream().filter(section -> section.getTermRegister().contains(term.getTermType())).collect(Collectors.toList());
        }

        List<SectionDTO> sectionDTOS = sectionMapper.mapToDTO(sections);
        return ResponseEntity.ok(sectionDTOS);
    }

    @PostMapping("/class/createOrUpdate")
    public ResponseEntity<?> createOrUpdateSectionClass(@RequestParam(value = "userId") Long userId, @RequestBody SectionClassBean data) throws ParseException {
        SectionClass toSave = null;

        if (data.getId() != null) {
            toSave = sectionClassRepository.findById(data.getId()).orElse(null);

            if (toSave == null) {
                throw new ValidationException("Không tìm thấy lớp học phần !!");
            }
        }

        if(data.getTermId() == null){
            throw new ValidationException("Mã học kỳ của lớp học phần không được để trống !!");
        }

        Term term = termRepository.findById(data.getTermId()).orElse(null);

        if(term == null){
            throw new ValidationException("Không tìm thấy học kỳ !!");
        }

        if (data.getSectionId() == null) {
            throw new ValidationException("Mã học phần của lớp học phần không được để trống !");
        }

        Section section = sectionRepository.findById(data.getSectionId()).orElse(null);

        if (section == null) {
            throw new ValidationException("Không tìm thấy học phần !");
        }


        if (toSave == null) {
            toSave = new SectionClass();

            toSave.setSectionId(section.getId());

            String code = "";
            boolean isExist = true;

            while (isExist) {
                code = StringUtils.randomNumberGenerate(10);
                isExist = sectionClassRepository.existsSectionClassByCode(code);
            }

            toSave.setCode(code);
        }

        if (data.getLecturerId() == null) {
            throw new ValidationException("Mã giảng viên đảm nhiệm lớp học phần không được để trống !");
        }

        Lecturer lecturer = lecturerRepository.findById(data.getLecturerId()).orElse(null);

        if (lecturer == null) {
            throw new ValidationException("Không tìm thấy giảng viên chủ nhiệm !!");
        }

        toSave.setLecturerId(lecturer.getId());
        toSave.setTermId(term.getId());
        toSave.setNote(data.getNote());

        if (data.getSectionClassType() == null) {
            throw new ValidationException("Loại lớp học phần không được để trống !!");
        }

        if (data.getSectionClassType().equals(SectionClassType.practice)) {
            if (data.getRefId() == null) {
                throw new ValidationException("Lớp lý thuyết của lớp thực hành không được để trống !!");
            }

            SectionClass sectionClassThoery = sectionClassRepository.findById(data.getRefId()).orElse(null);

            if (sectionClassThoery == null) {
                throw new ValidationException("Không tìm thấy lớp lý thuyết cho lớp thực hành này !!");
            }

            toSave.setRefId(sectionClassThoery.getId());
        }

        toSave.setSectionClassType(data.getSectionClassType());

        if (data.getNumberOfStudents() == null || data.getNumberOfStudents() < 0) {
            throw new ValidationException("Sĩ số tối đa sinh viên của lớp học phần không được để trống !!");
        }
        toSave.setNumberOfStudents(data.getNumberOfStudents());


        if (data.getSectionClassType() == null) {
            throw new ValidationException("Loại lớp học phần không được để trống !!");
        }

        toSave = sectionClassRepository.saveAndFlush(toSave);

        if (toSave.getId() == null) {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (data.getTimeAndPlaces() == null || data.getTimeAndPlaces().isEmpty()) {
            throw new ValidationException("Thời gian học của lớp học phần không được để trống !!!");
        }


        try {
            List<TimeAndPlace> timeAndPlaces = timeAndPlaceRepository.findBySectionClassId(data.getId());
            timeAndPlaceRepository.deleteAll(timeAndPlaces);

            List<Schedule> schedules = scheduleRepository.findScheduleBySectionClassId(data.getId());
            scheduleRepository.deleteAll(schedules);
        } catch (Exception exception) {
            System.out.println(exception);
        }

        int totalSession = 0;
        if(toSave.getSectionClassType().equals(SectionClassType.theory)){
            totalSession += section.getSectionDuration().getTheory() * 15;
        } else if(toSave.getSectionClassType().equals(SectionClassType.practice)){
            totalSession += section.getSectionDuration().getPractice() * 3 * 15;
        }

        for (TimeAndPlace timeAndPlace : data.getTimeAndPlaces()) {
            TimeAndPlace timeAndPlaceToSave = new TimeAndPlace();
            timeAndPlaceToSave.setSectionClassId(toSave.getId());

            if (timeAndPlace.getRoom() == null || timeAndPlace.getRoom().isEmpty()) {
                throw new ValidationException("Phòng học của lớp học phần không được để trống !!");
            }

            timeAndPlaceToSave.setRoom(timeAndPlace.getRoom());
            timeAndPlaceToSave.setNote(timeAndPlace.getNote());

            if (timeAndPlace.getPeriodStart() == null
                || timeAndPlace.getPeriodStart() < 1
                || timeAndPlace.getPeriodStart() > 15
            ) {
                throw new ValidationException("Tiết bắt đầu của lớp học phần không được để trống và nằm trong khoảng từ tiết 1 đến 15");
            }

            timeAndPlaceToSave.setPeriodStart(timeAndPlace.getPeriodStart());

            if (timeAndPlace.getPeriodEnd() == null
                || timeAndPlace.getPeriodEnd() < 1
                || timeAndPlace.getPeriodEnd() > 15
            ) {
                throw new ValidationException("Tiết kết thúc của lớp học phần không được để trống và nằm trong khoảng từ tiết 1 đến 15");
            }

            timeAndPlaceToSave.setPeriodEnd(timeAndPlace.getPeriodEnd());

            if (timeAndPlace.getDayOfTheWeek() == null) {
                throw new ValidationException("Thứ trong ngày học của lớp học phần không được để trống !!");
            }

            timeAndPlaceToSave.setDayOfTheWeek(timeAndPlace.getDayOfTheWeek());

            timeAndPlaceToSave = timeAndPlaceRepository.saveAndFlush(timeAndPlaceToSave);

            if (timeAndPlaceToSave.getId() == null) {
                return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Tạo thời khoá biểu cho lớp học phần
            List<Schedule> schedules = scheduleService.createSchedules(toSave, timeAndPlaceToSave, totalSession);

            if(schedules.isEmpty()){
                return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        SectionClassDTO sectionClassDTO = sectionClassMapper.mapToDTO(toSave);

        return ResponseEntity.ok(sectionClassDTO);
    }

    @PostMapping("/class/getPage")
    public ResponseEntity<?> getSectionClassPage(@RequestParam(value = "userId", required = false) Long userId,
                                                 @RequestParam("pageNumber") int pageNumber, @RequestParam("pageRows") int pageRows,
                                                 @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                                 @RequestParam(value = "sortOrder", required = false, defaultValue = "-1") int sortOrder,
                                                 @RequestBody SectionClassRequest filterRequest) {
        Page<SectionClass> sectionClasses = sectionClassRepository.findAll(sectionClassSpecification.getFilter(filterRequest), PageRequest.of(pageNumber, pageRows, Sort.by(sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "id")));
        Page<SectionClassDTO> page = sectionClassMapper.mapToDTO(sectionClasses);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/class/getList")
    public ResponseEntity<?> getSectionClassList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody SectionClassRequest filterRequest) {
        List<SectionClass> sectionClasses = sectionClassRepository.findAll(sectionClassSpecification.getFilter(filterRequest));
        List<SectionClassDTO> sectionClassDTOS = sectionClassMapper.mapToDTO(sectionClasses);
        return ResponseEntity.ok(sectionClassDTOS);
    }
    @PostMapping("/class/registerSection")
    public ResponseEntity<?> registerSectionClass(@RequestParam(value = "userId") Long userId, @RequestBody SectionClassBean data) {

        //#region Student - Section Class (Registration Class)

        // Theory
        StudentSectionClass studentSectionClassTheory = new StudentSectionClass();

        if(data.getSectionClassTheoryId() == null){
            throw new ValidationException("Lớp học phần lý thuyết của sinh viên không được để trống khi đăng ký !!");
        }

        SectionClass sectionClassTheory = sectionClassRepository.findById(data.getSectionClassTheoryId()).orElse(null);

        if(sectionClassTheory == null){
            throw new ValidationException("Không tìm thấy lớp học phần lý thuyết này !!");
        }

        if(sectionClassTheory.getSectionClassStatus().equals(SectionClassStatus.closed)){
            throw new ValidationException("Lớp học phần lý thuyết đã đóng đăng ký !!");
        }

        studentSectionClassTheory.setSectionClassId(sectionClassTheory.getId());

        Student student = studentRepository.getStudentByUserId(userId);

        if(student == null){
            throw new ValidationException("Không tìm thấy sinh viên đăng ký !!");
        }

        studentSectionClassTheory.setStudentId(student.getId());
        studentSectionClassTheory = studentSectionClassRepository.saveAndFlush(studentSectionClassTheory);

        if(studentSectionClassTheory.getId() == null){
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Practice
        StudentSectionClass studentSectionClassPractice = new StudentSectionClass();

        if(data.getSectionId() == null){
            throw new ValidationException("Học phần không được để trống khi đăng ký học phần !!");
        }

        Section section = sectionRepository.findById(data.getSectionId()).orElse(null);

        if(section == null){
            throw new ValidationException("Không tìm thấy học phần này !!");
        }

        if(section.getSectionDuration().getPractice() > 0 && data.getSectionClassPracticeId() == null){
            throw new ValidationException("Hãy chọn lớp thực hành của lớp học phần !!");
        }

        SectionClass sectionClassPractice = sectionClassRepository.findById(data.getSectionClassPracticeId()).orElse(null);

        if(sectionClassPractice == null){
            throw new ValidationException("Không tìm thấy lớp thực hành của lớp học phần !!");
        }

        if(sectionClassPractice.getSectionClassStatus().equals(SectionClassStatus.closed)){
            throw new ValidationException("Lớp học phần thực hành đã đóng đăng ký !!");
        }

        studentSectionClassPractice.setSectionClassId(sectionClassPractice.getId());
        studentSectionClassPractice.setStudentId(student.getId());
        studentSectionClassPractice = studentSectionClassRepository.saveAndFlush(studentSectionClassPractice);

        if(studentSectionClassPractice.getId() == null){
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //#endregion

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/class/cancelSection")
    public ResponseEntity<?> cancelSectionClass(@RequestParam(value = "userId") Long userId, @RequestBody SectionClassBean data) {


        return ResponseEntity.ok("");
    }
}
