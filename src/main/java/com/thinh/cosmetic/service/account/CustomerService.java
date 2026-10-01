package com.thinh.cosmetic.service.account;

import com.thinh.cosmetic.domain.dto.request.account.BeautyProfileRequest;
import com.thinh.cosmetic.domain.dto.request.account.CustomerAddressRequest;
import com.thinh.cosmetic.domain.dto.request.account.CustomerProfileRequest;
import com.thinh.cosmetic.domain.dto.response.account.BeautyProfileResponse;
import com.thinh.cosmetic.domain.dto.response.account.CustomerAddressResponse;
import com.thinh.cosmetic.domain.dto.response.account.CustomerResponse;

import java.util.List;

public interface CustomerService {
    CustomerResponse getProfile(Long customerId) throws Exception;
    CustomerResponse updateProfile(Long customerId, CustomerProfileRequest request) throws Exception;
    List<CustomerAddressResponse> getAddresses(Long customerId);
    CustomerAddressResponse addAddress(Long customerId, CustomerAddressRequest request) throws Exception;
    CustomerAddressResponse updateAddress(Long customerId, Long addressId, CustomerAddressRequest request) throws Exception;
    void deleteAddress(Long customerId, Long addressId) throws Exception;
    BeautyProfileResponse getBeautyProfile(Long customerId) throws Exception;
    BeautyProfileResponse updateBeautyProfile(Long customerId, BeautyProfileRequest request) throws Exception;
    List<CustomerResponse> getAllCustomers();
    CustomerResponse getCustomerById(Long id) throws Exception;
}
