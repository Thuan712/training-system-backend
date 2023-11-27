package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.SpecializationDTO;
import iuh.fit.trainingsystembackend.dto.UserInfoDTO;
import iuh.fit.trainingsystembackend.enums.Gender;
import iuh.fit.trainingsystembackend.enums.SystemRole;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.FacultyRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("specializationMapper")
@AllArgsConstructor
public class SpecializationMapper {
    private FacultyRepository facultyRepository;
    public SpecializationDTO mapToDTO(Specialization specialization) {
        Faculty faculty = null;
        if(specialization.getFacultyId() != null){
            faculty = facultyRepository.findById(specialization.getFacultyId()).orElse(null);
        }

        return SpecializationDTO.builder()
                .id(specialization.getId())
                .facultyId(faculty != null ? faculty.getId() : null)
                .facultyName(faculty != null ? faculty.getName() : "")
                .name(specialization.getName())
                .code(specialization.getCode())
                .build();
    }

    public List<SpecializationDTO> mapToDTO(List<Specialization> specializations) {
        return specializations.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<SpecializationDTO> mapToDTO(Page<Specialization> specializationPage) {
        return specializationPage.map(this::mapToDTO);
    }
}
