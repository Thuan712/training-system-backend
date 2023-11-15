package iuh.fit.trainingsystembackend.service;

import iuh.fit.trainingsystembackend.bean.AddressBean;
import iuh.fit.trainingsystembackend.model.Address;
import iuh.fit.trainingsystembackend.model.Province;
import iuh.fit.trainingsystembackend.model.Region;
import iuh.fit.trainingsystembackend.model.Ward;
import iuh.fit.trainingsystembackend.repository.AddressRepository;
import iuh.fit.trainingsystembackend.repository.ProvinceRepository;
import iuh.fit.trainingsystembackend.repository.RegionRepository;
import iuh.fit.trainingsystembackend.repository.WardRepository;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;

@Service
public class AddressService implements Serializable {
    private AddressRepository addressRepository;
    private RegionRepository regionRepository;
    private WardRepository wardRepository;
    private ProvinceRepository provinceRepository;
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
        }

        toSave.setAddressLine(data.getAddressLine());

        if(data.getRegionId() != null) {
            Region region = regionRepository.findById(data.getRegionId()).orElse(null);
            if(region != null) {
                toSave.setRegionName(region.getName());
                toSave.setRegionId(region.getId());
            }
        }

        if(data.getProvinceCode() != null && !data.getProvinceName().isEmpty()) {
            Province province = provinceRepository.findProvinceByCode(data.getProvinceCode());
            if(province != null) {
                toSave.setProvinceCode(province.getCode());
                toSave.setProvinceName(province.getFullName());
            }
        }


        if(data.getWardCode() != null && !data.getWardCode().isEmpty()) {
            Ward ward = wardRepository.findWardByCode(data.getWardCode());
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

}
