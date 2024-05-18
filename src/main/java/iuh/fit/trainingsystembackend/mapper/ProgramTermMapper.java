package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.CourseDTO;
import iuh.fit.trainingsystembackend.dto.ProgramDTO;
import iuh.fit.trainingsystembackend.dto.ProgramTermDTO;
import iuh.fit.trainingsystembackend.enums.CourseType;
import iuh.fit.trainingsystembackend.model.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("programTermMapper")
@AllArgsConstructor
public class ProgramTermMapper {

    private final ProgramCourseRepository programCourseRepository;
    private CourseMapper courseMapper;
    public ProgramTermDTO mapToDTO(ProgramTerm programTerm) {
        int totalElective = 0;
        int totalCompulsory = 0;
        List<CourseDTO> courses;

        List<ProgramCourse> programCourses = programCourseRepository.findByProgramTermId(programTerm.getId());

        if(!programCourses.isEmpty()){
            for(ProgramCourse programCourse : programCourses){
                if(programCourse.getCourse().getCourseType().equals(CourseType.elective)){
                    totalElective += programCourse.getCourse().getCredits();
                } else if(programCourse.getCourse().getCourseType().equals(CourseType.compulsory)){
                    totalCompulsory += programCourse.getCourse().getCredits();
                }
            }

            courses = courseMapper.mapToDTO(programCourses.stream().map(ProgramCourse::getCourse).collect(Collectors.toList()));
        } else {
            courses = new ArrayList<>();
        }



        return ProgramTermDTO.builder()
                .id(programTerm.getId())
                .programId(programTerm.getProgramId())
                .minimumElective(programTerm.getMinimumElective())

                .totalCompulsory(totalCompulsory)
                .totalElective(totalElective)

                .termType(programTerm.getTermType())

                .programCourses(courses)
                .build();
    }
    public List<ProgramTermDTO> mapToDTO(List<ProgramTerm> programTermList) {
        return programTermList.parallelStream().map(this::mapToDTO).collect(Collectors.toList());   
    }

    public Page<ProgramTermDTO> mapToDTO(Page<ProgramTerm> programTermPage) {
        return programTermPage.map(this::mapToDTO);
    }
}
