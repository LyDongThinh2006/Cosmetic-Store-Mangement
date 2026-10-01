package com.thinh.cosmetic.rest.account;

import com.thinh.cosmetic.domain.dto.request.account.BeautyProfileRequest;
import com.thinh.cosmetic.domain.dto.request.account.CustomerAddressRequest;
import com.thinh.cosmetic.domain.dto.request.account.CustomerProfileRequest;
import com.thinh.cosmetic.domain.dto.response.account.BeautyProfileResponse;
import com.thinh.cosmetic.domain.dto.response.account.CustomerAddressResponse;
import com.thinh.cosmetic.domain.dto.response.account.CustomerResponse;
import com.thinh.cosmetic.service.account.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerRestController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAll() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<CustomerResponse> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody CustomerProfileRequest request
    ) throws Exception {
        return ResponseEntity.ok(customerService.updateProfile(id, request));
    }

    @GetMapping("/{id}/addresses")
    public ResponseEntity<List<CustomerAddressResponse>> getAddresses(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getAddresses(id));
    }

    @PostMapping("/{id}/addresses")
    public ResponseEntity<CustomerAddressResponse> addAddress(
            @PathVariable Long id,
            @Valid @RequestBody CustomerAddressRequest request
    ) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.addAddress(id, request));
    }

    @PutMapping("/{id}/addresses/{addressId}")
    public ResponseEntity<CustomerAddressResponse> updateAddress(
            @PathVariable Long id,
            @PathVariable Long addressId,
            @Valid @RequestBody CustomerAddressRequest request
    ) throws Exception {
        return ResponseEntity.ok(customerService.updateAddress(id, addressId, request));
    }

    @DeleteMapping("/{id}/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id, @PathVariable Long addressId) throws Exception {
        customerService.deleteAddress(id, addressId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/beauty-profile")
    public ResponseEntity<BeautyProfileResponse> getBeautyProfile(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(customerService.getBeautyProfile(id));
    }

    @PutMapping("/{id}/beauty-profile")
    public ResponseEntity<BeautyProfileResponse> updateBeautyProfile(
            @PathVariable Long id,
            @Valid @RequestBody BeautyProfileRequest request
    ) throws Exception {
        return ResponseEntity.ok(customerService.updateBeautyProfile(id, request));
    }
}
