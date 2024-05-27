package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.bean.ScheduleBean;
import iuh.fit.trainingsystembackend.dto.ScheduleDTO;
import iuh.fit.trainingsystembackend.enums.DayInWeek;
import iuh.fit.trainingsystembackend.enums.ScheduleType;
import iuh.fit.trainingsystembackend.enums.SystemRole;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.mapper.ScheduleMapper;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.*;
import iuh.fit.trainingsystembackend.request.ScheduleRequest;
import iuh.fit.trainingsystembackend.specification.ScheduleSpecification;
import iuh.fit.trainingsystembackend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.PREFIX_ENDPOINT + "schedule")
public class ScheduleController {
    private ScheduleRepository scheduleRepository;
    private ScheduleSpecification scheduleSpecification;
    private ScheduleMapper scheduleMapper;
    private SectionClassRepository sectionClassRepository;
    private LecturerRepository lecturerRepository;
    private final StudentSectionRepository studentSectionRepository;
    private final StudentSectionClassRepository studentSectionClassRepository;
    private final UserRepository userRepository;

    @PostMapping("/getList")
    public ResponseEntity<?> getList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody ScheduleRequest filterRequest) {
        List<Schedule> schedules = scheduleRepository.findAll(scheduleSpecification.getFilter(filterRequest));

        if (filterRequest.getStudentId() != null) {
            List<StudentSection> studentSections = studentSectionRepository.findByStudentId(filterRequest.getStudentId());
            Set<Long> sectionClassIds = new HashSet<>();
            if (!studentSections.isEmpty()) {
                for (StudentSection studentSection : studentSections) {
                    List<StudentSectionClass> studentSectionClasses = studentSectionClassRepository.findByStudentSectionId(studentSection.getId());

                    if (!studentSectionClasses.isEmpty()) {
                        sectionClassIds.addAll(studentSectionClasses.stream().map(StudentSectionClass::getSectionClassId).collect(Collectors.toList()));
                    }
                }
            } else {
                return ResponseEntity.ok(new ArrayList<>());
            }

            if (!sectionClassIds.isEmpty()) {
                schedules = schedules.stream().filter(schedule -> sectionClassIds.contains(schedule.getSectionClassId())).collect(Collectors.toList());
            }
        }

        List<ScheduleDTO> scheduleDTOS = scheduleMapper.mapToDTO(schedules);

        return ResponseEntity.ok(scheduleDTOS);
    }

    @PostMapping("/getPage")
    public ResponseEntity<?> getPage(@RequestParam(value = "userId", required = false) Long userId,
                                     @RequestParam("pageNumber") int pageNumber, @RequestParam("pageRows") int pageRows,
                                     @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                     @RequestParam(value = "sortOrder", required = false, defaultValue = "-1") int sortOrder,
                                     @RequestBody ScheduleRequest filterRequest) {
        Page<Schedule> schedules = scheduleRepository.findAll(scheduleSpecification.getFilter(filterRequest), PageRequest.of(pageNumber, pageRows, Sort.by(sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "id")));
        Page<ScheduleDTO> scheduleDTOS = scheduleMapper.mapToDTO(schedules);
        return ResponseEntity.ok(scheduleDTOS);
    }

    @PostMapping("/createOrUpdate")
    public ResponseEntity<?> createOrUpdateSchedule(@RequestParam(value = "userId", required = false) Long userId, @RequestBody ScheduleBean data) {
        Schedule toSave = null;

        if (data.getId() != null) {
            toSave = scheduleRepository.findById(data.getId()).orElse(null);

            if (toSave == null) {
                throw new ValidationException("Không tìm thấy thời khoá biểu này !!");
            }
        }

        boolean isCreate = toSave == null;
        if (toSave == null) {
            toSave = new Schedule();

            if (data.getSectionClassId() == null) {
                throw new ValidationException("Mã lớp học phần không được để trống !");
            }

            SectionClass sectionClass = sectionClassRepository.findById(data.getSectionClassId()).orElse(null);

            if (sectionClass == null) {
                throw new ValidationException("Không tìm thấy lớp học phần này !!");
            }

            toSave.setSectionClassId(sectionClass.getId());
        }

        if (data.getRoom() == null || data.getRoom().isEmpty()) {
            throw new ValidationException("Phòng học của thời khoá biểu này không được để trống !!");
        }

        toSave.setRoom(data.getRoom());


        if (data.getLecturerId() == null) {
            throw new ValidationException("Mã giảng viên giảng dạy không được để trống !!");
        }

        Lecturer lecturer = lecturerRepository.findById(data.getLecturerId()).orElse(null);

        if (lecturer == null) {
            throw new ValidationException("Không tìm thấy giảng viên này !!");
        }

        toSave.setLecturerId(lecturer.getId());

        if (data.getPeriodStart() == null) {
            throw new ValidationException("Tiết bắt đầu của thời khoá biểu không được để trống !!");
        }

        toSave.setPeriodStart(data.getPeriodStart());

        if (data.getPeriodEnd() == null) {
            throw new ValidationException("Tiết kết thúc của thời khoá biểu không được để trống !!!");
        }

        toSave.setPeriodEnd(data.getPeriodEnd());

        LocalDate localDateStartTerm = data.getLearningDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        DayOfWeek dayOfWeek = localDateStartTerm.getDayOfWeek();

        if (dayOfWeek.equals(DayOfWeek.MONDAY)) {
            toSave.setDayOfTheWeek(DayInWeek.monday);
        } else if (dayOfWeek.equals(DayOfWeek.TUESDAY)) {
            toSave.setDayOfTheWeek(DayInWeek.tuesday);
        } else if (dayOfWeek.equals(DayOfWeek.WEDNESDAY)) {
            toSave.setDayOfTheWeek(DayInWeek.wednesday);
        } else if (dayOfWeek.equals(DayOfWeek.THURSDAY)) {
            toSave.setDayOfTheWeek(DayInWeek.thursday);
        } else if (dayOfWeek.equals(DayOfWeek.FRIDAY)) {
            toSave.setDayOfTheWeek(DayInWeek.friday);
        } else if (dayOfWeek.equals(DayOfWeek.SATURDAY)) {
            toSave.setDayOfTheWeek(DayInWeek.saturday);
        } else if (dayOfWeek.equals(DayOfWeek.SUNDAY)) {
            toSave.setDayOfTheWeek(DayInWeek.sunday);
        }

        if (data.getScheduleType() == null || data.getScheduleType().equals(ScheduleType.normal)) {
            List<Schedule> schedules = scheduleRepository.findByLecturerIdOrRoomAndDayOfTheWeekAndLearningDate(lecturer.getId(), data.getRoom(), toSave.getDayOfTheWeek(), data.getLearningDate());
            if(!isCreate){
                schedules = schedules.stream().filter(schedule -> !schedule.getId().equals(data.getId())).collect(Collectors.toList());
            }

            if (!schedules.isEmpty()) {
                for (Schedule schedule : schedules) {
                    if (schedule.getPeriodStart().equals(data.getPeriodStart()) || schedule.getPeriodStart().equals(data.getPeriodEnd())) {
                        throw new ValidationException("Lịch học này đã bị trùng !!");
                    } else if (schedule.getPeriodEnd().equals(data.getPeriodStart()) || schedule.getPeriodEnd().equals(data.getPeriodEnd())) {
                        throw new ValidationException("Lịch học này đã bị trùng !!");
                    }
                }
            }
        } else if (data.getScheduleType().equals(ScheduleType.test)) {
            List<Schedule> schedules = scheduleRepository.findByDayOfTheWeekAndLearningDate(toSave.getDayOfTheWeek(), data.getLearningDate());

            if (!schedules.isEmpty()) {
                if (!isCreate) {
                    Schedule finalToSave = toSave;
                    schedules = schedules.stream().filter(schedule -> !schedule.getId().equals(finalToSave.getId())).collect(Collectors.toList());
                }

                for (Schedule schedule : schedules) {
                    if ((schedule.getPeriodStart().equals(data.getPeriodStart()) || schedule.getPeriodStart().equals(data.getPeriodEnd())) && (lecturer.getId().equals(schedule.getLecturerId()) || schedule.getRoom().equals(data.getRoom()))) {
                        throw new ValidationException("Giảng viên này đã có lịch dạy ở lớp khác hoặc lớp học này đã có lịch dạy !!");
                    } else if ((schedule.getPeriodEnd().equals(data.getPeriodStart()) || schedule.getPeriodEnd().equals(data.getPeriodEnd())) && (lecturer.getId().equals(schedule.getLecturerId()) || schedule.getRoom().equals(data.getRoom()))) {
                        throw new ValidationException("Giảng viên này đã có lịch dạy ở lớp khác hoặc lớp học này đã có lịch dạy!!");
                    }
                }
            }
        }

        toSave.setScheduleType(data.getScheduleType() != null ? data.getScheduleType() : ScheduleType.normal);

        toSave.setLearningDate(data.getLearningDate());
        toSave.setNote(data.getNote());

        toSave = scheduleRepository.saveAndFlush(toSave);

        if (toSave.getId() == null) {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ScheduleDTO scheduleDTO = scheduleMapper.mapToDTO(toSave);

        return ResponseEntity.ok(scheduleDTO);
    }

    @DeleteMapping("/deleteById")
    public ResponseEntity<?> deleteById(@RequestParam(value = "userId", required = false) Long userId, @RequestParam(value = "id") Long id) {

        Schedule schedule = scheduleRepository.findById(id).orElse(null);

        if (schedule == null) {
            throw new ValidationException("Không tìm thấy thời khoá biểu này !!");
        }

        scheduleRepository.delete(schedule);

        return ResponseEntity.ok(HttpStatus.OK);
    }

}
