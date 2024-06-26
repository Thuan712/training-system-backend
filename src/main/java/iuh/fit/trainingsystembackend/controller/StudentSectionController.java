package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.bean.RegistrationSectionBean;
import iuh.fit.trainingsystembackend.bean.ResultBean;
import iuh.fit.trainingsystembackend.dto.RegistrationDTO;
import iuh.fit.trainingsystembackend.enums.*;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.mapper.RegistrationMapper;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.*;
import iuh.fit.trainingsystembackend.request.RegistrationRequest;
import iuh.fit.trainingsystembackend.specification.RegistrationSpecification;
import iuh.fit.trainingsystembackend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.REGISTRATION_ENDPOINT)
public class StudentSectionController {
    private RegistrationMapper registrationMapper;
    private RegistrationSpecification registrationSpecification;
    private StudentSectionRepository studentSectionRepository;
    private StudentRepository studentRepository;
    private TermRepository termRepository;
    private SectionRepository sectionRepository;
    private SectionClassRepository sectionClassRepository;
    private TimeAndPlaceRepository timeAndPlaceRepository;
    private StudentSectionClassRepository studentSectionClassRepository;
    private TuitionRepository tuitionRepository;
    private StudentTuitionRepository studentTuitionRepository;
    private final ResultRepository resultRepository;
    private final StudentCourseRepository studentCourseRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @PostMapping("/getPage")
    public ResponseEntity<?> getPage(@RequestParam(value = "userId", required = false) Long userId,
                                     @RequestParam("pageNumber") int pageNumber, @RequestParam("pageRows") int pageRows,
                                     @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                     @RequestParam(value = "sortOrder", required = false, defaultValue = "-1") int sortOrder,
                                     @RequestBody RegistrationRequest filterRequest) {
        Page<StudentSection> studentSections = studentSectionRepository.findAll(registrationSpecification.getFilter(filterRequest), PageRequest.of(pageNumber, pageRows, Sort.by(sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "id")));
        Page<RegistrationDTO> registrationDTOS = registrationMapper.mapToDTO(studentSections);
        return ResponseEntity.ok(registrationDTOS);
    }

    @PostMapping("/getList")
    public ResponseEntity<?> getList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody RegistrationRequest filterRequest) {
        List<StudentSection> studentSections = studentSectionRepository.findAll(registrationSpecification.getFilter(filterRequest));

        if (filterRequest.getTermId() != null) {
            studentSections = studentSections.stream().filter(studentSection -> studentSection.getSection().getTermId().equals(filterRequest.getTermId())).collect(Collectors.toList());
        }

        List<RegistrationDTO> registrationDTOS = registrationMapper.mapToDTO(studentSections);
        return ResponseEntity.ok(registrationDTOS);
    }

    @PostMapping("/registerSection")
    public ResponseEntity<?> registerSection(@RequestParam(value = "userId") Long userId, @RequestBody RegistrationSectionBean data) {
        // Check Section
        if (data.getSectionId() == null) {
            throw new ValidationException("Học phần sinh viên đăng ký không được trống !!");
        }

        Section section = sectionRepository.findById(data.getSectionId()).orElse(null);

        if (section == null) {
            throw new ValidationException("Không tìm thấy học phần sinh viên đăng ký !!");
        }

//        if(section.getLockDate().getTime() < new Date().getTime()){
//            throw new ValidationException("Học phần đã khoá đăng ký trong kỳ này !! Nếu muốn chèn lớp xin liên hệ phòng Giáo vụ");
//        }

        // Check Student
        if (data.getStudentId() == null) {
            throw new ValidationException("Mã sinh viên đăng ký không được để trống !!");
        }

        Student student = studentRepository.findById(data.getStudentId()).orElse(null);

        if (student == null) {
            throw new ValidationException("Không tìm thấy sinh viên đăng ký !!");
        }

        // Check Term
        if (data.getTermId() == null) {
            throw new ValidationException("Học kỳ đăng ký học phần không được để trống !!");
        }

        Term term = termRepository.findById(data.getTermId()).orElse(null);

        if (term == null) {
            throw new ValidationException("Không tìm thấy học kì đăng ký sinh viên chọn !!");
        }

        // Check Course
        Course course = courseRepository.findById(section.getCourseId()).orElse(null);

        if(course == null){
            throw new ValidationException("Không tìm thấy môn học mà học phần này thuộc !!");
        }

        if(course.getRequireCourse() != null){
            if(course.getRequireCourse().getPrerequisite() != null && !course.getRequireCourse().getPrerequisite().isEmpty()){
                for(String courseCode : course.getRequireCourse().getPrerequisite()){
                    Course courseByCode = courseRepository.findByCode(courseCode);

                    if(courseByCode == null){
                        throw new ValidationException("Không tìm thấy môn học tiên quyết với mã học phần này");
                    }

                    StudentCourse studentCourse = studentCourseRepository.findByCourseIdAndStudentId(course.getId(), student.getId());

                    if(studentCourse == null){
                        throw new ValidationException("Sinh viên chưa hoàn thành điều kiện tiên quyết của môn học !!");
                    }

                    if(!studentCourse.getCompletedStatus().equals(CompletedStatus.completed)){
                        throw new ValidationException("Sinh viên chưa hoàn thành điều kiện tiên quyết của môn học !!");
                    }
                }
            }

            if(course.getRequireCourse().getStudyFirst() != null && !course.getRequireCourse().getStudyFirst().isEmpty()){
                for(String courseCode : course.getRequireCourse().getStudyFirst()){
                    Course courseByCode = courseRepository.findByCode(courseCode);

                    if(courseByCode == null){
                        throw new ValidationException("Không tìm thấy môn học tiên quyết với mã học phần này");
                    }

                    StudentCourse studentCourse = studentCourseRepository.findByCourseIdAndStudentId(course.getId(), student.getId());

                    if(studentCourse == null){
                        throw new ValidationException("Sinh viên chưa hoàn thành môn học học trước của môn học !!");
                    }

                    if(studentCourse.getResult().getFinalPoint() != null && studentCourse.getResult().getFinalPoint() > 0 ){
                        throw new ValidationException("Sinh viên chưa hoàn thành môn học học trước của môn học !!");
                    }
                }
            }
        }

        // Check Đã đăng ký học phần này chưa
        boolean isExistStudentSection = studentSectionRepository.existsByStudentIdAndSectionId(student.getId(), section.getId());

        if (isExistStudentSection) {
            throw new ValidationException("Sinh viên đã đăng ký học phần này rồi !!");
        }

        // Check Have Practice Section class
        if (section.getCourse().getCourseDuration().getPractice() > 0 && section.getCourse().getCourseDuration().getTheory() > 0) {
            if (data.getSectionClassId() == null) {
                throw new ValidationException("Xin hãy chọn lớp học phần thực hành thuộc lớp lý thuyết này (Nếu chưa có lịch lớp thực hành cho lớp này xin liên hệ với ban giáo vụ) !!");
            }
        }

        // Section Class Theory check
        if (data.getSectionClassRefId() == null) {
            throw new ValidationException("Lớp học phần của sinh viên không được để trống khi đăng ký !!");
        }

        SectionClass sectionClassRef = sectionClassRepository.findById(data.getSectionClassRefId()).orElse(null);

        if (sectionClassRef == null) {
            throw new ValidationException("Không tìm thấy lớp học phần này !!");
        }

//        if (sectionClassRef.getSectionClassStatus().equals(SectionClassStatus.closed)) {
//            throw new ValidationException("Lớp học phần đã đóng đăng ký !!");
//        } else if (sectionClassRef.getSectionClassStatus().equals(SectionClassStatus.full)) {
//            throw new ValidationException("Lớp học phần đã đạt tối đa sinh viên !!");
//        } else if (sectionClassRef.getSectionClassStatus().equals(SectionClassStatus.canceled)) {
//            throw new ValidationException("Lớp học phần đã bị huỷ mở lớp !!");
//        }

        List<StudentSection> students = studentSectionClassRepository.findBySectionClassId(sectionClassRef.getId()).stream().map(StudentSectionClass::getStudentSection).collect(Collectors.toList());

        if (students.size() >= sectionClassRef.getMaxStudents()) {
            throw new ValidationException("Số sinh viên hiện tại của lớp học phần này đã đạt tối đa");
        }

        // Check SectionClass Practice
        boolean haveSectionClassPractice = section.getCourse().getCourseDuration().getPractice() > 0 && section.getCourse().getCourseDuration().getTheory() > 0;
        SectionClass sectionClass = null;
        if (haveSectionClassPractice) {
            if (data.getSectionClassId() == null) {
                throw new ValidationException("Lớp học phần thực hành của sinh viên không được để trống khi đăng ký !!");
            }

            sectionClass = sectionClassRepository.findById(data.getSectionClassId()).orElse(null);

            if (sectionClass == null) {
                throw new ValidationException("Không tìm thấy lớp học phần thực hành này !!");
            }

//            if (sectionClass.getSectionClassStatus().equals(SectionClassStatus.closed)) {
//                throw new ValidationException("Lớp học phần thực hành đã đóng đăng ký !!");
//            } else if (sectionClass.getSectionClassStatus().equals(SectionClassStatus.full)) {
//                throw new ValidationException("Lớp học phần thực hành đã đạt tối đa sinh viên !!");
//            } else if (sectionClass.getSectionClassStatus().equals(SectionClassStatus.canceled)) {
//                throw new ValidationException("Lớp học phần thực hành đã bị huỷ mở lớp !!");
//            }

            List<StudentSection> studentSections = studentSectionClassRepository.findBySectionClassId(sectionClass.getId()).stream().map(StudentSectionClass::getStudentSection).collect(Collectors.toList());

            if (studentSections.size() >= sectionClass.getMaxStudents()) {
                throw new ValidationException("Số sinh viên hiện tại của lớp học phần này đã đạt tối đa");
            }
        }
        //#region Registration Student - Section
        Result result = new Result();
        result.setSectionId(section.getId());
        result.setStudentId(student.getId());

        result = resultRepository.saveAndFlush(result);

        if (result.getId() == null) {
            throw new ValidationException("Khởi tạo kết quả học tập mới cho sinh viên không thành công !!");
        }

        StudentSection studentSection = new StudentSection();

        if (data.getRegistrationStatus() == null) {
            throw new ValidationException("Trạng thái đăng ký không được để trống !!");
        }

        studentSection.setStudentId(student.getId());
        studentSection.setSectionId(section.getId());
        studentSection.setResultId(result.getId());
        studentSection.setRegistrationStatus(data.getRegistrationStatus());
        studentSection.setCompletedStatus(CompletedStatus.uncompleted);

        studentSection = studentSectionRepository.saveAndFlush(studentSection);

        if (studentSection.getId() == null) {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //#endregion

        //#region Registration Student - Section Class

        //#region Section Class (Ref) - Lớp Chính
        StudentSectionClass studentSectionClassRef = new StudentSectionClass();
        studentSectionClassRef.setStudentId(student.getId());
        studentSectionClassRef.setStudentSectionId(studentSection.getId());
        studentSectionClassRef.setSectionClassId(sectionClassRef.getId());

        if (data.getTimeAndPlaceRefId() == null) {
            throw new ValidationException("Thời gian học của lớp học không được để trống !!");
        }

        TimeAndPlace timeAndPlace = timeAndPlaceRepository.findById(data.getTimeAndPlaceRefId()).orElse(null);

        if (timeAndPlace == null) {
            throw new ValidationException("Không tìm thấy thời gian học của lớp học phần này !!");
        }

        studentSectionClassRef.setTimeAndPlaceId(timeAndPlace.getId());


        studentSectionClassRef = studentSectionClassRepository.saveAndFlush(studentSectionClassRef);

        if (studentSectionClassRef.getId() == null) {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //#endregion

        //#region Section Class (Practice) - Lớp Phụ (Thực hành)
        if (haveSectionClassPractice) {
            StudentSectionClass studentSectionClassPractice = new StudentSectionClass();
            studentSectionClassPractice.setStudentId(student.getId());
            studentSectionClassPractice.setStudentSectionId(studentSection.getId());
            studentSectionClassPractice.setSectionClassId(sectionClass.getId());

            if (data.getTimeAndPlaceId() == null) {
                throw new ValidationException("Thời gian học của lớp học thực hành không được để trống !!");
            }

            TimeAndPlace timeAndPlacePractice = timeAndPlaceRepository.findById(data.getTimeAndPlaceId()).orElse(null);

            if (timeAndPlacePractice == null) {
                throw new ValidationException("Không tìm thấy thời gian học của lớp học phần này !!");
            }

            studentSectionClassPractice.setTimeAndPlaceId(timeAndPlacePractice.getId());

            studentSectionClassPractice = studentSectionClassRepository.saveAndFlush(studentSectionClassPractice);

            if (studentSectionClassPractice.getId() == null) {
                return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        //#endregion

        //#endregion

        //#region Create Student - Tuition
        Tuition tuition = tuitionRepository.findBySectionId(section.getId());

        if (tuition != null) {
            StudentTuition studentTuition = new StudentTuition();
            studentTuition.setStudentId(student.getId());
            studentTuition.setTuitionId(tuition.getId());

            studentTuition.setPlusDeductions(0D);
            studentTuition.setMinusDeductions(0D);

            studentTuition.setStatus(TuitionStatus.unpaid);
            studentTuition.setInvestigateStatus(true);

            studentTuition = studentTuitionRepository.saveAndFlush(studentTuition);

            if (studentTuition.getId() == null) {
                return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //#endregion

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/inputResult")
    public ResponseEntity<?> inputResultStudentSection(@RequestParam(value = "userId") Long userId, @RequestBody List<ResultBean> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            throw new ValidationException("Danh sách nhập điểm của lớp học phần không được để trống !!");
        }
//
//        SectionClass sectionClass = sectionClassRepository.findById(sectionClassId).orElse(null);
//
//        if(sectionClass == null){
//            throw new ValidationException("Không tìm thấy lớp học phần !!");
//        }
//
//        sectionClass.setInputResultEnable(enable);
//
//        sectionClass = sectionClassRepository.saveAndFlush(sectionClass);
//
//        if(sectionClass.getId() == null){
//            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
//        }

        for (ResultBean resultBean : dataList) {
            if (resultBean.getId() != null) {
                StudentSection studentSection = studentSectionRepository.findById(resultBean.getId()).orElse(null);

                if (studentSection == null) {
                    throw new ValidationException("Không tìm thấy sinh viên trong lớp học phần này !!");
                }
            }

            Result result = null;
            if (resultBean.getResultId() != null) {
                result = resultRepository.findById(resultBean.getResultId()).orElseThrow(null);

                if (result == null) {
                    throw new ValidationException("Không tìm thấy kết quả học tập của sinh viên trong học phần");
                }
            } else {
                result = new Result();
                result.setSectionId(resultBean.getSectionId());
                result.setStudentId(resultBean.getStudentId());
            }

            result.setRegularPoint1(resultBean.getRegularPoint1() != null ? resultBean.getRegularPoint1() : null);
            result.setRegularPoint2(resultBean.getRegularPoint2() != null ? resultBean.getRegularPoint2() : null);
            result.setRegularPoint3(resultBean.getRegularPoint3() != null ? resultBean.getRegularPoint3() : null);
            result.setRegularPoint4(resultBean.getRegularPoint4() != null ? resultBean.getRegularPoint4() : null);
            result.setRegularPoint5(resultBean.getRegularPoint5() != null ? resultBean.getRegularPoint5() : null);

            result.setMidtermPoint1(resultBean.getMidtermPoint1() != null ? resultBean.getMidtermPoint1() : null);
            result.setMidtermPoint2(resultBean.getMidtermPoint2() != null ? resultBean.getMidtermPoint2() : null);
            result.setMidtermPoint3(resultBean.getMidtermPoint3() != null ? resultBean.getMidtermPoint3() : null);

            result.setPracticePoint1(resultBean.getPracticePoint1() != null ? resultBean.getPracticePoint1() : null);
            result.setPracticePoint2(resultBean.getPracticePoint2() != null ? resultBean.getPracticePoint2() : null);

            result.setFinalPoint(resultBean.getFinalPoint() != null ? resultBean.getFinalPoint() : null);

            result = resultRepository.saveAndFlush(result);

            if (result.getId() == null) {
                throw new ValidationException("Lưu kết quả học tập không thành công !!");
            }
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/saveResultFinal")
    public ResponseEntity<?> saveResultFinal(@RequestParam(value = "userId") Long userId, @RequestParam(value = "sectionClassId") Long sectionClassId) {
        if (sectionClassId == null) {
            throw new ValidationException("Mã lớp học phần muốn khoá kết quả không được trống !!");
        }

        SectionClass sectionClass = sectionClassRepository.findById(sectionClassId).orElse(null);

        if (sectionClass == null) {
            throw new ValidationException("Không tìm thấy lớp học phần này !!");
        }

        List<StudentSectionClass> studentSectionClasses = studentSectionClassRepository.findBySectionClassId(sectionClassId);

        if (studentSectionClasses.isEmpty()) {
            throw new ValidationException("Lớp học phần này hiện tại chưa có sinh viên nào !!");
        }

        List<StudentSection> studentSections = studentSectionRepository.findBySectionId(studentSectionClasses.get(0).getSectionClass().getSectionId());

        if (studentSections.isEmpty()) {
            throw new ValidationException("Không tìm thấy học phần của những sinh viên trong lớp này !!");
        }

        for (StudentSection studentSection : studentSections) {
            Result result = studentSection.getResult();
            Course course = courseRepository.findById(studentSection.getSection().getCourseId()).orElse(null);


            if (result.getFinalPoint() == null) {
                throw new ValidationException("Chưa nhập đầy đủ cột điểm cuối kỳ để nộp kết quả học tập !!");
            }

            double totalPoint = 0;
            if (result.getFinalPoint() > 0) {
                int totalRegular = 0;
                int totalRegularPoint = 0;
                if (result.getRegularPoint1() != null) {
                    totalRegularPoint += result.getRegularPoint1();
                    totalRegular++;
                }
                if (result.getRegularPoint2() != null) {
                    totalRegularPoint += result.getRegularPoint2();
                    totalRegular++;
                }
                if (result.getRegularPoint3() != null) {
                    totalRegularPoint += result.getRegularPoint3();
                    totalRegular++;
                }
                if (result.getRegularPoint4() != null) {
                    totalRegularPoint += result.getRegularPoint4();
                    totalRegular++;
                }
                if (result.getRegularPoint5() != null) {
                    totalRegularPoint += result.getRegularPoint5();
                    totalRegular++;
                }

                double regular = (double) totalRegularPoint / totalRegular;

                int totalMidTerm = 0;
                int totalMidTermPoint = 0;
                if (result.getMidtermPoint1() != null) {
                    totalMidTermPoint += result.getMidtermPoint1();
                    totalMidTerm++;
                }
                if (result.getMidtermPoint2() != null) {
                    totalMidTermPoint += result.getMidtermPoint2();
                    totalMidTerm++;
                }
                if (result.getMidtermPoint3() != null) {
                    totalMidTermPoint += result.getMidtermPoint3();
                    totalMidTerm++;
                }
                double midterm = (double) totalMidTermPoint / totalMidTerm;

                int totalPractice = 0;
                int totalPracticePoint = 0;
                if (result.getPracticePoint1() != null) {
                    totalPracticePoint += result.getPracticePoint1();
                    totalPractice++;
                }
                if (result.getPracticePoint2() != null) {
                    totalPracticePoint += result.getPracticePoint2();
                    totalPractice++;
                }
                double practice = (double) totalPracticePoint / totalPractice;

                double finalPoint = result.getFinalPoint();

                if (totalPractice == 0) {
                    totalPoint = (regular * 20 + midterm * 30 + finalPoint * 50) / 100;
                } else {
                    if (course != null) {
                        totalPoint = ((((regular * 20 + midterm * 30 + finalPoint * 50) / 100) * course.getCourseDuration().getTheory()) + (practice * course.getCourseDuration().getPractice())) / course.getCredits();
                    }
                }
            }

            StudentCourse studentCourse = new StudentCourse();
            studentCourse.setStudentId(studentSection.getStudentId());
            studentCourse.setCourseId(course.getId());
            studentCourse.setResultId(result.getId());

            if (totalPoint >= 2) {
                studentCourse.setCompletedStatus(CompletedStatus.completed);
            } else {
                studentCourse.setCompletedStatus(CompletedStatus.uncompleted);
            }

            studentCourse = studentCourseRepository.saveAndFlush(studentCourse);
        }

        sectionClass.setInputResultEnable(false);
        sectionClass = sectionClassRepository.saveAndFlush(sectionClass);

        if (sectionClass.getId() == null) {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/createOrUpdate")
    public ResponseEntity<?> createOrUpdateRegistration(@RequestParam(value = "userId") Long userId, @RequestBody RegistrationSectionBean data) {
        // Student Check
        if (data.getStudentId() == null) {
            throw new ValidationException("Mã sinh viên không được để trống !!");
        }

        Student student = studentRepository.findById(data.getStudentId()).orElse(null);

        if (student == null) {
            throw new ValidationException("Không tìm thấy sinh viên đăng ký !!");
        }

        //#region Student Section
        // Section Check
        if (data.getSectionId() == null) {
            throw new ValidationException("Mã học phần không được để trống !!");
        }

        Section section = sectionRepository.findById(data.getSectionId()).orElse(null);

        if (section == null) {
            throw new ValidationException("Không tìm thấy học phần của lớp học phần này !!");
        }

        StudentSection studentSection = studentSectionRepository.findBySectionIdAndStudentId(section.getId(), student.getId());

        if (studentSection != null) {
            if (data.getSectionClassId() != null) {
                StudentSectionClass studentSectionClass = studentSectionClassRepository.findByStudentSectionIdAndStudentIdAndSectionClassId(studentSection.getId(), student.getId(), data.getSectionClassId());

                if (studentSectionClass != null) {
                    throw new ValidationException("Sinh viên đã đăng ký lớp học phần này rồi !!");
                }
            } else {
                throw new ValidationException("Sinh viên đã đăng ký học phần này rồi !!");
            }
        } else {
            studentSection = new StudentSection();
        }

        // Create new Result for Student Section
        Result result = resultRepository.findByStudentIdAndSectionId(student.getId(), section.getId());

        if (result == null) {
            result = new Result();
            result.setStudentId(student.getId());
            result.setSectionId(section.getId());

            result = resultRepository.saveAndFlush(result);

            if (result.getId() == null) {
                throw new ValidationException("Khởi tạo kết quả học tập cho sinh viên không thành công !!");
            }
        }

        // Create student section
        studentSection.setStudentId(student.getId());
        studentSection.setSectionId(section.getId());
        studentSection.setResultId(result.getId());
        studentSection.setRegistrationStatus(data.getRegistrationStatus());
        studentSection.setCompletedStatus(CompletedStatus.uncompleted);

        studentSection = studentSectionRepository.saveAndFlush(studentSection);

        if (studentSection.getId() == null) {
            throw new ValidationException("khởi tại học phần cho sinh viên không thành công !!");
        }

        //#endregion

        //#region Student - Section Class (Registration Class)

        if (data.getSectionClassId() == null) {
            throw new ValidationException("Lớp học phần của sinh viên không được để trống khi đăng ký !!");
        }

        SectionClass sectionClassTheory = sectionClassRepository.findById(data.getSectionClassId()).orElse(null);

        if (sectionClassTheory == null) {
            throw new ValidationException("Không tìm thấy lớp học phần này !!");
        }

        StudentSectionClass studentSectionClass = new StudentSectionClass();

//        if (sectionClassTheory.getSectionClassStatus().equals(SectionClassStatus.closed)) {
//            throw new ValidationException("Lớp học phần đã đóng đăng ký !!");
//        }

        if (data.getTermId() == null) {
            throw new ValidationException("Học kỳ không được để trống !!");
        }

        Term term = termRepository.findById(data.getTermId()).orElse(null);

        if (term == null) {
            throw new ValidationException("Không tìm thây học kỳ này !!");
        }

        if (data.getTimeAndPlaceId() == null) {
            throw new ValidationException("Thời gian học của lớp học không được để trống !!");
        }

        TimeAndPlace timeAndPlaceTheory = timeAndPlaceRepository.findById(data.getTimeAndPlaceId()).orElse(null);

        if (timeAndPlaceTheory == null) {
            throw new ValidationException("Không tìm thấy thời gian học của lớp học phần này !!");
        }

        studentSectionClass.setTimeAndPlaceId(timeAndPlaceTheory.getId());
        studentSectionClass.setStudentId(student.getId());
        studentSectionClass.setSectionClassId(sectionClassTheory.getId());
        studentSectionClass.setStudentSectionId(studentSection.getId());

        studentSectionClass = studentSectionClassRepository.saveAndFlush(studentSectionClass);

        if (studentSectionClass.getId() == null) {
            throw new ValidationException("khởi tại lớp học phần cho sinh viên không thành công !!");
        }

        //#region CreateOrUpdate Tuition For Student
        Tuition tuition = tuitionRepository.findBySectionId(section.getId());

        if (tuition == null) {
            throw new ValidationException("Không tìm thấy học phí của học phần này !!");
        }

        StudentTuition studentTuition = studentTuitionRepository.findByStudentIdAndTuitionId(student.getId(), tuition.getId());

        if (studentTuition == null) {
            studentTuition = new StudentTuition();
        }
        studentTuition.setStudentId(student.getId());
        studentTuition.setTuitionId(tuition.getId());
        studentTuition.setPlusDeductions(0D);
        studentTuition.setMinusDeductions(0D);
        studentTuition.setStatus(TuitionStatus.unpaid);
        studentTuition.setInvestigateStatus(true);


        studentTuition = studentTuitionRepository.saveAndFlush(studentTuition);

        if (studentTuition.getId() == null) {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //#endregion

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeRegistrationStatus(@RequestParam(value = "userId") Long userId, @RequestBody Map<String, Object> data) {

        if (data.get("id") == null) {
            throw new ValidationException("Học phần đăng ký không được trống !!");
        }

        StudentSection studentSection = studentSectionRepository.findById(((Integer) data.get("id")).longValue()).orElse(null);

        if (studentSection == null) {
            throw new ValidationException("Không tìm thấy học phần sinh viên đã đăng ký !!");
        }

        studentSection.setRegistrationStatus((RegistrationStatus.valueOf((String) data.get("status"))));

        studentSection = studentSectionRepository.saveAndFlush(studentSection);

        if (studentSection.getId() == null) {
            throw new ValidationException("Cập nhật trạng thái cho học phần đăng ký sinh viên không thành công !!");
        }

        if (data.get("status").equals(RegistrationStatus.cancel_register)) {
            // Kiểm tra xoá lớp học phần thuộc học phần cần xoá
            List<StudentSectionClass> studentSectionClasses = studentSectionClassRepository.findByStudentSectionId(studentSection.getId());

            if (!studentSectionClasses.isEmpty()) {
                try {
                    studentSectionClassRepository.deleteAll(studentSectionClasses);
                } catch (Exception ignored) {
                }
            }

            // Xoá bảng điểm của sinh viên đã tạo trong học phần này
            Result result = resultRepository.findByStudentIdAndSectionId(studentSection.getStudentId(), studentSection.getSectionId());

            if (result != null) {
                try {
                    resultRepository.delete(result);
                } catch (Exception ignored) {
                }
            }

            // Xoá bỏ công nợ giữa học phần và sinh viên này
            Tuition tuition = tuitionRepository.findBySectionId(studentSection.getSectionId());

            if (tuition == null) {
                throw new ValidationException("Không tìm thấy học phí cho học phần này !!");
            }
            StudentTuition studentTuition = studentTuitionRepository.findByStudentIdAndTuitionId(studentSection.getStudentId(), tuition.getId());

            if (studentTuition != null) {
                try {
                    studentTuitionRepository.delete(studentTuition);
                } catch (Exception ignored) {
                }
            }
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/deletedRegistration")
    public ResponseEntity<?> deletedRegistration(@RequestParam(value = "userId") Long userId, @RequestParam(value = "id") Long id) {
        // Xoá bỏ đăng ký học phần
        StudentSection studentSection = studentSectionRepository.findById(id).orElse(null);

        if (studentSection == null) {
            throw new ValidationException("Không tìm thấy học phần đăng ký của sinh viên !!");
        }

        UserEntity userEntity = userRepository.findById(userId).orElse(null);

        if(userEntity == null){
            throw new ValidationException("Không tìm thấy người dùng này !!");
        }

        if(userEntity.getSystemRole().equals(SystemRole.student)){
            if(studentSection.getSection().getLockDate().getTime() < new Date().getTime()){
                throw new ValidationException("Không thể huỷ học phần này do đã khoá đăng ký !!");
            }
        }

        Course course = courseRepository.findById(studentSection.getSection().getCourseId()).orElse(null);
        if(course == null){
            throw new ValidationException("Không tìm thấy môn học của học phần đăng ký !!");
        }

        try {
            studentSectionRepository.delete(studentSection);
        } catch (Exception ignored) {
        }

        // Kiểm tra xoá lớp học phần thuộc học phần cần xoá
        List<StudentSectionClass> studentSectionClasses = studentSectionClassRepository.findByStudentSectionId(studentSection.getId());

        if (!studentSectionClasses.isEmpty()) {
            try {
                studentSectionClassRepository.deleteAll(studentSectionClasses);
            } catch (Exception ignored) {
            }
        }

        // Xoá bảng điểm của sinh viên đã tạo trong học phần này
        Result result = resultRepository.findByStudentIdAndSectionId(studentSection.getStudentId(), studentSection.getSectionId());

        if (result != null) {
            try {
                resultRepository.delete(result);
            } catch (Exception ignored) {
            }
        }

        // Xoá bỏ công nợ giữa học phần và sinh viên này
        Tuition tuition = tuitionRepository.findBySectionId(studentSection.getSectionId());

        if (tuition == null) {
            throw new ValidationException("Không tìm thấy học phí cho học phần này !!");
        }
        StudentTuition studentTuition = studentTuitionRepository.findByStudentIdAndTuitionId(studentSection.getStudentId(), tuition.getId());

        if (studentTuition != null) {
            try {
                studentTuitionRepository.delete(studentTuition);
            } catch (Exception ignored) {
            }
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
