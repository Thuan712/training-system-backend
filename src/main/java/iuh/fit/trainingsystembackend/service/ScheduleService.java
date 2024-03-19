package iuh.fit.trainingsystembackend.service;

import iuh.fit.trainingsystembackend.enums.DayInWeek;
import iuh.fit.trainingsystembackend.model.Schedule;
import iuh.fit.trainingsystembackend.model.SectionClass;
import iuh.fit.trainingsystembackend.model.Term;
import iuh.fit.trainingsystembackend.model.TimeAndPlace;
import iuh.fit.trainingsystembackend.repository.ScheduleRepository;
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

    public List<Schedule> createSchedules(SectionClass sectionClass, TimeAndPlace timeAndPlace, int totalSession) {
        if (sectionClass == null || timeAndPlace == null) {
            return new ArrayList<>();
        }

        Term term = termRepository.findById(sectionClass.getTermId()).orElse(null);

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
                startDate = localDateStartTerm.plusDays(dayOfWeek.getValue());
            } else if (dayOfWeek.equals(DayOfWeek.TUESDAY)) {
                startDate = localDateStartTerm.plusDays(1 - dayOfWeek.getValue());
            } else if (dayOfWeek.equals(DayOfWeek.WEDNESDAY)) {
                startDate = localDateStartTerm.plusDays(2 - dayOfWeek.getValue());
            } else if (dayOfWeek.equals(DayOfWeek.THURSDAY)) {
                startDate = localDateStartTerm.plusDays(3 - dayOfWeek.getValue());
            } else if (dayOfWeek.equals(DayOfWeek.FRIDAY)) {
                startDate = localDateStartTerm.plusDays(4 - dayOfWeek.getValue());
            } else if (dayOfWeek.equals(DayOfWeek.SATURDAY)) {
                startDate = localDateStartTerm.plusDays(5 - dayOfWeek.getValue());
            }
        } else if (timeAndPlace.getDayOfTheWeek().equals(DayInWeek.tuesday)) {
            if (dayOfWeek.equals(DayOfWeek.MONDAY)) {
                startDate = localDateStartTerm.plusDays(dayOfWeek.getValue() + 1);
            } else if (dayOfWeek.equals(DayOfWeek.TUESDAY)) {
                startDate = localDateStartTerm.plusDays(dayOfWeek.getValue());
            } else if (dayOfWeek.equals(DayOfWeek.WEDNESDAY)) {
                startDate = localDateStartTerm.plusDays(1 - dayOfWeek.getValue());
            } else if (dayOfWeek.equals(DayOfWeek.THURSDAY)) {
                startDate = localDateStartTerm.plusDays(2 - dayOfWeek.getValue());
            } else if (dayOfWeek.equals(DayOfWeek.FRIDAY)) {
                startDate = localDateStartTerm.plusDays(3 - dayOfWeek.getValue());
            } else if (dayOfWeek.equals(DayOfWeek.SATURDAY)) {
                startDate = localDateStartTerm.plusDays(4 - dayOfWeek.getValue());
            }
        } else if (timeAndPlace.getDayOfTheWeek().equals(DayInWeek.wednesday)) {
            if (dayOfWeek.equals(DayOfWeek.MONDAY)) {
                startDate = localDateStartTerm.plusDays(dayOfWeek.getValue() + 2);
            } else if (dayOfWeek.equals(DayOfWeek.TUESDAY)) {
                startDate = localDateStartTerm.plusDays(dayOfWeek.getValue() + 1);
            } else if (dayOfWeek.equals(DayOfWeek.WEDNESDAY)) {
                startDate = localDateStartTerm.plusDays(dayOfWeek.getValue());
            } else if (dayOfWeek.equals(DayOfWeek.THURSDAY)) {
                startDate = localDateStartTerm.plusDays(1 - dayOfWeek.getValue());
            } else if (dayOfWeek.equals(DayOfWeek.FRIDAY)) {
                startDate = localDateStartTerm.plusDays(2 - dayOfWeek.getValue());
            } else if (dayOfWeek.equals(DayOfWeek.SATURDAY)) {
                startDate = localDateStartTerm.plusDays(3 - dayOfWeek.getValue());
            }
        } else if (timeAndPlace.getDayOfTheWeek().equals(DayInWeek.thursday)) {
            if (dayOfWeek.equals(DayOfWeek.MONDAY)) {
                startDate = localDateStartTerm.plusDays(dayOfWeek.getValue() + 3);
            } else if (dayOfWeek.equals(DayOfWeek.TUESDAY)) {
                startDate = localDateStartTerm.plusDays(dayOfWeek.getValue() + 2);
            } else if (dayOfWeek.equals(DayOfWeek.WEDNESDAY)) {
                startDate = localDateStartTerm.plusDays(dayOfWeek.getValue() + 1);
            } else if (dayOfWeek.equals(DayOfWeek.THURSDAY)) {
                startDate = localDateStartTerm.plusDays(dayOfWeek.getValue());
            } else if (dayOfWeek.equals(DayOfWeek.FRIDAY)) {
                startDate = localDateStartTerm.plusDays(1 - dayOfWeek.getValue());
            } else if (dayOfWeek.equals(DayOfWeek.SATURDAY)) {
                startDate = localDateStartTerm.plusDays(2 - dayOfWeek.getValue());
            }
        } else if (timeAndPlace.getDayOfTheWeek().equals(DayInWeek.friday)) {
            if (dayOfWeek.equals(DayOfWeek.MONDAY)) {
                startDate = localDateStartTerm.plusDays(dayOfWeek.getValue() + 4);
            } else if (dayOfWeek.equals(DayOfWeek.TUESDAY)) {
                startDate = localDateStartTerm.plusDays(dayOfWeek.getValue() + 3);
            } else if (dayOfWeek.equals(DayOfWeek.WEDNESDAY)) {
                startDate = localDateStartTerm.plusDays(dayOfWeek.getValue() + 2);
            } else if (dayOfWeek.equals(DayOfWeek.THURSDAY)) {
                startDate = localDateStartTerm.plusDays(dayOfWeek.getValue() + 1);
            } else if (dayOfWeek.equals(DayOfWeek.FRIDAY)) {
                startDate = localDateStartTerm.plusDays(1 - dayOfWeek.getValue());
            } else if (dayOfWeek.equals(DayOfWeek.SATURDAY)) {
                startDate = localDateStartTerm.plusDays(2 - dayOfWeek.getValue());
            }
        } else if (timeAndPlace.getDayOfTheWeek().equals(DayInWeek.saturday)) {
            if (dayOfWeek.equals(DayOfWeek.MONDAY)) {
                startDate = localDateStartTerm.plusDays(dayOfWeek.getValue() + 5);
            } else if (dayOfWeek.equals(DayOfWeek.TUESDAY)) {
                startDate = localDateStartTerm.plusDays(dayOfWeek.getValue() + 4);
            } else if (dayOfWeek.equals(DayOfWeek.WEDNESDAY)) {
                startDate = localDateStartTerm.plusDays(dayOfWeek.getValue() + 3);
            } else if (dayOfWeek.equals(DayOfWeek.THURSDAY)) {
                startDate = localDateStartTerm.plusDays(dayOfWeek.getValue() + 2);
            } else if (dayOfWeek.equals(DayOfWeek.FRIDAY)) {
                startDate = localDateStartTerm.plusDays(dayOfWeek.getValue() + 1);
            } else if (dayOfWeek.equals(DayOfWeek.SATURDAY)) {
                startDate = localDateStartTerm.plusDays(dayOfWeek.getValue());
            }
        }

        List<Schedule> schedules = new ArrayList<>();
        for (int i = 0; i < totalSession; i++) {
            Schedule schedule = new Schedule();

            schedule.setTimeAndPlaceId(timeAndPlace.getId());
            schedule.setSectionClassId(sectionClass.getId());

            if(startDate != null){
                schedule.setLearningDate(Date.from(Instant.from(startDate.plusDays(i * 7L).atStartOfDay(ZoneId.of("GMT")))));
            }

            schedule = scheduleRepository.saveAndFlush(schedule);

            if(schedule.getId() == null){
                return new ArrayList<>();
            }
            schedules.add(schedule);
        }

        return schedules;
    }
}
