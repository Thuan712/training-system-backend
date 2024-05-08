package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.AcademicYearDTO;
import iuh.fit.trainingsystembackend.enums.TermType;
import iuh.fit.trainingsystembackend.model.AcademicYear;
import iuh.fit.trainingsystembackend.model.Term;
import iuh.fit.trainingsystembackend.repository.TermRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("academicYearMapper")
@AllArgsConstructor
public class AcademicYearMapper {
    private TermRepository termRepository;

    public AcademicYearDTO mapToDTO(AcademicYear academicYear) {

        Term firstTerm = termRepository.findByAcademicYearIdAndTermType(academicYear.getId(), TermType.first_term);
        Term secondTerm = termRepository.findByAcademicYearIdAndTermType(academicYear.getId(), TermType.second_term);
        Term thirdTerm = termRepository.findByAcademicYearIdAndTermType(academicYear.getId(), TermType.summer_term);
        int yearEnd = academicYear.getYearStart() + 1;
        return AcademicYearDTO.builder()
                .id(academicYear.getId())
                .name(academicYear.getYearStart() != null ? (academicYear.getYearStart() + "-" + yearEnd) : "")
                .yearStart(academicYear.getYearStart())

                .firstTermId(firstTerm != null ? firstTerm.getId() : null)
                .firstTermName(firstTerm != null ? firstTerm.getName(): "")
                .costFirstTerm(firstTerm != null ? firstTerm.getCostPerCredit() : 0)
                .firstTermStart(firstTerm != null ? new SimpleDateFormat("dd/MM/yyyy").format(firstTerm.getTermStart()) : new SimpleDateFormat("dd/MM/yyyy").format(new Date()))
                .firstTermEnd(firstTerm != null ? new SimpleDateFormat("dd/MM/yyyy").format(firstTerm.getTermStart()) : new SimpleDateFormat("dd/MM/yyyy").format(new Date()))

                .secondTermId(secondTerm != null ? secondTerm.getId() : null)
                .secondTermName(secondTerm != null ? secondTerm.getName() : "")
                .costSecondTerm(secondTerm != null ? secondTerm.getCostPerCredit() : 0)
                .secondTermStart(secondTerm != null ? new SimpleDateFormat("dd/MM/yyyy").format(secondTerm.getTermStart()) : new SimpleDateFormat("dd/MM/yyyy").format(new Date()))
                .secondTermEnd(secondTerm != null ? new SimpleDateFormat("dd/MM/yyyy").format(secondTerm.getTermStart()) : new SimpleDateFormat("dd/MM/yyyy").format(new Date()))

                .thirdTermId(thirdTerm != null ? thirdTerm.getId() : null)
                .thirdTermName(thirdTerm != null ? thirdTerm.getName() : "")
                .costThirdTerm(thirdTerm != null ? thirdTerm.getCostPerCredit() : 0)
                .thirdTermStart(thirdTerm != null ? new SimpleDateFormat("dd/MM/yyyy").format(thirdTerm.getTermStart()) : new SimpleDateFormat("dd/MM/yyyy").format(new Date()))
                .thirdTermEnd(thirdTerm != null ? new SimpleDateFormat("dd/MM/yyyy").format(thirdTerm.getTermStart()) : new SimpleDateFormat("dd/MM/yyyy").format(new Date()))
                .build();
    }

    public List<AcademicYearDTO> mapToDTO(List<AcademicYear> academicYears) {
        return academicYears.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<AcademicYearDTO> mapToDTO(Page<AcademicYear> academicYearPage) {
        return academicYearPage.map(this::mapToDTO);
    }
}
