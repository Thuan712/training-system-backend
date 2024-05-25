package iuh.fit.trainingsystembackend.controller;


import iuh.fit.trainingsystembackend.dto.TermDTO;
import iuh.fit.trainingsystembackend.mapper.TermMapper;
import iuh.fit.trainingsystembackend.model.Term;
import iuh.fit.trainingsystembackend.repository.TermRepository;
import iuh.fit.trainingsystembackend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.TERM_ENDPOINT)
public class TermController {
    private TermRepository termRepository;
    private TermMapper termMapper;
    @PostMapping("/getList")
    public ResponseEntity<?> getList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody Term filterRequest) {
        List<Term> terms = termRepository.findAll();
        List<TermDTO> termDTOS = termMapper.mapToDTO(terms);
        return ResponseEntity.ok(termDTOS);
    }
}
