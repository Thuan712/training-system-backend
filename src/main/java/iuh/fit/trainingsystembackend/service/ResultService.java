package iuh.fit.trainingsystembackend.service;

import iuh.fit.trainingsystembackend.bean.ResultBean;
import iuh.fit.trainingsystembackend.bean.SectionClassBean;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ResultService {
    private ResultRepository resultRepository;
    private SectionRepository sectionRepository;
    private LecturerRepository lecturerRepository;
    private SectionClassRepository sectionClassRepository;
    private CourseRepository courseRepository;
    private StudentRepository studentRepository;
    private UserRepository userRepository;
    public List<Result> enterScoresForStudent(Long lecturerId, SectionClassBean data){

        // Lecturer
        if(lecturerId == null){
            throw new ValidationException("Lecturer ID is required !!");
        }

        Lecturer lecturer = lecturerRepository.findById(lecturerId).orElse(null);

        if(lecturer == null){
            throw new ValidationException("Lecturer is not found !!");
        }

        // Section
        if(data.getSectionId() == null){
            throw new ValidationException("Section ID is required !!");
        }

        Section section = sectionRepository.findById(data.getSectionId()).orElse(null);

        if(section == null){
            throw new ValidationException("Section is not found !!");
        }

        if(!isInstructorSectionClass(lecturer.getId(), data.getId())){
            throw new ValidationException("You aren't instructor this section class !!");
        }

        List<Result> toSaveResults = new ArrayList<>();
//
//        if(!data.getResults().isEmpty()){
//            boolean isFail = false;
//            for (Result result : data.getResults()){
//                //TODO: Ham input diem tung sinh vien
//                try{
//                    Result toSave = createOrUpdateResult(result);
//
//                    if(toSave.getId() == null){
//                        throw new ValidationException("Create Student " + result.getStudentCode() + " fail !!");
//                    }
//
//                    toSaveResults.add(result);
//                } catch (Exception e){
//                    isFail = true;
//                }
//            }
//
//            if(isFail){
//                throw new ValidationException("Create Or Update Result for students fail !!");
//            }
//        }
//
//        if(toSaveResults.isEmpty()){
//            return new ArrayList<>();
//        }

        return toSaveResults;
    }

    private Result createOrUpdateResult(Result data){
        Result toSave = null;
        if(data.getId() != null){
            toSave = resultRepository.findById(data.getId()).orElse(null);
        }


        if(toSave == null){
            toSave = resultRepository.findResultBySectionIdAndStudentId(data.getSectionId(), data.getStudentId());

            if(toSave == null){
                toSave = new Result();

                if(data.getSectionId() == null){
                    throw new ValidationException("Section ID is not found !!");
                }

                Section section = data.getSection();

                if(section == null){
                    throw new ValidationException("Section is not found !!");
                }

                toSave.setSectionId(section.getId());

                if(data.getStudentId() == null){
                    throw new ValidationException("Student ID is not found !!");
                }

                Student student = data.getStudent();

                if(student == null){
                    throw new ValidationException("Student is not found !!");
                }

                toSave.setStudentId(student.getId());

                UserEntity userEntity = userRepository.findById(student.getUserId()).orElse(null);

                if(userEntity == null){
                    throw new ValidationException("User is not found !!");
                }

            }
        }

        // Regular Point
        if(data.getRegularPoint1() != null&& !data.getRegularPoint1().isNaN() && !data.getRegularPoint1().isInfinite()){
            if(data.getRegularPoint1() < 0 || data.getRegularPoint1() > 10){
                throw new ValidationException("Regular point column 1 is invalid !!");
            }

            toSave.setRegularPoint1(data.getRegularPoint1());
        }

        if(data.getRegularPoint2() != null && !data.getRegularPoint2().isNaN() && !data.getRegularPoint2().isInfinite()){
            if(data.getRegularPoint2() < 0 || data.getRegularPoint2() > 10){
                throw new ValidationException("Regular point column 2 is invalid !!");
            }

            toSave.setRegularPoint2(data.getRegularPoint2());
        }

        if(data.getRegularPoint3() != null && !data.getRegularPoint3().isNaN() && !data.getRegularPoint3().isInfinite()){
            if(data.getRegularPoint3() < 0 || data.getRegularPoint3() > 10){
                throw new ValidationException("Regular point column 3 is invalid !!");
            }

            toSave.setRegularPoint3(data.getRegularPoint3());
        }

        if(data.getRegularPoint4() != null && !data.getRegularPoint4().isNaN() && !data.getRegularPoint4().isInfinite()){
            if(data.getRegularPoint4() < 0 || data.getRegularPoint4() > 10){
                throw new ValidationException("Regular point column 4 is invalid !!");
            }

            toSave.setRegularPoint4(data.getRegularPoint4());
        }

        if(data.getRegularPoint5() != null && !data.getRegularPoint5().isNaN() && !data.getRegularPoint5().isInfinite()){
            if(data.getRegularPoint5() < 0 || data.getRegularPoint5() > 10){
                throw new ValidationException("Regular point column 5 is invalid !!");
            }

            toSave.setRegularPoint5(data.getRegularPoint5());
        }

        // Midterm Point
        if(data.getMidtermPoint1() != null && !data.getMidtermPoint1().isNaN() && !data.getMidtermPoint1().isInfinite()){
            if(data.getMidtermPoint1() < 0 || data.getMidtermPoint1() > 10){
                throw new ValidationException("Midterm point column 1 is invalid !!");
            }

            toSave.setMidtermPoint1(data.getMidtermPoint1());
        }

        if(data.getMidtermPoint2() != null && !data.getMidtermPoint2().isNaN() && !data.getMidtermPoint2().isInfinite()){
            if(data.getMidtermPoint2() < 0 || data.getMidtermPoint2() > 10){
                throw new ValidationException("Midterm point column 2 is invalid !!");
            }

            toSave.setMidtermPoint2(data.getMidtermPoint2());
        }

        if(data.getMidtermPoint3() != null && !data.getMidtermPoint3().isNaN() && !data.getMidtermPoint3().isInfinite()){
            if(data.getMidtermPoint3() < 0 || data.getMidtermPoint3() > 10){
                throw new ValidationException("Midterm point column 3 is invalid !!");
            }

            toSave.setMidtermPoint3(data.getMidtermPoint3());
        }

        // Final Point
        if(data.getFinalPoint() != null && !data.getFinalPoint().isNaN() && !data.getFinalPoint().isInfinite()){
            if(data.getFinalPoint() < 0 || data.getFinalPoint() > 10){
                throw new ValidationException("Final point is invalid !!");
            }

            toSave.setFinalPoint(data.getFinalPoint());
        }

        // Practice Point
        if(data.getPracticePoint1() != null && !data.getPracticePoint1().isNaN() && !data.getPracticePoint1().isInfinite()){
            if(data.getPracticePoint1() < 0 || data.getPracticePoint1() > 10){
                throw new ValidationException("Practice point column 1 is invalid !!");
            }

            toSave.setPracticePoint1(data.getPracticePoint1());
        }

        if(data.getPracticePoint2() != null && !data.getPracticePoint2().isNaN() && !data.getPracticePoint2().isInfinite()){
            if(data.getPracticePoint2() < 0 || data.getPracticePoint2() > 10){
                throw new ValidationException("Practice point column 2 is invalid !!");
            }

            toSave.setPracticePoint2(data.getPracticePoint2());
        }

        return toSave;
    }

    private boolean isInstructorSectionClass(Long lecturerId, Long sectionClassId){
        if(lecturerId == null){
            throw new ValidationException("Lecturer ID is required !!");
        }

        Lecturer lecturer = lecturerRepository.findById(lecturerId).orElse(null);

        if(lecturer == null){
            throw new ValidationException("Lecturer is not found !!");
        }

        if(sectionClassId == null){
            throw new ValidationException("Section Class ID is required !!");
        }

        SectionClass sectionClass = sectionClassRepository.findById(sectionClassId).orElse(null);

        if(sectionClass == null){
            throw new ValidationException("Section Class is not found !!");
        }

        return sectionClassRepository.existsSectionClassByLecturerIdAndSectionId(lecturer.getId(), sectionClass.getSectionId());
    }

}
