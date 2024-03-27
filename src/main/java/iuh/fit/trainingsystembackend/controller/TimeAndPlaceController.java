package iuh.fit.trainingsystembackend.controller;

import iuh.fit.trainingsystembackend.dto.TimeAndPlaceDTO;
import iuh.fit.trainingsystembackend.mapper.TimeAndPlaceMapper;
import iuh.fit.trainingsystembackend.model.TimeAndPlace;
import iuh.fit.trainingsystembackend.repository.TimeAndPlaceRepository;
import iuh.fit.trainingsystembackend.request.TimeAndPlaceRequest;
import iuh.fit.trainingsystembackend.specification.TimeAndPlaceSpecification;
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
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.TIME_AND_PLACE_ENDPOINT)
public class TimeAndPlaceController {
    private TimeAndPlaceSpecification timeAndPlaceSpecification;
    private TimeAndPlaceRepository timeAndPlaceRepository;
    private TimeAndPlaceMapper timeAndPlaceMapper;
    @PostMapping("/getPage")
    public ResponseEntity<?> getPage(@RequestParam(value = "userId", required = false) Long userId,
                                     @RequestParam("pageNumber") int pageNumber, @RequestParam("pageRows") int pageRows,
                                     @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                     @RequestParam(value = "sortOrder", required = false, defaultValue = "-1") int sortOrder,
                                     @RequestBody TimeAndPlaceRequest filterRequest) {
        Page<TimeAndPlace> timeAndPlaces = timeAndPlaceRepository.findAll(timeAndPlaceSpecification.getFilter(filterRequest), PageRequest.of(pageNumber, pageRows, Sort.by(sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "id")));

        Page<TimeAndPlaceDTO> timeAndPlaceDTOS = timeAndPlaceMapper.mapToDTO(timeAndPlaces);
        return ResponseEntity.ok(timeAndPlaceDTOS);
    }

    @PostMapping("/getList")
    public ResponseEntity<?> getList(@RequestParam(value = "userId", required = false) Long userId, @RequestBody TimeAndPlaceRequest filterRequest) {
        List<TimeAndPlace> timeAndPlaces = timeAndPlaceRepository.findAll(timeAndPlaceSpecification.getFilter(filterRequest));
        List<TimeAndPlaceDTO> timeAndPlaceDTOS = timeAndPlaceMapper.mapToDTO(timeAndPlaces);
        return ResponseEntity.ok(timeAndPlaceDTOS);
    }

}
