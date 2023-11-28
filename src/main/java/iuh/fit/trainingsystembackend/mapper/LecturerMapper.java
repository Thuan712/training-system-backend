package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.LecturerDTO;
import iuh.fit.trainingsystembackend.dto.StudentDTO;
import iuh.fit.trainingsystembackend.enums.Position;
import iuh.fit.trainingsystembackend.enums.Title;
import iuh.fit.trainingsystembackend.model.Lecturer;
import iuh.fit.trainingsystembackend.model.Student;
import iuh.fit.trainingsystembackend.model.UserEntity;
import iuh.fit.trainingsystembackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("lecturerMapper")
@AllArgsConstructor
public class LecturerMapper {
    private UserRepository userRepository;
    public LecturerDTO mapToDTO(Lecturer lecturer) {
        UserEntity userEntity = userRepository.findById(lecturer.getUserId()).orElse(null);

        return LecturerDTO.builder()
                .id(lecturer.getId())
                .userId(userEntity != null ? userEntity.getId() : null)
                .name(userEntity != null ? userEntity.getFirstName() + " " + userEntity.getLastName() : "")
                .code(userEntity != null ? userEntity.getCode() : "")
                .specializationId(lecturer.getSpecializationId())
                .title(lecturer.getTitle())
                .position(lecturer.getPosition())
                .entryDate(lecturer.getEntryDate())
                .build();
    }

    public List<LecturerDTO> mapToDTO(List<Lecturer> lecturers) {
        return lecturers.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<LecturerDTO> mapToDTO(Page<Lecturer> lecturerPage) {
        return lecturerPage.map(this::mapToDTO);
    }
}
