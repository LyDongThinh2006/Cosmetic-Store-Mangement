package com.thinh.cosmetic.service.returns.impl;

import com.thinh.cosmetic.domain.dto.request.returns.ReturnRequestDto;
import com.thinh.cosmetic.domain.dto.response.returns.ReturnRequestResponse;
import com.thinh.cosmetic.domain.entity.account.EmployeeEntity;
import com.thinh.cosmetic.domain.entity.sales.OrderEntity;
import com.thinh.cosmetic.domain.entity.sales.OrderItemEntity;
import com.thinh.cosmetic.domain.entity.returns.*;
import com.thinh.cosmetic.domain.enums.ReturnStatus;
import com.thinh.cosmetic.repository.account.EmployeeRepository;
import com.thinh.cosmetic.repository.order.OrderItemRepository;
import com.thinh.cosmetic.repository.order.OrderRepository;
import com.thinh.cosmetic.repository.returns.*;
import com.thinh.cosmetic.service.returns.ReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReturnServiceImpl implements ReturnService {
    private final ReturnRequestRepository returnRequestRepository;
    private final ReturnItemRepository returnItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public ReturnRequestResponse create(ReturnRequestDto request) throws Exception {
        OrderEntity order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new Exception("Order not found: " + request.getOrderId()));

        ReturnRequestEntity ret = ReturnRequestEntity.builder()
                .order(order)
                .reason(request.getReason())
                .status(ReturnStatus.REQUESTED)
                .build();
        ret = returnRequestRepository.save(ret);

        List<ReturnItemEntity> items = new ArrayList<>();
        for (ReturnRequestDto.ReturnItemDto itemDto : request.getItems()) {
            OrderItemEntity orderItem = orderItemRepository.findById(itemDto.getOrderItemId())
                    .orElseThrow(() -> new Exception("Order item not found: " + itemDto.getOrderItemId()));

            ReturnItemEntity item = ReturnItemEntity.builder()
                    .returnRequest(ret)
                    .orderItem(orderItem)
                    .quantity(itemDto.getQuantity())
                    .detailReason(itemDto.getDetailReason())
                    .build();
            items.add(returnItemRepository.save(item));
        }
        ret.setItems(items);

        return toResponse(ret);
    }

    @Override
    @Transactional(readOnly = true)
    public ReturnRequestResponse getById(Long id) throws Exception {
        ReturnRequestEntity ret = returnRequestRepository.findById(id)
                .orElseThrow(() -> new Exception("Return request not found: " + id));
        return toResponse(ret);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReturnRequestResponse> getAll() {
        return returnRequestRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReturnRequestResponse> getByCustomer(Long customerId) {
        return returnRequestRepository.findByCustomerId(customerId).stream().map(this::toResponse).toList();
    }

    @Override
    public ReturnRequestResponse updateStatus(Long id, ReturnStatus status, Long employeeId) throws Exception {
        ReturnRequestEntity ret = returnRequestRepository.findById(id)
                .orElseThrow(() -> new Exception("Return request not found: " + id));

        ret.setStatus(status);
        ret.setProcessedBy(employeeRepository.findById(employeeId).orElse(null));
        ret.setProcessedAt(LocalDateTime.now());
        return toResponse(returnRequestRepository.save(ret));
    }

    private ReturnRequestResponse toResponse(ReturnRequestEntity r) {
        List<ReturnItemEntity> items = r.getItems() != null ? r.getItems() :
                returnItemRepository.findByReturnRequestId(r.getId());

        List<ReturnRequestResponse.ReturnItemResponse> itemResponses = items.stream()
                .map(i -> ReturnRequestResponse.ReturnItemResponse.builder()
                        .id(i.getId())
                        .orderItemId(i.getOrderItem().getId())
                        .productName(i.getOrderItem().getSnapshotProductName())
                        .quantity(i.getQuantity())
                        .detailReason(i.getDetailReason())
                        .build())
                .toList();

        return ReturnRequestResponse.builder()
                .id(r.getId())
                .orderId(r.getOrder().getId())
                .reason(r.getReason())
                .status(r.getStatus())
                .requestedAt(r.getRequestedAt())
                .processedByName(r.getProcessedBy() != null ? r.getProcessedBy().getFullName() : null)
                .processedAt(r.getProcessedAt())
                .items(itemResponses)
                .build();
    }
}
