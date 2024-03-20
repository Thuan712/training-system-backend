package iuh.fit.trainingsystembackend.service;

import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.model.SectionClass;
import iuh.fit.trainingsystembackend.model.Student;
import iuh.fit.trainingsystembackend.model.StudentSectionClass;
import iuh.fit.trainingsystembackend.model.Term;
import iuh.fit.trainingsystembackend.repository.StudentRepository;
import iuh.fit.trainingsystembackend.repository.StudentSectionClassRepository;
import iuh.fit.trainingsystembackend.request.DebtRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class DebtService {
    private StudentSectionClassRepository studentSectionClassRepository;
    private StudentRepository studentRepository;
    public List<Map<String, Object>> getDebtList(Long studentId, DebtRequest request){
        Student student = studentRepository.findById(studentId).orElse(null);

        if(student == null){
            throw new ValidationException("Không tìm thấy sinh viên này !!");
        }

        List<StudentSectionClass> studentSectionClasses = studentSectionClassRepository.findByStudentId(student.getId());

        if(!studentSectionClasses.isEmpty()){
            List<Map<String, Object>> resultList = new ArrayList<>();
            for (StudentSectionClass studentSectionClass : studentSectionClasses){
                Map<String, Object> result = new HashMap<>();

                SectionClass sectionClass = studentSectionClass.getSectionClass();

                if(sectionClass != null){
                    // Học kì đăng ký
                    result.put("termId", sectionClass.getTerm().getId());
                    result.put("termName", sectionClass.getTerm().getName());

                    // Học phần của lớp đăng ký
                    result.put("sectionId", sectionClass.getSection().getId());
                    result.put("sectionName", sectionClass.getSection().getName());
                    result.put("sectionCode", sectionClass.getSection().getCode());
                    result.put("credits", sectionClass.getSection().getName());
                    result.put("costCredits", sectionClass.getSection().getName());

                    // Mức phí ban đầu
                    double initialFee = 0D;
                    initialFee = sectionClass.getSection().getCostCredits() * sectionClass.getTerm().getCostPerCredit();
                    result.put("initialFee", initialFee);


                }

                resultList.add(result);
            }
        }

        return new ArrayList<>();
    }
}
