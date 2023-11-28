package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.SpecializationClassDTO;
import iuh.fit.trainingsystembackend.model.AcademicYear;
import iuh.fit.trainingsystembackend.model.Specialization;
import iuh.fit.trainingsystembackend.model.SpecializationClass;
import iuh.fit.trainingsystembackend.repository.AcademicYearRepository;
import iuh.fit.trainingsystembackend.repository.SpecializationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("specializationClassMapper")
@AllArgsConstructor
public class SpecializationClassMapper {
    private SpecializationRepository specializationRepository;

    public SpecializationClassDTO mapToDTO(SpecializationClass specializationClass) {

        Specialization specialization = null;
        if(specializationClass.getSpecializationId() != null){
            specialization = specializationRepository.findById(specializationClass.getSpecializationId()).orElse(null);
        }

        return SpecializationClassDTO.builder()
                .id(specializationClass.getId())
                .specializationId(specialization != null ? specialization.getId() : null)
                .specializationName(specialization != null ? specialization.getName() : "")
                .specializationCode(specialization != null ? specialization.getCode() : "")
                .schoolYear(specializationClass.getSchoolYear())
                .name(specializationClass.getName())
                .build();
    }

    public List<SpecializationClassDTO> mapToDTO(List<SpecializationClass> specializationClasses) {
        return specializationClasses.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<SpecializationClassDTO> mapToDTO(Page<SpecializationClass> specializationClassPage) {
        return specializationClassPage.map(this::mapToDTO);
    }
}
