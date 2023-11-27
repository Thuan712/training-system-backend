package iuh.fit.trainingsystembackend.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maxmind.geoip2.record.City;
import iuh.fit.trainingsystembackend.exceptions.ValidationException;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.*;
import iuh.fit.trainingsystembackend.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(Constants.PREFIX_ENDPOINT + Constants.ADDRESS_ENDPOINT)
public class AddressController {
    private AddressRepository addressRepository;
    private RegionRepository regionRepository;
    private WardRepository wardRepository;
    private ProvinceRepository provinceRepository;
    private DistrictRepository districtRepository;
    @GetMapping("/getById")
    public ResponseEntity<?> getById(
            @RequestParam("id") Long id
    ){
        Address address = addressRepository.findById(id).orElse(null);
        if(address == null) {
            throw new ValidationException("Address Not Found !");
        }
        return ResponseEntity.ok(address);
    }

    @GetMapping("/getRegions")
    public ResponseEntity<?> getRegions() {
        List<Region> regions = regionRepository.findAll();
        return ResponseEntity.ok(regions);
    }
    @GetMapping("/getProvinces")
    public ResponseEntity<?> getStates(
            @RequestParam("regionId") Integer regionId
    ){
        List<Province> provinces = provinceRepository.findProvincesByRegionId(regionId);
        return ResponseEntity.ok(provinces);
    }
    @GetMapping("/getWards")
    public ResponseEntity<?> getWards(
            @RequestParam("districtCode") String districtCode

    ){
        List<Ward> wards = wardRepository.findWardByDistrictCode(districtCode);
        return ResponseEntity.ok(wards);
    }

    @GetMapping("/getDistrict")
    public ResponseEntity<?> getDistricts(
            @RequestParam("provinceCode") String provinceCode
    ){
        List<District> districts = districtRepository.findDistrictsByProvinceCode(provinceCode);
        return ResponseEntity.ok(districts);
    }
}

