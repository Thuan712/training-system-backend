package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.ScheduleDTO;
import iuh.fit.trainingsystembackend.enums.DayInWeek;
import iuh.fit.trainingsystembackend.model.Lecturer;
import iuh.fit.trainingsystembackend.model.Schedule;
import iuh.fit.trainingsystembackend.model.Section;
import iuh.fit.trainingsystembackend.model.UserEntity;
import iuh.fit.trainingsystembackend.repository.LecturerRepository;
import iuh.fit.trainingsystembackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;
import java.util.stream.Collectors;

@Service("scheduleMapper")
@AllArgsConstructor
public class ScheduleMapper {
    private LecturerRepository lecturerRepository;
    private UserRepository userRepository;

    public ScheduleDTO mapToDTO(Schedule schedule) {
        Section section = null;
        Lecturer lecturer = null;
        UserEntity userEntity = null;
        if (schedule.getSectionClass() != null) {
            section = schedule.getSectionClass().getSection();

            if (schedule.getSectionClass().getLecturerId() != null) {
                lecturer = lecturerRepository.findById(schedule.getSectionClass().getLecturerId()).orElse(null);

                if (lecturer != null) {
                    userEntity = userRepository.findById(lecturer.getUserId()).orElse(null);
                }
            }
        }

        return ScheduleDTO.builder()
                .id(schedule.getId())
                .termId(schedule.getSectionClass() != null && schedule.getSectionClass().getTerm() != null ? schedule.getSectionClass().getTerm().getId() : null)
                .termName(schedule.getSectionClass() != null && schedule.getSectionClass().getTerm() != null ? schedule.getSectionClass().getTerm().getName() : "")

                .lecturerId(lecturer != null ? lecturer.getId() : null)
                .lecturerName(userEntity != null ? userEntity.getFirstName() + " " + userEntity.getLastName() : "")

                .sectionId(section != null ? section.getId() : null)
                .sectionName(section != null ? section.getName() : "")
                .sectionCode(section != null ? section.getCode() : "")

                .sectionClassId(schedule.getSectionClass() != null ? schedule.getSectionClass().getId() : null)
                .sectionClassCode(schedule.getSectionClass() != null ? schedule.getSectionClass().getCode() : "")
                .numberOfStudents(schedule.getSectionClass() != null ? schedule.getSectionClass().getNumberOfStudents() : null)
                .note(schedule.getSectionClass() != null ? schedule.getSectionClass().getNote() : "")
                .sectionClassType(schedule.getSectionClass() != null ? schedule.getSectionClass().getSectionClassType().name() : "")

                .periodStart(schedule.getPeriodStart() != null ? schedule.getPeriodStart() : 0)
                .periodEnd(schedule.getPeriodEnd() != null ? schedule.getPeriodEnd() : 0)
                .dayInWeek(schedule.getDayOfTheWeek() != null ? schedule.getDayOfTheWeek() : null)
                .room(schedule.getRoom() != null  && !schedule.getRoom().isEmpty() ? schedule.getRoom() : "")

                .learningDate(schedule.getLearningDate())
                .scheduleType(schedule.getScheduleType())
                .build();
    }

    public List<ScheduleDTO> mapToDTO(List<Schedule> schedules) {
        return schedules.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<ScheduleDTO> mapToDTO(Page<Schedule> schedulePage) {
        return schedulePage.map(this::mapToDTO);
    }
}
