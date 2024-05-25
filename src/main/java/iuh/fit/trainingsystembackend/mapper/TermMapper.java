package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.TermDTO;
import iuh.fit.trainingsystembackend.dto.TimeAndPlaceDTO;
import iuh.fit.trainingsystembackend.enums.DayInWeek;
import iuh.fit.trainingsystembackend.model.AcademicYear;
import iuh.fit.trainingsystembackend.model.Term;
import iuh.fit.trainingsystembackend.model.TimeAndPlace;
import iuh.fit.trainingsystembackend.repository.AcademicYearRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("termMapper")
@AllArgsConstructor
public class TermMapper {
    private AcademicYearRepository academicYearRepository;
    public TermDTO mapToDTO(Term term) {
        AcademicYear academicYear = academicYearRepository.findById(term.getAcademicYearId()).orElse(null);
        String year = "";

        if(academicYear != null){
            String yearEnd = academicYear.getYearStart() + 1 + "";
            year = academicYear.getYearStart() + "-" + yearEnd;
        }

        return TermDTO.builder()
                .id(term.getId())
                .academicYearId(term.getAcademicYearId())
                .name(term.getName() + "(" + year + ")")
                .termType(term.getTermType())
                .termStart(term.getTermStart())
                .termEnd(term.getTermStart())
                .costPerCredit(term.getCostPerCredit())
                .build();
    }

    public List<TermDTO> mapToDTO(List<Term> terms) {
        return terms.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<TermDTO> mapToDTO(Page<Term> terms) {
        return terms.map(this::mapToDTO);
    }
}
