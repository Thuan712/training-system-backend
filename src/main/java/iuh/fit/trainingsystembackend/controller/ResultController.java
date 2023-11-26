package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.bean.SectionClassBean;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.model.Result;
import iuh.fit.trainingsystembackend.model.UserEntity;
import iuh.fit.trainingsystembackend.request.ResultRequest;
import iuh.fit.trainingsystembackend.request.UserRequest;
import iuh.fit.trainingsystembackend.service.ResultService;
import iuh.fit.trainingsystembackend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.RESULT_ENDPOINT)
public class ResultController {
    private ResultService resultService;
    @PostMapping("/getPage")
    public ResponseEntity<?> getPage(@RequestParam(value = "userId", required = false) Long userId,
                                     @RequestParam("pageNumber") int pageNumber, @RequestParam("pageRows") int pageRows,
                                     @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                     @RequestParam(value = "sortOrder", required = false, defaultValue = "-1") int sortOrder,
                                     @RequestBody ResultRequest filterRequest) {
        return ResponseEntity.ok(null);
    }


    @PostMapping("/inputScores")
    public ResponseEntity<?> inputResult(@RequestParam(value = "lecturerId") Long lecturerId,@RequestBody SectionClassBean data){
        List<Result> results = resultService.enterScoresForStudent(lecturerId, data);

        if(results.isEmpty()){
            throw new ValidationException("Input scores for student fail !!");
        }

        return ResponseEntity.ok(results);
    }

}
