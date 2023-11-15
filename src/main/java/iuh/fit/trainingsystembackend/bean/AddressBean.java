package com.thinkvitals.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import com.thinkvitals.data.PhoneExtension;
import com.thinkvitals.enums.AddressType;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressBean implements Serializable {
    @SerializedName("hospital_address_id")
    private Long hospitalId;
    @SerializedName("user_address_id")
    private Long userId;
    @SerializedName("nok_address_id")
    private Long nokId;
    @SerializedName("gp_address_id")
    private Long gpId;

    private Long addressId;
    private AddressType type = AddressType.other;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressLine4;
    private String refCountry;
    private String country;
    private String refState;
    private String state;
    private String city;
    private String postalCode;
    private String phone;
    private PhoneExtension phoneExtension;

}
