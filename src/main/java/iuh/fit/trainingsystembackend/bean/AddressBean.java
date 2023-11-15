package iuh.fit.trainingsystembackend.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressBean implements Serializable {
    @SerializedName("user_address_id")
    private Long userId;
    private Long addressId;

    private String addressLine;

    private Integer regionId;
    private String regionName;

    private String provinceCode;
    private String provinceName;

    private String wardCode;
    private String wardName;

    private String phone;
}
