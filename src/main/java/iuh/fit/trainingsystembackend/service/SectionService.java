package iuh.fit.trainingsystembackend.service;

import iuh.fit.trainingsystembackend.bean.SectionBean;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.model.Course;
import iuh.fit.trainingsystembackend.model.Section;
import iuh.fit.trainingsystembackend.model.Term;
import iuh.fit.trainingsystembackend.model.Tuition;
import iuh.fit.trainingsystembackend.repository.*;
import iuh.fit.trainingsystembackend.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
@AllArgsConstructor
public class SectionService {
    private SectionRepository sectionRepository;
    private CourseRepository courseRepository;
    private ResultRepository resultRepository;
    private StudentRepository studentRepository;
    private SectionClassRepository sectionClassRepository;
    private TuitionRepository tuitionRepository;
    private final TermRepository termRepository;

    public Section createOrUpdateSection(SectionBean data){
        //#region Create Or Update Section
        Section toSave = null;

        if (data.getId() != null) {
            toSave = sectionRepository.findById(data.getId()).orElse(null);

            if (toSave == null) {
                throw new ValidationException("Không tìm thấy học phần !!");
            }
        }

        if (data.getCourseId() == null) {
            throw new ValidationException("Học phần phải bao gồm thuộc một môn học nào đó !!");
        }
        Course course = courseRepository.findById(data.getCourseId()).orElse(null);

        if (course == null) {
            throw new ValidationException("Không tìm thấy môn học có ID là " + data.getCourseId() + "!!");
        }

        if (data.getTermId() == null) {
            throw new ValidationException("Học kỳ của học phần không được để trống !!");
        }
        Term term = termRepository.findById(data.getTermId()).orElse(null);

        if (term == null) {
            throw new ValidationException("Không tìm thấy học kỳ này !!");
        }

        if (toSave == null) {
            toSave = new Section();
            boolean isExist = true;
            String code = "";

            while (isExist) {
                code = course.getCode() + StringUtils.randomNumberGenerate(4);
                Section section = sectionRepository.findSectionByCode(code);

                isExist = section != null;
            }

            toSave.setCode(code);
        }

        toSave.setCourseId(data.getCourseId());
        toSave.setTermId(term.getId());

        toSave.setName(course.getName());
        toSave.setDescription(data.getDescription());

        toSave = sectionRepository.saveAndFlush(toSave);
        if (toSave.getId() == null) {
            return null;
        }

        //#endregion

        //#region Create Or Update Tuition Fee
        Tuition tuitionSave = tuitionRepository.findBySectionId(toSave.getId());

        if(tuitionSave == null){
            tuitionSave = new Tuition();
            tuitionSave.setSectionId(toSave.getId());
        }

        double initialFee = term.getCostPerCredit() * course.getCostCredits();
        double discountFee = initialFee * data.getDiscountAmount();

        tuitionSave.setInitialFee(initialFee);
        tuitionSave.setDiscountAmount(data.getDiscountAmount());

        if(discountFee == data.getDiscountFee()){
            tuitionSave.setDiscountFee(data.getDiscountFee());
        } else {
            tuitionSave.setDiscountFee(discountFee);
        }

        // Hạn nộp của học phí là 2 tháng trước khi bắt đầu kì
        LocalDate localDate = term.getTermStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        localDate = localDate.plusMonths(2);

        tuitionSave.setPaymentDeadline(Date.from(Instant.from(localDate.atStartOfDay(ZoneId.of("GMT")))));

        tuitionSave = tuitionRepository.saveAndFlush(tuitionSave);

        if(tuitionSave.getId() == null){
            return null;
        }
        //#endregion

        return toSave;
    }
}
