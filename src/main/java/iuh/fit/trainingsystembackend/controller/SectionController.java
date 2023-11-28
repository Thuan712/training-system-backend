package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.bean.SectionBean;
import iuh.fit.trainingsystembackend.bean.SectionClassBean;
import iuh.fit.trainingsystembackend.dto.SectionClassDTO;
import iuh.fit.trainingsystembackend.dto.SectionDTO;
import iuh.fit.trainingsystembackend.enums.SectionClassType;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.mapper.SectionClassMapper;
import iuh.fit.trainingsystembackend.mapper.SectionMapper;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.*;
import iuh.fit.trainingsystembackend.request.SectionClassRequest;
import iuh.fit.trainingsystembackend.request.SectionRequest;
import iuh.fit.trainingsystembackend.specification.SectionClassSpecification;
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

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.SECTION_ENDPOINT)
public class SectionController {

    private SectionRepository sectionRepository;
    private CourseRepository courseRepository;
    private TermRepository termRepository;
    private SectionSpecification sectionSpecification;
    private LecturerRepository lecturerRepository;
    private SectionClassRepository sectionClassRepository;
    private SectionClassSpecification sectionClassSpecification;
    private StudentSectionClassRepository studentSectionClassRepository;
    private StudentRepository studentRepository;
    private ScheduleRepository scheduleRepository;
    private SectionMapper sectionMapper;
    private SectionClassMapper sectionClassMapper;

    @PostMapping("/createOrUpdate")
    public ResponseEntity<?> createOrUpdateSection(@RequestParam(value = "userId") Long userId, @RequestBody SectionBean data) {
        Section toSave = null;

        if (data.getId() != null) {
            toSave = sectionRepository.findById(data.getId()).orElse(null);

            if (toSave == null) {
                throw new ValidationException("Section is not found !!");
            }
        }

        if (data.getCourseId() == null) {
            throw new ValidationException("Course ID is required !!");
        }

        Course course = courseRepository.findById(data.getCourseId()).orElse(null);

        if (course == null) {
            throw new ValidationException("Course is not found !!");
        }

        if (data.getTermId() == null) {
            throw new ValidationException("Term ID is required !!");
        }

        Term term = termRepository.findById(data.getTermId()).orElse(null);

        if (term == null) {
            throw new ValidationException("Term is not found !!");
        }

        if (toSave == null) {
            toSave = new Section();
            toSave.setCourseId(course.getId());
            toSave.setTermId(term.getId());

        }

        toSave.setName(data.getName());
        boolean isExist = true;
        String code = "";

        while (isExist) {
            code = StringUtils.randomNumberGenerate(12);
            Section section = sectionRepository.findSectionByCode(code);

            if (section != null) {
                isExist = true;
            } else {
                isExist = false;
            }
        }

        toSave.setCode(code);
        if (data.getTheoryPeriods() == null || data.getTheoryPeriods() < 1) {
            throw new ValidationException("Theory Periods should be greater than 2 !");
        }

        toSave.setTheoryPeriods(data.getTheoryPeriods());
        toSave.setPracticePeriods(data.getPracticePeriods());
        toSave.setSectionType(data.getSectionType());

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
        List<SectionDTO> sectionDTOS = sectionMapper.mapToDTO(sections);
        return ResponseEntity.ok(sectionDTOS);
    }

    @PostMapping("/class/createOrUpdate")
    public ResponseEntity<?> createOrUpdateSectionClass(@RequestParam(value = "userId") Long userId, @RequestBody SectionClassBean data) throws ParseException {
        SectionClass toSave = null;

        if (data.getId() != null) {
            toSave = sectionClassRepository.findById(data.getId()).orElse(null);

            if (toSave == null) {
                throw new ValidationException("Section Class is not found !!");
            }
        }

        if (toSave == null) {
            toSave = new SectionClass();

            if (data.getSectionId() == null) {
                throw new ValidationException("Section ID is require !");
            }

            Section section = sectionRepository.findById(data.getSectionId()).orElse(null);

            if (section == null) {
                throw new ValidationException("Section is not found !");
            }

            toSave.setSectionId(section.getId());

            String classCode = "";
            boolean isExist = true;

            while (isExist) {
                classCode = StringUtils.randomNumberGenerate(10);
                isExist = sectionClassRepository.existsSectionClassByClassCode(classCode);
            }

            toSave.setClassCode(classCode);
        }

        if (data.getLecturerId() == null) {
            throw new ValidationException("Lecturer ID is require !");
        }

        Lecturer lecturer = lecturerRepository.findById(data.getLecturerId()).orElse(null);

        if (lecturer == null) {
            throw new ValidationException("Lecturer is not found !!");
        }

        toSave.setLecturerId(lecturer.getId());

        if (data.getPeriodTo() == null || data.getPeriodTo() < 1 || data.getPeriodTo() > 15) {
            throw new ValidationException("Period To is invalid ! \nPeriod should be greater than 0 and lower than 15");
        }

        toSave.setPeriodTo(data.getPeriodTo());

        if (data.getPeriodFrom() == null || data.getPeriodFrom() < 1 || data.getPeriodFrom() > 15) {
            throw new ValidationException("Period From is invalid ! \nPeriod should be greater than 0 and lower than 15");
        }

        toSave.setPeriodFrom(data.getPeriodFrom());

        if (data.getRoom() == null || data.getRoom().isEmpty()) {
            throw new ValidationException("Room is required !");
        }

        toSave.setRoom(data.getRoom());
        toSave.setNote(data.getNote());
        toSave.setSectionClassType(data.getSectionClassType());
        toSave.setNumberOfStudents(data.getNumberOfStudents());
        toSave.setDayInWeek(data.getDayInWeek());

        String inputModified = data.getStartedAt().replace("T", " ");

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSX");
        LocalDateTime result = LocalDateTime.parse(inputModified, format);
        Date out = Date.from(result.atZone(ZoneId.systemDefault()).toInstant());
        toSave.setStartedAt(out);

        toSave = sectionClassRepository.saveAndFlush(toSave);

        if (toSave.getId() == null) {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(toSave);
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

        List<SectionClass> sectionClasses = sectionClassRepository.findSectionClassBySectionIdAndLecturerId(data.getSectionId(), data.getLecturerId());
        if(!sectionClasses.isEmpty()){
            for(SectionClass sectionClass : sectionClasses){
                StudentSectionClass toSave =new StudentSectionClass();

                if (data.getStudentId() == null) {
                    throw new ValidationException("Student ID is required !");
                }

                Student student = studentRepository.findById(data.getStudentId()).orElse(null);

                if (student == null) {
                    throw new ValidationException("Student is not found !!");
                }

                toSave.setStudentId(student.getId());


                int studentsInClass = studentSectionClassRepository.countAllBySectionClassId(sectionClass.getId());
                if (studentsInClass >= sectionClass.getNumberOfStudents()) {
                    throw new ValidationException("The number of students in the class taking this section has reached its maximum !!");
                }

                toSave.setSectionClassId(sectionClass.getId());

                toSave = studentSectionClassRepository.saveAndFlush(toSave);

                if (toSave.getId() == null) {
                    return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
                }

                try {
                    ExecutorService executor = Executors.newFixedThreadPool(1);
                    StudentSectionClass finalToSave = toSave;
                    FutureTask<String> futureTasks = new FutureTask<>(new Runnable() {
                        @Override
                        public void run() {
                            if(sectionClass.getSectionClassType().equals(SectionClassType.theory)){
                                for (int i = 0; i < sectionClass.getSection().getTheoryPeriods() * 15; i++) {
                                    Schedule schedule = new Schedule();
                                    schedule.setSectionClassId(sectionClass.getId());
                                    Date date = sectionClass.getStartedAt();
                                    Instant instantDateLearn = Instant.ofEpochMilli(date.getTime());
                                    schedule.setLearningDate(Date.from(Instant.from(LocalDateTime.ofInstant(instantDateLearn, ZoneId.systemDefault()).toLocalDate().plusDays(i * 7L))));
                                    schedule.setStudentSectionClassId(finalToSave.getId());

                                    schedule = scheduleRepository.saveAndFlush(schedule);

                                    if (schedule.getId() == null) {
                                        throw new ValidationException("Create Schedule failed !!");
                                    }

                                }
                            } else {
                                for (int i = 0; i < sectionClass.getSection().getPracticePeriods() * 30; i++) {
                                    Schedule schedule = new Schedule();
                                    schedule.setSectionClassId(sectionClass.getId());
                                    Date date = sectionClass.getStartedAt();
                                    Instant instantDateLearn = Instant.ofEpochMilli(date.getTime());
                                    LocalDate localDate = LocalDateTime.ofInstant(instantDateLearn, ZoneId.systemDefault()).toLocalDate().plusDays(i * 7);
                                    schedule.setLearningDate(Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                                    schedule.setStudentSectionClassId(finalToSave.getId());
                                    schedule = scheduleRepository.saveAndFlush(schedule);

                                    if (schedule.getId() == null) {
                                        throw new ValidationException("Create Schedule failed !!");
                                    }

                                }

                            }

                        }
                    }, "Done");

                    executor.submit(futureTasks);
                } catch (Exception exception) {
                    System.out.println("Fail to create schedule !!");
                }

            }
        }


        return ResponseEntity.ok(HttpStatus.OK);
    }
}
