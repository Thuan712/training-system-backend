package iuh.fit.trainingsystembackend.service;

import iuh.fit.trainingsystembackend.bean.ProgramTermBean;
import iuh.fit.trainingsystembackend.enums.CourseType;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
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

        if(data.getTermType() == null){
            return null;
        }

        toSave.setMinimumElective(data.getMinimumElective() != null ? data.getMinimumElective() : 0);

        if(!data.getProgramElectiveCourses().isEmpty() && data.getMinimumElective() != null && data.getMinimumElective() != 0){

            int totalElectiveCredits = 0;
            for(Course course : data.getProgramElectiveCourses()){
                totalElectiveCredits += course.getCredits();
            }

            if(totalElectiveCredits < data.getMinimumElective() || totalElectiveCredits % data.getMinimumElective() != 0){
                throw new ValidationException("Số tín chỉ tự chọn tối thiểu phải bằng 1 hoặc nhiều tín chỉ cộng lại trong số môn tự chọn !!");
            }
        }

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

        if(!data.getProgramCompulsoryCourses().isEmpty()){
            for(Course course : data.getProgramCompulsoryCourses()) {
                ProgramCourse programCourse = new ProgramCourse();
                programCourse.setProgramTermId(toSave.getId());
                programCourse.setCourseId(course.getId());

                programCourse = programCourseRepository.saveAndFlush(programCourse);
                if(programCourse.getId() == null){
                    return null;
                }
            }
        }

        if(!data.getProgramElectiveCourses().isEmpty()){
            for(Course course : data.getProgramElectiveCourses()) {
                ProgramCourse programCourse = new ProgramCourse();
                programCourse.setProgramTermId(toSave.getId());
                programCourse.setCourseId(course.getId());

                programCourse = programCourseRepository.saveAndFlush(programCourse);
                if(programCourse.getId() == null){
                    return null;
                }
            }
        }

        return toSave;
    }
}
