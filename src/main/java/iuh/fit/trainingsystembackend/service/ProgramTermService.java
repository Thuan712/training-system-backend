package iuh.fit.trainingsystembackend.service;

import iuh.fit.trainingsystembackend.bean.ProgramTermBean;
import iuh.fit.trainingsystembackend.enums.CourseType;
import iuh.fit.trainingsystembackend.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProgramTermService {

    private final ProgramTermRepository programTermRepository;
    private final ProgramCourseRepository programCourseRepository;

    public ProgramTerm createOrUpdateProgramTerm (Long programId, ProgramTermBean data){
        ProgramTerm toSave = null;

        if(data.getId() != null){
            toSave =  programTermRepository.findById(data.getId()).orElse(null);

            if(toSave == null){
                return null;
            }
        }

        if(toSave == null){
            toSave = new ProgramTerm();
        }

        toSave.setProgramId(programId);
        if(data.getProgramCourses().isEmpty()){
            return null;
        }

        if(data.getTermType() == null){
            return null;
        }

        toSave.setMinimumElective(data.getMinimumElective() != null ? data.getMinimumElective() : 0);
        toSave.setTermType(data.getTermType());
        toSave = programTermRepository.saveAndFlush(toSave);
        if(toSave.getId() == null){
            return null;
        }

        try {
            List<ProgramCourse> programCourses = programCourseRepository.findByProgramTermId(toSave.getId());

            if(!programCourses.isEmpty()){
                programCourseRepository.deleteAll(programCourses);
            }
        } catch (Exception ignored){}

        for(Course course : data.getProgramCourses()) {
            ProgramCourse programCourse = new ProgramCourse();
            programCourse.setProgramTermId(toSave.getId());
            programCourse.setCourseId(course.getId());

            programCourse = programCourseRepository.saveAndFlush(programCourse);
            if(programCourse.getId() == null){
                return null;
            }
        }

        return toSave;
    }
}
