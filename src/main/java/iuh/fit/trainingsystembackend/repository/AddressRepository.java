package com.thinkvitals.repository;
import com.thinkvitals.enums.AddressRefType;
import com.thinkvitals.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface AddressRepository extends JpaRepository<Address, Long>, JpaSpecificationExecutor<Address> {
    List<Address> findAddressesByRefIdAndRefType(Long refId, AddressRefType refType);
    Address findDistinctByRefIdAndRefType(Long refId, AddressRefType refType);
}
