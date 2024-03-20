package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.dto.ScheduleDTO;
import iuh.fit.trainingsystembackend.dto.SectionDTO;
import iuh.fit.trainingsystembackend.mapper.ScheduleMapper;
import iuh.fit.trainingsystembackend.model.Schedule;
import iuh.fit.trainingsystembackend.model.Section;
import iuh.fit.trainingsystembackend.model.Term;
import iuh.fit.trainingsystembackend.repository.ScheduleRepository;
import iuh.fit.trainingsystembackend.request.ScheduleRequest;
import iuh.fit.trainingsystembackend.request.SectionRequest;
import iuh.fit.trainingsystembackend.specification.ScheduleSpecification;
import iuh.fit.trainingsystembackend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.PREFIX_ENDPOINT + "schedule")
public class ScheduleController {
    private ScheduleRepository scheduleRepository;
    private ScheduleSpecification scheduleSpecification;
    private ScheduleMapper scheduleMapper;

    @PostMapping("/getList")
    public ResponseEntity<?> getList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody ScheduleRequest filterRequest) {
        List<Schedule> schedules = scheduleRepository.findAll(scheduleSpecification.getFilter(filterRequest));

        List<ScheduleDTO> scheduleDTOS = scheduleMapper.mapToDTO(schedules);

        return ResponseEntity.ok(scheduleDTOS);
    }

    @PostMapping("/getPage")
    public ResponseEntity<?> getPage(@RequestParam(value = "userId", required = false) Long userId,
                                     @RequestParam("pageNumber") int pageNumber, @RequestParam("pageRows") int pageRows,
                                     @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                     @RequestParam(value = "sortOrder", required = false, defaultValue = "-1") int sortOrder,
                                     @RequestBody ScheduleRequest filterRequest) {
        Page<Schedule> schedules = scheduleRepository.findAll(scheduleSpecification.getFilter(filterRequest), PageRequest.of(pageNumber, pageRows, Sort.by(sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "id")));
        Page<ScheduleDTO> scheduleDTOS = scheduleMapper.mapToDTO(schedules);
        return ResponseEntity.ok(scheduleDTOS);
    }

}
