package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.bean.SectionBean;
import iuh.fit.trainingsystembackend.bean.SectionClassBean;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.*;
import iuh.fit.trainingsystembackend.request.SectionClassRequest;
import iuh.fit.trainingsystembackend.request.SectionRequest;
import iuh.fit.trainingsystembackend.specification.SectionClassSpecification;
import iuh.fit.trainingsystembackend.specification.SectionSpecification;
import iuh.fit.trainingsystembackend.token.RefreshToken;
import iuh.fit.trainingsystembackend.utils.Constants;
import iuh.fit.trainingsystembackend.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
        return ResponseEntity.ok(sections);
    }

    @PostMapping("/getList")
    public ResponseEntity<?> getList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody SectionRequest filterRequest) {
        List<Section> sections = sectionRepository.findAll(sectionSpecification.getFilter(filterRequest));
        return ResponseEntity.ok(sections);
    }

    @PostMapping("/class/createOrUpdate")
    public ResponseEntity<?> createOrUpdateSectionClass(@RequestParam(value = "userId") Long userId, @RequestBody SectionClassBean data) {
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
        return ResponseEntity.ok(sectionClasses);
    }

    @PostMapping("/class/getList")
    public ResponseEntity<?> getSectionClassList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody SectionClassRequest filterRequest) {
        List<SectionClass> sectionClasses = sectionClassRepository.findAll(sectionClassSpecification.getFilter(filterRequest));
        return ResponseEntity.ok(sectionClasses);
    }

    @PostMapping("/class/registerSection")
    public ResponseEntity<?> registerSectionClass(@RequestParam(value = "userId") Long userId, @RequestBody List<StudentSectionClass> data) {
        List<StudentSectionClass> sectionClasses = new ArrayList<>();
        if (!data.isEmpty()) {
            for (StudentSectionClass studentSectionClass : data) {
                StudentSectionClass toSave = null;
                if (studentSectionClass.getId() != null) {
                    toSave = studentSectionClassRepository.findById(studentSectionClass.getId()).orElse(null);

                    if (toSave == null) {
                        throw new ValidationException("Student in Section Class is not found !!");
                    }
                }

                if (toSave == null) {
                    toSave = new StudentSectionClass();
                }

                if (studentSectionClass.getStudentId() == null) {
                    throw new ValidationException("Student ID is required !");
                }

                Student student = studentRepository.findById(studentSectionClass.getStudentId()).orElse(null);

                if (student == null) {
                    throw new ValidationException("Student is not found !!");
                }

                toSave.setStudentId(student.getId());

                if (studentSectionClass.getSectionClassId() == null) {
                    throw new ValidationException("Section Class ID is required !!");
                }

                SectionClass sectionClass = sectionClassRepository.findById(studentSectionClass.getSectionClassId()).orElse(null);

                if (sectionClass == null) {
                    throw new ValidationException("Section Class is not found !!");
                }

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
                    FutureTask<String> futureTasks = new FutureTask<>(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < sectionClass.getSection().getTheoryPeriods() /3; i++){
                                Schedule schedule = new Schedule();
                                schedule.setSectionClassId(sectionClass.getId());
                                Date date = sectionClass.getStartedAt();
                                Instant instantDateLearn = Instant.ofEpochMilli(date.getTime());
                                schedule.setLearningDate(Date.from(Instant.from(LocalDateTime.ofInstant(instantDateLearn, ZoneId.systemDefault()).toLocalDate().plusDays(i * 7L))));

                                schedule = scheduleRepository.saveAndFlush(schedule);

                                if(schedule.getId() == null){
                                    throw new ValidationException("Create Schedule failed !!");
                                }
                            }
                        }
                    }, "Done");

                    executor.submit(futureTasks);
                } catch (Exception exception) {
                    System.out.println("Fail to create schedule !!");
                }

                sectionClasses.add(toSave);
            }
        }

        return ResponseEntity.ok(sectionClasses);
    }
}
