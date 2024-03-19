package iuh.fit.trainingsystembackend.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import iuh.fit.trainingsystembackend.dto.SectionDTO;
import iuh.fit.trainingsystembackend.model.Course;
import iuh.fit.trainingsystembackend.model.Section;
import iuh.fit.trainingsystembackend.model.Specialization;
import iuh.fit.trainingsystembackend.model.Term;
import iuh.fit.trainingsystembackend.repository.CourseRepository;
import iuh.fit.trainingsystembackend.repository.SpecializationRepository;
import iuh.fit.trainingsystembackend.repository.TermRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("sectionMapper")
@AllArgsConstructor
public class SectionMapper {
    private SpecializationRepository specializationRepository;
    public SectionDTO mapToDTO(Section section) {

        Specialization specialization = null;
        if(section.getSpecializationId() != null){
            specialization = specializationRepository.findById(section.getSpecializationId()).orElse(null);
        }


//        if(section.getRequireSection().getPrerequisite() != null && !section.getRequireSection().getPrerequisite().isEmpty()){
//            for(String sectionId : section.getRequireSection().getPrerequisite()){
//
//            }
//        }

        return SectionDTO.builder()
                .id(section.getId())
                .specializationId(section.getSpecializationId())
                .specializationName(specialization != null ? specialization.getName() : "")
                .specializationCode(specialization != null ? specialization.getCode() : "")
                .courseIds(section.getCourseIds())
                .name(section.getName())
                .description(section.getDescription())
                .code(section.getCode())
                .theory(section.getSectionDuration().getTheory())
                .practice(section.getSectionDuration().getPractice())
                .discussionExercises(section.getSectionDuration().getDiscussionExercises())
                .selfLearning(section.getSectionDuration().getSelfLearning())
                .termRegister(section.getTermRegister())
                .credits(section.getCredits())
                .costCredits(section.getCostCredits())
                .sectionType(section.getSectionType())
                .requireSection(section.getRequireSection())
                .sectionDuration(section.getSectionDuration())
                .createdAt(section.getCreatedAt())
                .updatedAt(section.getUpdatedAt())
                .deletedAt(section.getDeletedAt())
                .deleted(section.getDeleted())
                .build();
    }

    public List<SectionDTO> mapToDTO(List<Section> sectionList) {
        return sectionList.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<SectionDTO> mapToDTO(Page<Section> sectionPage) {
        return sectionPage.map(this::mapToDTO);
    }
}
