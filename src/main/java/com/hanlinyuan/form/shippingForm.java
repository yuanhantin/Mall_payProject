package com.hanlinyuan.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: 翰林猿
 * @Description: TODO
 **/
@Data
public class shippingForm {
    @NotBlank
    private String receiverName;

    @NotBlank
    private String receiverPhone;

    @NotBlank
    private String receiverMobile;

    @NotBlank
    private String receiverProvince;

    @NotBlank
    private String receiverCity;

    @NotBlank
    private String receiverDistrict;

    @NotBlank
    private String receiverAddress;

    @NotBlank
    private String receiverZip;
}
