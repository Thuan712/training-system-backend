package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.AcademicYearDTO;
import iuh.fit.trainingsystembackend.dto.LecturerDTO;
import iuh.fit.trainingsystembackend.model.AcademicYear;
import iuh.fit.trainingsystembackend.model.Lecturer;
import iuh.fit.trainingsystembackend.model.Term;
import iuh.fit.trainingsystembackend.model.UserEntity;
import iuh.fit.trainingsystembackend.repository.AcademicYearRepository;
import iuh.fit.trainingsystembackend.repository.TermRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("academicYearMapper")
@AllArgsConstructor
public class AcademicYearMapper {
    private AcademicYearRepository academicYearRepository;
    private TermRepository termRepository;

    public AcademicYearDTO mapToDTO(AcademicYear academicYear) {

        Term firstTerm = termRepository.findById(academicYear.getFirstTermId()).orElse(null);
        Term secondTerm = termRepository.findById(academicYear.getSecondTermId()).orElse(null);
        Term thirdTerm = termRepository.findById(academicYear.getThirdTermId()).orElse(null);


        return AcademicYearDTO.builder()
                .id(academicYear.getId())
                .academicYearName(academicYear.getName())

                .firstTermId(firstTerm != null ? firstTerm.getId() : null)
                .firstTermName(firstTerm != null ? firstTerm.getName() : "")
                .firstTermStart(firstTerm != null ? new SimpleDateFormat("dd/MM/yyyy").format(firstTerm.getTermStart()): new SimpleDateFormat("dd/MM/yyyy").format(new Date()))
                .firstTermEnd(firstTerm != null ?  new SimpleDateFormat("dd/MM/yyyy").format(firstTerm.getTermStart()) : new SimpleDateFormat("dd/MM/yyyy").format(new Date()))

                .secondTermId(secondTerm != null ? secondTerm.getId() : null)
                .secondTermName(secondTerm != null ? secondTerm.getName() : "")
                .secondTermStart(secondTerm != null ?  new SimpleDateFormat("dd/MM/yyyy").format(secondTerm.getTermStart()): new SimpleDateFormat("dd/MM/yyyy").format(new Date()))
                .secondTermEnd(secondTerm != null ?  new SimpleDateFormat("dd/MM/yyyy").format(secondTerm.getTermStart()) : new SimpleDateFormat("dd/MM/yyyy").format(new Date()))

                .thirdTermId(thirdTerm != null ? thirdTerm.getId() : null)
                .thirdTermName(thirdTerm != null ? thirdTerm.getName() : "")
                .thirdTermStart(thirdTerm != null ?  new SimpleDateFormat("dd/MM/yyyy").format(thirdTerm.getTermStart()) : new SimpleDateFormat("dd/MM/yyyy").format(new Date()))
                .thirdTermEnd(thirdTerm != null ?  new SimpleDateFormat("dd/MM/yyyy").format(thirdTerm.getTermStart()) : new SimpleDateFormat("dd/MM/yyyy").format(new Date()))

                .build();
    }

    public List<AcademicYearDTO> mapToDTO(List<AcademicYear> academicYears) {
        return academicYears.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<AcademicYearDTO> mapToDTO(Page<AcademicYear> academicYearPage) {
        return academicYearPage.map(this::mapToDTO);
    }
}
