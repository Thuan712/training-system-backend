package iuh.fit.trainingsystembackend.service;

import iuh.fit.trainingsystembackend.enums.DayInWeek;
import iuh.fit.trainingsystembackend.enums.ScheduleType;
import iuh.fit.trainingsystembackend.enums.SectionClassType;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.LecturerRepository;
import iuh.fit.trainingsystembackend.repository.ScheduleRepository;
import iuh.fit.trainingsystembackend.repository.SectionRepository;
import iuh.fit.trainingsystembackend.repository.TermRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ScheduleService {
    private TermRepository termRepository;
    private ScheduleRepository scheduleRepository;
    private final SectionRepository sectionRepository;
    private final LecturerRepository lecturerRepository;

    public List<Schedule> createSchedules(SectionClass sectionClass, TimeAndPlace timeAndPlace, int totalSession) {
        if (sectionClass == null || timeAndPlace == null) {
            return new ArrayList<>();
        }

        Section section = sectionRepository.findById(sectionClass.getSectionId()).orElse(null);

        if(section == null){
            return new ArrayList<>();
        }

        Lecturer lecturer = lecturerRepository.findById(sectionClass.getLecturerId()).orElse(null);

        if(lecturer == null){
            return new ArrayList<>();
        }

        Term term = termRepository.findById(section.getTermId()).orElse(null);

        if (term == null) {
            System.out.println("Không tìm thấy học kỳ để tạo thời khoá biểu !!");
            return new ArrayList<>();
        }
        // Tìm ngày bắt đầu của học kỳ
        LocalDate localDateStartTerm = term.getTermStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        DayOfWeek dayOfWeek = localDateStartTerm.getDayOfWeek();

        // Xác định ngày bắt đầu học
        LocalDate startDate = null;
        if (timeAndPlace.getDayOfTheWeek().equals(DayInWeek.monday)) {
            if (dayOfWeek.equals(DayOfWeek.MONDAY)) {
                startDate = localDateStartTerm.plusDays(0);
            } else if (dayOfWeek.equals(DayOfWeek.TUESDAY)) {
                startDate = localDateStartTerm.plusDays(6);
            } else if (dayOfWeek.equals(DayOfWeek.WEDNESDAY)) {
                startDate = localDateStartTerm.plusDays(5);
            } else if (dayOfWeek.equals(DayOfWeek.THURSDAY)) {
                startDate = localDateStartTerm.plusDays(4);
            } else if (dayOfWeek.equals(DayOfWeek.FRIDAY)) {
                startDate = localDateStartTerm.plusDays(3);
            } else if (dayOfWeek.equals(DayOfWeek.SATURDAY)) {
                startDate = localDateStartTerm.plusDays(2);
            } else if (dayOfWeek.equals(DayOfWeek.SUNDAY)) {
                startDate = localDateStartTerm.plusDays(1);
            }
        } else if (timeAndPlace.getDayOfTheWeek().equals(DayInWeek.tuesday)) {
            if (dayOfWeek.equals(DayOfWeek.MONDAY)) {
                startDate = localDateStartTerm.plusDays(1);
            } else if (dayOfWeek.equals(DayOfWeek.TUESDAY)) {
                startDate = localDateStartTerm.plusDays(0);
            } else if (dayOfWeek.equals(DayOfWeek.WEDNESDAY)) {
                startDate = localDateStartTerm.plusDays(6);
            } else if (dayOfWeek.equals(DayOfWeek.THURSDAY)) {
                startDate = localDateStartTerm.plusDays(5);
            } else if (dayOfWeek.equals(DayOfWeek.FRIDAY)) {
                startDate = localDateStartTerm.plusDays(4);
            } else if (dayOfWeek.equals(DayOfWeek.SATURDAY)) {
                startDate = localDateStartTerm.plusDays(3);
            } else if (dayOfWeek.equals(DayOfWeek.SUNDAY)) {
                startDate = localDateStartTerm.plusDays(2);
            }
        } else if (timeAndPlace.getDayOfTheWeek().equals(DayInWeek.wednesday)) {
            if (dayOfWeek.equals(DayOfWeek.MONDAY)) {
                startDate = localDateStartTerm.plusDays(2);
            } else if (dayOfWeek.equals(DayOfWeek.TUESDAY)) {
                startDate = localDateStartTerm.plusDays(1);
            } else if (dayOfWeek.equals(DayOfWeek.WEDNESDAY)) {
                startDate = localDateStartTerm.plusDays(0);
            } else if (dayOfWeek.equals(DayOfWeek.THURSDAY)) {
                startDate = localDateStartTerm.plusDays(6);
            } else if (dayOfWeek.equals(DayOfWeek.FRIDAY)) {
                startDate = localDateStartTerm.plusDays(5);
            } else if (dayOfWeek.equals(DayOfWeek.SATURDAY)) {
                startDate = localDateStartTerm.plusDays(4);
            } else if (dayOfWeek.equals(DayOfWeek.SUNDAY)) {
                startDate = localDateStartTerm.plusDays(3);
            }
        } else if (timeAndPlace.getDayOfTheWeek().equals(DayInWeek.thursday)) {
            if (dayOfWeek.equals(DayOfWeek.MONDAY)) {
                startDate = localDateStartTerm.plusDays(3);
            } else if (dayOfWeek.equals(DayOfWeek.TUESDAY)) {
                startDate = localDateStartTerm.plusDays(2);
            } else if (dayOfWeek.equals(DayOfWeek.WEDNESDAY)) {
                startDate = localDateStartTerm.plusDays(1);
            } else if (dayOfWeek.equals(DayOfWeek.THURSDAY)) {
                startDate = localDateStartTerm.plusDays(0);
            } else if (dayOfWeek.equals(DayOfWeek.FRIDAY)) {
                startDate = localDateStartTerm.plusDays(6);
            } else if (dayOfWeek.equals(DayOfWeek.SATURDAY)) {
                startDate = localDateStartTerm.plusDays(5);
            } else if (dayOfWeek.equals(DayOfWeek.SUNDAY)) {
                startDate = localDateStartTerm.plusDays(4);
            }
        } else if (timeAndPlace.getDayOfTheWeek().equals(DayInWeek.friday)) {
            if (dayOfWeek.equals(DayOfWeek.MONDAY)) {
                startDate = localDateStartTerm.plusDays(4);
            } else if (dayOfWeek.equals(DayOfWeek.TUESDAY)) {
                startDate = localDateStartTerm.plusDays(3);
            } else if (dayOfWeek.equals(DayOfWeek.WEDNESDAY)) {
                startDate = localDateStartTerm.plusDays(2);
            } else if (dayOfWeek.equals(DayOfWeek.THURSDAY)) {
                startDate = localDateStartTerm.plusDays(1);
            } else if (dayOfWeek.equals(DayOfWeek.FRIDAY)) {
                startDate = localDateStartTerm.plusDays(0);
            } else if (dayOfWeek.equals(DayOfWeek.SATURDAY)) {
                startDate = localDateStartTerm.plusDays(6);
            } else if (dayOfWeek.equals(DayOfWeek.SUNDAY)) {
                startDate = localDateStartTerm.plusDays(5);
            }
        } else if (timeAndPlace.getDayOfTheWeek().equals(DayInWeek.saturday)) {
            if (dayOfWeek.equals(DayOfWeek.MONDAY)) {
                startDate = localDateStartTerm.plusDays(5);
            } else if (dayOfWeek.equals(DayOfWeek.TUESDAY)) {
                startDate = localDateStartTerm.plusDays(4);
            } else if (dayOfWeek.equals(DayOfWeek.WEDNESDAY)) {
                startDate = localDateStartTerm.plusDays(3);
            } else if (dayOfWeek.equals(DayOfWeek.THURSDAY)) {
                startDate = localDateStartTerm.plusDays(2);
            } else if (dayOfWeek.equals(DayOfWeek.FRIDAY)) {
                startDate = localDateStartTerm.plusDays(1);
            } else if (dayOfWeek.equals(DayOfWeek.SATURDAY)) {
                startDate = localDateStartTerm.plusDays(0);
            } else if (dayOfWeek.equals(DayOfWeek.SUNDAY)) {
                startDate = localDateStartTerm.plusDays(6);
            }
        } else if (timeAndPlace.getDayOfTheWeek().equals(DayInWeek.sunday)) {
            if (dayOfWeek.equals(DayOfWeek.MONDAY)) {
                startDate = localDateStartTerm.plusDays(6);
            } else if (dayOfWeek.equals(DayOfWeek.TUESDAY)) {
                startDate = localDateStartTerm.plusDays(5);
            } else if (dayOfWeek.equals(DayOfWeek.WEDNESDAY)) {
                startDate = localDateStartTerm.plusDays(4);
            } else if (dayOfWeek.equals(DayOfWeek.THURSDAY)) {
                startDate = localDateStartTerm.plusDays(3);
            } else if (dayOfWeek.equals(DayOfWeek.FRIDAY)) {
                startDate = localDateStartTerm.plusDays(2);
            } else if (dayOfWeek.equals(DayOfWeek.SATURDAY)) {
                startDate = localDateStartTerm.plusDays(1);
            } else if (dayOfWeek.equals(DayOfWeek.SUNDAY)) {
                startDate = localDateStartTerm.plusDays(0);
            }
        }

        if(sectionClass.getSectionClassType().equals(SectionClassType.practice) && sectionClass.getRefId() != null){
            if(startDate != null){
                startDate = startDate.plusDays(14);
            }
        }

        List<Schedule> schedules = new ArrayList<>();
        if(totalSession > 0){
            for (int i = 0; i < totalSession; i++) {
                Schedule schedule = new Schedule();
                schedule.setTimeAndPlaceId(timeAndPlace.getId());
                schedule.setSectionClassId(sectionClass.getId());
                schedule.setScheduleType(ScheduleType.normal);
                schedule.setLecturerId(lecturer.getId());
                schedule.setRoom(timeAndPlace.getRoom());
                schedule.setDayOfTheWeek(timeAndPlace.getDayOfTheWeek());
                schedule.setPeriodStart(timeAndPlace.getPeriodStart());
                schedule.setPeriodEnd(timeAndPlace.getPeriodEnd());
                schedule.setNote(timeAndPlace.getNote());

                if(startDate != null){
                    schedule.setLearningDate(Date.from(Instant.from(startDate.atStartOfDay(ZoneId.systemDefault()).plusDays(i * 7L))));
                }

                schedule = scheduleRepository.saveAndFlush(schedule);

                if(schedule.getId() == null){
                    return new ArrayList<>();
                }
                schedules.add(schedule);
            }
        }

        return schedules;
    }


}
