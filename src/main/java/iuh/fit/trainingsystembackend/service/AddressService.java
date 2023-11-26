package iuh.fit.trainingsystembackend.service;

import iuh.fit.trainingsystembackend.bean.AddressBean;
import iuh.fit.trainingsystembackend.model.*;
import iuh.fit.trainingsystembackend.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;

@Service
@AllArgsConstructor
public class AddressService implements Serializable {
    private AddressRepository addressRepository;
    private RegionRepository regionRepository;
    private WardRepository wardRepository;
    private ProvinceRepository provinceRepository;
    private DistrictRepository districtRepository;
    public Address saveAddress(AddressBean data) {
        Address toSave = null;
        if (data.getAddressId() != null) {
            toSave = addressRepository.findById(data.getAddressId()).orElse(null);
            if (toSave != null) {
                toSave.setUpdatedAt(new Date());
            }
        }
        if (toSave == null) {
            toSave = new Address();
            toSave.setUserId(data.getUserId());
        }

        toSave.setAddressLine(data.getAddressLine());

        if(data.getRegionId() != null) {
            Region region = regionRepository.findById(data.getRegionId()).orElse(null);
            if(region != null) {
                toSave.setRegionName(region.getName());
                toSave.setRegionId(region.getId());
            }
        }

        if(data.getDistrictCode() != null && !data.getDistrictCode().isEmpty()) {
            District district = districtRepository.findById(data.getDistrictCode()).orElse(null);
            if(district != null){
                toSave.setDistrictCode(district.getCode());
                toSave.setDistrictName(district.getName());
            }
        }

        if(data.getProvinceCode() != null && !data.getProvinceName().isEmpty()) {
            Province province = provinceRepository.findById(data.getProvinceCode()).orElse(null);
            if(province != null) {
                toSave.setProvinceCode(province.getCode());
                toSave.setProvinceName(province.getFullName());
            }
        }


        if(data.getWardCode() != null && !data.getWardCode().isEmpty()) {
            Ward ward = wardRepository.findById(data.getWardCode()).orElse(null);
            if(ward != null) {
                toSave.setWardName(ward.getName());
                toSave.setWardCode(ward.getCode());
            }
        }

        toSave.setPhone(data.getPhone());

        try {
            toSave = addressRepository.saveAndFlush(toSave);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return toSave;
    }

    public Address getUserAddress(Long userId){
        return addressRepository.findAddressByUserId(userId);
    }
}
