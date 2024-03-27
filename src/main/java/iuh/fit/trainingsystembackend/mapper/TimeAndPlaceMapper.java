package iuh.fit.trainingsystembackend.mapper;

import iuh.fit.trainingsystembackend.dto.SectionDTO;
import iuh.fit.trainingsystembackend.dto.TimeAndPlaceDTO;
import iuh.fit.trainingsystembackend.dto.UserInfoDTO;
import iuh.fit.trainingsystembackend.enums.DayInWeek;
import iuh.fit.trainingsystembackend.model.Section;
import iuh.fit.trainingsystembackend.model.Specialization;
import iuh.fit.trainingsystembackend.model.TimeAndPlace;
import iuh.fit.trainingsystembackend.model.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("timeAndPlaceMapper")
@AllArgsConstructor
public class TimeAndPlaceMapper {

    public TimeAndPlaceDTO mapToDTO(TimeAndPlace timeAndPlace) {
        String dayInWeek = "";

        if(timeAndPlace.getDayOfTheWeek().equals(DayInWeek.monday)){
            dayInWeek += "Thứ Hai";
        } else if(timeAndPlace.getDayOfTheWeek().equals(DayInWeek.tuesday)){
            dayInWeek += "Thứ Ba";
        }else if(timeAndPlace.getDayOfTheWeek().equals(DayInWeek.wednesday)){
            dayInWeek += "Thứ Tư";
        }else if(timeAndPlace.getDayOfTheWeek().equals(DayInWeek.thursday)){
            dayInWeek += "Thứ Năm";
        }else if(timeAndPlace.getDayOfTheWeek().equals(DayInWeek.friday)){
            dayInWeek += "Thứ Sáu";
        }else if(timeAndPlace.getDayOfTheWeek().equals(DayInWeek.saturday)){
            dayInWeek += "Thứ Bảy";
        }else if(timeAndPlace.getDayOfTheWeek().equals(DayInWeek.sunday)){
            dayInWeek += "Chủ Nhật";
        }

        return TimeAndPlaceDTO.builder()
                .id(timeAndPlace.getId())
                .sectionClassId(timeAndPlace.getSectionClassId())
                .room(timeAndPlace.getRoom())
                .dayOfTheWeek(timeAndPlace.getDayOfTheWeek())
                .periodStart(timeAndPlace.getPeriodStart())
                .periodEnd(timeAndPlace.getPeriodEnd())
                .note(timeAndPlace.getNote())

                .name(timeAndPlace.getRoom() + " ("+ dayInWeek + " " + timeAndPlace.getPeriodStart() + "-" + timeAndPlace.getPeriodEnd() + ")")
                .build();
    }

    public List<TimeAndPlaceDTO> mapToDTO(List<TimeAndPlace> timeAndPlaces) {
        return timeAndPlaces.parallelStream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<TimeAndPlaceDTO> mapToDTO(Page<TimeAndPlace> timeAndPlaces) {
        return timeAndPlaces.map(this::mapToDTO);
    }
}
