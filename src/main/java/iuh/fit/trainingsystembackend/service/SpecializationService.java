package iuh.fit.trainingsystembackend.service;

import iuh.fit.trainingsystembackend.enums.TrainingLevel;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.model.Lecturer;
import iuh.fit.trainingsystembackend.model.Specialization;
import iuh.fit.trainingsystembackend.model.SpecializationClass;
import iuh.fit.trainingsystembackend.repository.LecturerRepository;
import iuh.fit.trainingsystembackend.repository.SpecializationClassRepository;
import iuh.fit.trainingsystembackend.repository.SpecializationRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SpecializationService {

    private final SpecializationClassRepository specializationClassRepository;
    private final SpecializationRepository specializationRepository;
    private final LecturerRepository lecturerRepository;

    public SpecializationClass createNewSpecializationClass(SpecializationClass data){
        SpecializationClass toSave = null;
        if (data.getId() != null) {
            toSave = specializationClassRepository.findById(data.getId()).orElse(null);

            if (toSave == null) {
                throw new ValidationException("Không tìm thấy lớp chuyên ngành !");
            }
        }

        if (toSave == null) {
            toSave = new SpecializationClass();
        }

        if (data.getSpecializationId() == null) {
            throw new ValidationException("Mã chuyên ngành không được để trống !!");
        }

        Specialization specialization = specializationRepository.findById(data.getSpecializationId()).orElse(null);

        if (specialization == null) {
            throw new ValidationException("Không tìm thấy chuyên ngành !!");
        }

        if(data.getLecturerId() == null){
            throw new ValidationException("Mã giãng viên chủ nhiệm của lớp chuyên ngành không được để trống !!");
        }

        Lecturer lecturer = lecturerRepository.findById(data.getLecturerId()).orElse(null);

        if(lecturer == null){
            throw new ValidationException("Không tìm thấy giảng viên !!");
        }

        toSave.setLecturerId(lecturer.getId());
        toSave.setSchoolYear(data.getSchoolYear());
        toSave.setSpecializationId(specialization.getId());

        if(data.getName() == null || data.getName().isEmpty()){
            int numOfSpecializationClass = specializationClassRepository.countBySpecializationId(specialization.getId());

            if(numOfSpecializationClass == 0){
                char key = (char) (67);
                String endYear = data.getSchoolYear();
                String nameGenerate = "DH" + specialization.getCode() + endYear + key;

                toSave.setName(nameGenerate);
            } else {
                char key = (char) (numOfSpecializationClass + 66);
                String endYear = data.getSchoolYear();
                String nameGenerate = "DH" + specialization.getCode() + endYear + key;

                toSave.setName(nameGenerate);
            }
        } else {
            toSave.setName(data.getName());
        }

        if(data.getNumberOfStudents() == null || data.getNumberOfStudents() < 1){
            throw new ValidationException("Sỉ số tối đa sinh viên của lớp chuyên ngành không được để trống và phải lớn hơn 1 !!");
        }

        toSave.setNumberOfStudents(data.getNumberOfStudents());

        toSave = specializationClassRepository.saveAndFlush(toSave);

        if (toSave.getId() == null) {
            return null;
        }

        return toSave;
    }

}
