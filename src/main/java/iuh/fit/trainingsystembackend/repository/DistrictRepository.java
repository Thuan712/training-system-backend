package iuh.fit.trainingsystembackend.repository;

import iuh.fit.trainingsystembackend.model.District;
import iuh.fit.trainingsystembackend.model.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, String> {
    List<District> findDistrictsByProvinceCode(String provinceCode);

}
