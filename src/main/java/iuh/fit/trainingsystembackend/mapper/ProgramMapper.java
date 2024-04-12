package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.data.CourseDuration;
import iuh.fit.trainingsystembackend.data.RequireCourse;
import iuh.fit.trainingsystembackend.dto.ProgramDTO;
import iuh.fit.trainingsystembackend.dto.ProgramTermDTO;
import iuh.fit.trainingsystembackend.dto.SectionDTO;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.AcademicYearRepository;
import iuh.fit.trainingsystembackend.repository.SpecializationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("programMapper")
@AllArgsConstructor
public class ProgramMapper {
    private SpecializationRepository specializationRepository;
    private AcademicYearRepository academicYearRepository;
    private ProgramTermRepository programTermRepository;
    private ProgramTermMapper programTermMapper;
    public ProgramDTO mapToDTO(Program program) {
        Specialization specialization = specializationRepository.findById(program.getSpecializationId()).orElse(null);
        AcademicYear academicYear = academicYearRepository.findById(program.getAcademicYearId()).orElse(null);
        String academicYearName = "";
        if(academicYear != null){
            int yearEnd = academicYear.getYearStart() + 1;
            academicYearName += academicYear.getYearStart() + "-" + yearEnd;
        }

        List<ProgramTerm> programTerms = programTermRepository.findByProgramId(program.getId());
        List<ProgramTermDTO> programTermDTOS;
        if(!programTerms.isEmpty()){
            programTermDTOS = programTermMapper.mapToDTO(programTerms);
        } else {
            programTermDTOS = new ArrayList<>();
        }

        return ProgramDTO.builder()
                .id(program.getId())
                .name(program.getName())

                .specializationId(program.getSpecializationId())
                .specializationName(specialization != null ? specialization.getName() : "")
                .specializationCode(specialization != null ? specialization.getCode() : "")

                .academicYearId(program.getAcademicYearId())
                .academicYearName(academicYearName)

                .createdAt(program.getCreatedAt())
                .updatedAt(program.getUpdatedAt())
                .deletedAt(program.getDeletedAt())
                .deleted(program.getDeleted())

                .programTerms(programTermDTOS)
                .build();
    }
    public List<ProgramDTO> mapToDTO(List<Program> programList) {
        return programList.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<ProgramDTO> mapToDTO(Page<Program> programPage) {
        return programPage.map(this::mapToDTO);
    }
}
