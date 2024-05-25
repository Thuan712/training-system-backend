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
        List<CourseDTO> compulsoryCourseDTO;
        List<CourseDTO> electiveCourseDTO;

        List<ProgramCourse> programCourses = programCourseRepository.findByProgramTermId(programTerm.getId());
        List<Course> compulsoryCourses = new ArrayList<>();
        List<Course> electiveCourses = new ArrayList<>();

        if(!programCourses.isEmpty()){
            for(ProgramCourse programCourse : programCourses){
                if(programCourse.getCourse().getCourseType().equals(CourseType.elective)){
                    totalElective += programCourse.getCourse().getCredits();
                    electiveCourses.add(programCourse.getCourse());

                } else if(programCourse.getCourse().getCourseType().equals(CourseType.compulsory)){
                    totalCompulsory += programCourse.getCourse().getCredits();
                    compulsoryCourses.add(programCourse.getCourse());
                }
            }

            compulsoryCourseDTO = courseMapper.mapToDTO(compulsoryCourses);
            electiveCourseDTO = courseMapper.mapToDTO(electiveCourses);
        } else {
            compulsoryCourseDTO = new ArrayList<>();
            electiveCourseDTO =  new ArrayList<>();
        }



        return ProgramTermDTO.builder()
                .id(programTerm.getId())
                .programId(programTerm.getProgramId())
                .minimumElective(programTerm.getMinimumElective())

                .totalCompulsory(totalCompulsory)
                .totalElective(totalElective)

                .termType(programTerm.getTermType())

                .programCompulsoryCourses(compulsoryCourseDTO)
                .programElectiveCourses(electiveCourseDTO)
                .build();
    }
    public List<ProgramTermDTO> mapToDTO(List<ProgramTerm> programTermList) {
        return programTermList.parallelStream().map(this::mapToDTO).collect(Collectors.toList());   
    }

    public Page<ProgramTermDTO> mapToDTO(Page<ProgramTerm> programTermPage) {
        return programTermPage.map(this::mapToDTO);
    }
}
