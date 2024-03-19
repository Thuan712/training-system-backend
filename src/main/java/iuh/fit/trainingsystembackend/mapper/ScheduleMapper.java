package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.ScheduleDTO;
import iuh.fit.trainingsystembackend.model.Lecturer;
import iuh.fit.trainingsystembackend.model.Schedule;
import iuh.fit.trainingsystembackend.model.Section;
import iuh.fit.trainingsystembackend.model.UserEntity;
import iuh.fit.trainingsystembackend.repository.LecturerRepository;
import iuh.fit.trainingsystembackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

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
        String date = "";
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
                .sectionClassId(schedule.getSectionClass() != null ? schedule.getSectionClass().getId() : null)
                .lecturerId(lecturer != null ? lecturer.getId() : null)
                .lecturerName(userEntity != null ? userEntity.getFirstName() + " " + userEntity.getLastName() : "")
                .sectionId(section != null ? section.getId() : null)
                .sectionCode(section != null ? section.getCode() : "")
                .numberOfStudents(schedule.getSectionClass() != null ? schedule.getSectionClass().getNumberOfStudents() : null)
                .note(schedule.getSectionClass() != null ? schedule.getSectionClass().getNote() : "")
                .sectionClassType(schedule.getSectionClass() != null ? schedule.getSectionClass().getSectionClassType().name() : "")
                .learningDate(schedule.getLearningDate())
                .dateTime(date)
                .build();
    }

    public List<ScheduleDTO> mapToDTO(List<Schedule> schedules) {
        return schedules.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<ScheduleDTO> mapToDTO(Page<Schedule> schedulePage) {
        return schedulePage.map(this::mapToDTO);
    }
}
