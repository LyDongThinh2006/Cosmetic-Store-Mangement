package com.thinh.cosmetic.service.account.impl;

import com.thinh.cosmetic.domain.dto.request.account.BeautyProfileRequest;
import com.thinh.cosmetic.domain.dto.request.account.CustomerAddressRequest;
import com.thinh.cosmetic.domain.dto.request.account.CustomerProfileRequest;
import com.thinh.cosmetic.domain.dto.response.account.BeautyProfileResponse;
import com.thinh.cosmetic.domain.dto.response.account.CustomerAddressResponse;
import com.thinh.cosmetic.domain.dto.response.account.CustomerResponse;
import com.thinh.cosmetic.domain.entity.account.BeautyProfileEntity;
import com.thinh.cosmetic.domain.entity.account.CustomerAddressEntity;
import com.thinh.cosmetic.domain.entity.account.CustomerEntity;
import com.thinh.cosmetic.mapper.account.CustomerAddressMapper;
import com.thinh.cosmetic.mapper.account.CustomerMapper;
import com.thinh.cosmetic.repository.account.BeautyProfileRepository;
import com.thinh.cosmetic.repository.account.CustomerAddressRepository;
import com.thinh.cosmetic.repository.account.CustomerRepository;
import com.thinh.cosmetic.service.account.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository addressRepository;
    private final BeautyProfileRepository beautyProfileRepository;
    private final CustomerMapper customerMapper;
    private final CustomerAddressMapper addressMapper;

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getProfile(Long customerId) throws Exception {
        return customerMapper.toResponse(findCustomer(customerId));
    }

    @Override
    public CustomerResponse updateProfile(Long customerId, CustomerProfileRequest request) throws Exception {
        CustomerEntity customer = findCustomer(customerId);
        if (request.getFullName() != null) customer.setFullName(request.getFullName());
        if (request.getDob() != null) customer.setDob(request.getDob());
        if (request.getGender() != null) customer.setGender(request.getGender());
        if (request.getPhone() != null && customer.getAccount() != null) {
            customer.getAccount().setPhone(request.getPhone());
        }
        return customerMapper.toResponse(customerRepository.save(customer));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerAddressResponse> getAddresses(Long customerId) {
        return addressRepository.findByCustomerEntityId(customerId)
                .stream().map(addressMapper::toResponse).toList();
    }

    @Override
    public CustomerAddressResponse addAddress(Long customerId, CustomerAddressRequest request) throws Exception {
        CustomerEntity customer = findCustomer(customerId);
        CustomerAddressEntity address = addressMapper.toEntity(request);
        address.setCustomerEntity(customer);
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressRepository.findByCustomerEntityId(customerId)
                    .forEach(a -> { a.setIsDefault(false); addressRepository.save(a); });
        }
        return addressMapper.toResponse(addressRepository.save(address));
    }

    @Override
    public CustomerAddressResponse updateAddress(Long customerId, Long addressId, CustomerAddressRequest request) throws Exception {
        CustomerAddressEntity address = addressRepository.findById(addressId)
                .orElseThrow(() -> new Exception("Address not found: " + addressId));
        addressMapper.updateEntity(request, address);
        return addressMapper.toResponse(addressRepository.save(address));
    }

    @Override
    public void deleteAddress(Long customerId, Long addressId) throws Exception {
        addressRepository.deleteById(addressId);
    }

    @Override
    @Transactional(readOnly = true)
    public BeautyProfileResponse getBeautyProfile(Long customerId) throws Exception {
        BeautyProfileEntity profile = beautyProfileRepository.findByCustomerId(customerId)
                .orElse(null);
        if (profile == null) return null;
        return BeautyProfileResponse.builder()
                .id(profile.getId()).skinType(profile.getSkinType())
                .skinConcerns(profile.getSkinConcerns()).careNeeds(profile.getCareNeeds())
                .preferences(profile.getPreferences()).priceRangeInterest(profile.getPriceRangeInterest())
                .updatedAt(profile.getUpdatedAt()).build();
    }

    @Override
    public BeautyProfileResponse updateBeautyProfile(Long customerId, BeautyProfileRequest request) throws Exception {
        CustomerEntity customer = findCustomer(customerId);
        BeautyProfileEntity profile = beautyProfileRepository.findByCustomerId(customerId)
                .orElse(BeautyProfileEntity.builder().customer(customer).build());
        profile.setSkinType(request.getSkinType());
        profile.setSkinConcerns(request.getSkinConcerns());
        profile.setCareNeeds(request.getCareNeeds());
        profile.setPreferences(request.getPreferences());
        profile.setPriceRangeInterest(request.getPriceRangeInterest());
        profile.setUpdatedAt(LocalDateTime.now());
        profile = beautyProfileRepository.save(profile);
        return BeautyProfileResponse.builder()
                .id(profile.getId()).skinType(profile.getSkinType())
                .skinConcerns(profile.getSkinConcerns()).careNeeds(profile.getCareNeeds())
                .preferences(profile.getPreferences()).priceRangeInterest(profile.getPriceRangeInterest())
                .updatedAt(profile.getUpdatedAt()).build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) throws Exception {
        return customerMapper.toResponse(findCustomer(id));
    }

    private CustomerEntity findCustomer(Long id) throws Exception {
        return customerRepository.findById(id)
                .orElseThrow(() -> new Exception("Customer not found: " + id));
    }
}
