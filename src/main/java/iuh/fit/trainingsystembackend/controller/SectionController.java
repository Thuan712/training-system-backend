package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.bean.SectionBean;
import iuh.fit.trainingsystembackend.bean.SectionClassBean;
import iuh.fit.trainingsystembackend.dto.SectionClassDTO;
import iuh.fit.trainingsystembackend.dto.SectionDTO;
import iuh.fit.trainingsystembackend.dto.UserInfoDTO;
import iuh.fit.trainingsystembackend.enums.*;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.mapper.SectionClassMapper;
import iuh.fit.trainingsystembackend.mapper.SectionMapper;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.*;
import iuh.fit.trainingsystembackend.request.SectionClassRequest;
import iuh.fit.trainingsystembackend.request.SectionRequest;
import iuh.fit.trainingsystembackend.service.ScheduleService;
import iuh.fit.trainingsystembackend.service.SectionService;
import iuh.fit.trainingsystembackend.specification.SectionClassSpecification;
import iuh.fit.trainingsystembackend.specification.SectionSpecification;
import iuh.fit.trainingsystembackend.utils.Constants;
import iuh.fit.trainingsystembackend.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.SECTION_ENDPOINT)
public class SectionController {

    private SectionRepository sectionRepository;
    private SectionSpecification sectionSpecification;
    private LecturerRepository lecturerRepository;
    private SectionClassRepository sectionClassRepository;
    private SectionClassSpecification sectionClassSpecification;
    private TermRepository termRepository;
    private SectionMapper sectionMapper;
    private SectionClassMapper sectionClassMapper;
    private TimeAndPlaceRepository timeAndPlaceRepository;
    private StudentRepository studentRepository;
    private ScheduleService scheduleService;
    private ScheduleRepository scheduleRepository;
    private SectionService sectionService;
    private StudentSectionRepository studentSectionRepository;

    @PostMapping("/createOrUpdate")
    public ResponseEntity<?> createOrUpdateSection(@RequestParam(value = "userId") Long userId, @RequestBody SectionBean data) {

        Section section = sectionService.createOrUpdateSection(data);

        if(section == null){
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(section);
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

        if (filterRequest.getStudentId() != null) {
            Term term = null;
            if (filterRequest.getTermId() != null) {
                term = termRepository.findById(filterRequest.getTermId()).orElse(null);

                if (term == null) {
                    throw new ValidationException("Không tìm thấy dữ liệu học kì này !!");
                }
            }

            Student student = studentRepository.findById(filterRequest.getStudentId()).orElse(null);

            if (student == null) {
                throw new ValidationException("Không tìm thấy sinh viên đăng ký học phần !!");
            }

            List<Long> studentSections = studentSectionRepository.findByStudentId(student.getId()).stream().map(StudentSection::getSectionId).collect(Collectors.toList());


            if (!studentSections.isEmpty()) {
                sections = sections.stream().filter(section -> !studentSections.contains(section.getId())).collect(Collectors.toList());
            }
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

        if (!lecturer.getSpecializationId().equals(section.getCourse().getSpecializationId())) {
            throw new ValidationException("Giảng viên này không thuộc chuyên ngành giảng dạy này !!");
        }

        toSave.setLecturerId(lecturer.getId());
        toSave.setNote(data.getNote());

        if (data.getSectionClassType() == null) {
            throw new ValidationException("Loại lớp học phần không được để trống !!");
        }

        if(data.getSectionClassType().equals(SectionClassType.theory)){
            if(section.getCourse().getCourseDuration().getPractice() > 0){
                toSave.setCreateStatus(false);
            }
        }

        toSave.setSectionClassType(data.getSectionClassType());

        if(section.getCourse().getCourseDuration().getPractice() > 0 && section.getCourse().getCourseDuration().getTheory() > 0){
            if(data.getSectionClassType().equals(SectionClassType.practice)){
                if (data.getRefId() == null) {
                    throw new ValidationException("Lớp lý thuyết của lớp thực hành không được để trống !!");
                }

                SectionClass sectionClassTheory = sectionClassRepository.findById(data.getRefId()).orElse(null);

                if (sectionClassTheory == null) {
                    throw new ValidationException("Không tìm thấy lớp lý thuyết cho lớp thực hành này !!");
                }

                if (!sectionClassTheory.getSectionId().equals(data.getSectionId())) {
                    throw new ValidationException("Lớp lý thuyết của lớp thực hành này phải thuộc cùng một học phần!!");
                }

                sectionClassTheory.setCreateStatus(true);
                sectionClassTheory = sectionClassRepository.saveAndFlush(sectionClassTheory);

                if(sectionClassTheory.getId() == null){
                    throw new ValidationException("Cập nhật trạng thái tạo cho lớp lý thuyết không thành công !!");
                }

                toSave.setRefId(sectionClassTheory.getId());
            }
        }

        if (data.getMinStudents() == null || data.getMinStudents() < 0) {
            throw new ValidationException("Sĩ số tối thiểu sinh viên của lớp học phần không được để trống !!");
        }
        toSave.setMinStudents(data.getMinStudents());

        if (data.getMaxStudents() == null || data.getMaxStudents() < 0 || data.getMaxStudents() < data.getMinStudents()) {
            throw new ValidationException("Sĩ số tối đa sinh viên của lớp học phần không được để trống và phải lớn hơn sỉ số tối đa !!");
        }

        toSave.setMaxStudents(data.getMaxStudents());

        toSave.setNote(data.getNote());
        toSave.setSectionClassStatus(SectionClassStatus.open);

        toSave = sectionClassRepository.saveAndFlush(toSave);

        if (toSave.getId() == null) {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Tạo lịch học cho lớp học phần
        if (data.getTimeAndPlaces() == null || data.getTimeAndPlaces().isEmpty()) {
            throw new ValidationException("Thời gian học của lớp học phần không được để trống !!!");
        }

        try {
            // Nếu là cập nhật lớp học phần (Có thể cập nhật lịch học) => Xoá bỏ các lịch học cũ
            List<TimeAndPlace> timeAndPlaces = timeAndPlaceRepository.findBySectionClassId(data.getId());
            timeAndPlaceRepository.deleteAll(timeAndPlaces);

            // Nếu là cập nhật lớp học phần (Có thể cập nhật lịch học) => Xoá bỏ các thời khoá biểu cũ
            List<Schedule> schedules = scheduleRepository.findScheduleBySectionClassId(data.getId());
            scheduleRepository.deleteAll(schedules);
        } catch (Exception ignored) {
        }

        Term term = section.getTerm();
        SectionClassRequest sectionClassRequest = new SectionClassRequest();
        sectionClassRequest.setLecturerId(toSave.getLecturerId());
        sectionClassRequest.setTermId(term.getId());

        // SectionClass in this term
        List<Long> sectionClassIds =  sectionClassRepository.findAll(sectionClassSpecification.getFilter(sectionClassRequest)).stream().map(SectionClass::getId).collect(Collectors.toList());

        // Tạo lại lịch học và thời khoá biểu cho lớp
        for (TimeAndPlace timeAndPlace : data.getTimeAndPlaces()) {

            TimeAndPlace timeAndPlaceToSave = new TimeAndPlace();

            List<TimeAndPlace> timeAndPlaces = timeAndPlaceRepository.findAll();
            if(!timeAndPlaces.isEmpty()){
                for(TimeAndPlace e : timeAndPlaces){
                    if(sectionClassIds.contains(e.getSectionClassId())){
                        if((e.getPeriodStart().equals(timeAndPlace.getPeriodStart())
                           || e.getPeriodStart().equals(timeAndPlace.getPeriodEnd())
                           || e.getPeriodEnd().equals(timeAndPlace.getPeriodEnd())
                           || e.getPeriodEnd().equals(timeAndPlace.getPeriodStart())) && e.getDayOfTheWeek().equals(timeAndPlace.getDayOfTheWeek())){
                            throw new ValidationException("Lịch học của lớp học phần này bị trùng với lớp học phần khác !!");
                        }
                    }
                }
            }

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

            int totalSession = 0;

            LocalDate localDateStartTerm = term.getTermStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate localDateEndTerm = term.getTermEnd().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            Duration duration = Duration.between(localDateStartTerm.atStartOfDay(), localDateEndTerm.atStartOfDay());
            long totalDaysInTerms = duration.toDays();

            if (toSave.getSectionClassType().equals(SectionClassType.theory)) {
                totalSession += section.getCourse().getCourseDuration().getTheory() * ((int) (totalDaysInTerms / 7) / ((timeAndPlaceToSave.getPeriodEnd() - timeAndPlaceToSave.getPeriodStart()) + 1));
            } else if (toSave.getSectionClassType().equals(SectionClassType.practice)) {
                totalSession += section.getCourse().getCourseDuration().getPractice() * 3 * ((int) (totalDaysInTerms / 7) / ((timeAndPlaceToSave.getPeriodEnd() - timeAndPlaceToSave.getPeriodStart()) + 1));
            }

            // Tạo thời khoá biểu cho lớp học phần
            List<Schedule> schedules = scheduleService.createSchedules(toSave, timeAndPlaceToSave, totalSession);

            if (schedules.isEmpty()) {
                return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        toSave = sectionClassRepository.saveAndFlush(toSave);

        if (toSave.getId() == null) {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
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

        if (filterRequest.getSectionClassRef() != null) {
            if (filterRequest.getSectionClassRef()) {
                sectionClasses = new PageImpl<>(sectionClasses.map(sectionClass -> sectionClass.getRefId() == null ? sectionClass : null).stream().filter(Objects::nonNull).collect(Collectors.toList()));
            } else {
                sectionClasses = new PageImpl<>(sectionClasses.map(sectionClass -> sectionClass.getRefId() != null ? sectionClass : null).stream().filter(Objects::nonNull).collect(Collectors.toList()));
            }
        }

        Page<SectionClassDTO> page = sectionClassMapper.mapToDTO(sectionClasses);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/class/getList")
    public ResponseEntity<?> getSectionClassList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody SectionClassRequest filterRequest) {
        List<SectionClass> sectionClasses = sectionClassRepository.findAll(sectionClassSpecification.getFilter(filterRequest));

        if (filterRequest.getSectionClassRef() != null) {
            if (filterRequest.getSectionClassRef()) {
                sectionClasses = sectionClasses.stream().filter(sectionClass -> sectionClass.getRefId() == null).collect(Collectors.toList());
            } else {
                sectionClasses = sectionClasses.stream().filter(sectionClass -> sectionClass.getRefId() != null).collect(Collectors.toList());
            }
        }

        List<SectionClassDTO> sectionClassDTOS = sectionClassMapper.mapToDTO(sectionClasses);
        return ResponseEntity.ok(sectionClassDTOS);
    }


    @GetMapping("/class/getById")
    public ResponseEntity<?> getById(@RequestParam(value = "userId", required = false) Long
                                             userId, @RequestParam(value = "id") Long id) {
        SectionClass sectionClass = sectionClassRepository.findById(id).orElse(null);

        if(sectionClass == null){
            throw new ValidationException("Không tìm thấy lớp học phần này !");
        }

        SectionClassDTO sectionClassDTO = sectionClassMapper.mapToDTO(sectionClass);
        return ResponseEntity.ok(sectionClassDTO);
    }
}

