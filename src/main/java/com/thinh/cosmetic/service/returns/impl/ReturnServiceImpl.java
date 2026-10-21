package com.thinh.cosmetic.service.returns.impl;

import com.thinh.cosmetic.domain.dto.request.returns.ReturnRequestDto;
import com.thinh.cosmetic.domain.dto.response.returns.ReturnRequestResponse;
import com.thinh.cosmetic.domain.entity.account.EmployeeEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductSkuEntity;
import com.thinh.cosmetic.domain.entity.inventory.InventoryEntity;
import com.thinh.cosmetic.domain.entity.inventory.StoreEntity;
import com.thinh.cosmetic.domain.entity.returns.ReturnItemEntity;
import com.thinh.cosmetic.domain.entity.returns.ReturnRequestEntity;
import com.thinh.cosmetic.domain.entity.sales.OrderEntity;
import com.thinh.cosmetic.domain.entity.sales.OrderItemEntity;
import com.thinh.cosmetic.domain.enums.OrderStatus;
import com.thinh.cosmetic.domain.enums.ReturnStatus;
import com.thinh.cosmetic.exception.BusinessException;
import com.thinh.cosmetic.exception.ErrorCode;
import com.thinh.cosmetic.domain.entity.catalog.ProductImageEntity;
import com.thinh.cosmetic.repository.account.EmployeeRepository;
import com.thinh.cosmetic.repository.catalog.ProductImageRepository;
import com.thinh.cosmetic.repository.order.OrderItemRepository;
import com.thinh.cosmetic.repository.order.OrderRepository;
import com.thinh.cosmetic.repository.returns.ReturnItemRepository;
import com.thinh.cosmetic.repository.returns.ReturnRequestRepository;
import com.thinh.cosmetic.repository.store.InventoryRepository;
import com.thinh.cosmetic.service.returns.ReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ReturnServiceImpl implements ReturnService {
    private final ReturnRequestRepository returnRequestRepository;
    private final ReturnItemRepository returnItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final EmployeeRepository employeeRepository;
    private final InventoryRepository inventoryRepository;

    @Autowired(required = false)
    private ProductImageRepository productImageRepository;

    @Override
    public ReturnRequestResponse create(Long customerId, ReturnRequestDto request) throws Exception {
        if (request == null || request.getOrderId() == null) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Mã đơn hàng không được để trống khi tạo yêu cầu trả hàng");
        }
        if (request.getReason() == null || request.getReason().isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Lý do đổi/trả hàng không được để trống");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Danh sách mặt hàng yêu cầu đổi/trả không được để trống");
        }

        OrderEntity order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND, "Không tìm thấy đơn hàng #" + request.getOrderId()));

        // 1. Xác nhận quyền của khách hàng
        if (customerId != null) {
            if (order.getCustomer() == null || !order.getCustomer().getId().equals(customerId)) {
                throw new BusinessException(ErrorCode.ACCESS_DENIED, "Đơn hàng này không thuộc về tài khoản của bạn");
            }
        }

        // 2. Xác minh điều kiện đơn hàng được phép trả
        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw new BusinessException(ErrorCode.INVALID_STATUS_TRANSITION,
                    "Chỉ có thể yêu cầu trả hàng đối với đơn hàng đã hoàn tất giao hàng (COMPLETED)");
        }

        // 3. Gom nhóm số lượng theo orderItemId & kiểm tra hợp lệ
        Map<Long, Integer> requestedQuantities = new LinkedHashMap<>();
        Map<Long, String> itemReasons = new HashMap<>();
        for (ReturnRequestDto.ReturnItemDto itemDto : request.getItems()) {
            if (itemDto.getOrderItemId() == null) {
                throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Mã mặt hàng (orderItemId) không được để trống");
            }
            if (itemDto.getQuantity() == null || itemDto.getQuantity() <= 0) {
                throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Số lượng trả phải lớn hơn 0");
            }
            requestedQuantities.merge(itemDto.getOrderItemId(), itemDto.getQuantity(), Integer::sum);
            if (itemDto.getDetailReason() != null && !itemDto.getDetailReason().isBlank()) {
                itemReasons.put(itemDto.getOrderItemId(), itemDto.getDetailReason().trim());
            }
        }

        // 4. Kiểm tra từng mặt hàng thuộc đơn và giới hạn số lượng trả
        Map<Long, OrderItemEntity> orderItemMap = new HashMap<>();
        BigDecimal totalRefund = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : requestedQuantities.entrySet()) {
            Long orderItemId = entry.getKey();
            int requestedQty = entry.getValue();

            OrderItemEntity orderItem = orderItemRepository.findById(orderItemId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND,
                            "Không tìm thấy mặt hàng #" + orderItemId + " trong hệ thống"));

            if (orderItem.getOrder() == null || !orderItem.getOrder().getId().equals(order.getId())) {
                throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                        String.format("Mặt hàng '%s' (ID #%d) không thuộc đơn hàng #%s",
                                orderItem.getSnapshotProductName(), orderItemId, order.getOrderCode()));
            }

            // Tính số lượng đã được yêu cầu trả trước đó (trừ những đơn đã bị từ chối REJECTED)
            List<ReturnItemEntity> previousReturnItems = returnItemRepository.findByOrderItemId(orderItemId);
            int alreadyReturnedQty = previousReturnItems.stream()
                    .filter(ri -> ri.getReturnRequest() != null && ri.getReturnRequest().getStatus() != ReturnStatus.REJECTED)
                    .mapToInt(ReturnItemEntity::getQuantity)
                    .sum();

            int maxReturnableQty = orderItem.getQuantity() - alreadyReturnedQty;
            if (requestedQty > maxReturnableQty) {
                throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                        String.format("Số lượng yêu cầu trả (%d) vượt quá số lượng tối đa có thể trả (%d) cho sản phẩm '%s' (Đã mua: %d, Đã yêu cầu/trả: %d)",
                                requestedQty, Math.max(0, maxReturnableQty), orderItem.getSnapshotProductName(),
                                orderItem.getQuantity(), alreadyReturnedQty));
            }

            orderItemMap.put(orderItemId, orderItem);

            BigDecimal unitPrice = orderItem.getSnapshotPrice() != null ? orderItem.getSnapshotPrice() : BigDecimal.ZERO;
            totalRefund = totalRefund.add(unitPrice.multiply(BigDecimal.valueOf(requestedQty)));
        }

        // 5. Tạo và lưu ReturnRequestEntity
        ReturnRequestEntity ret = ReturnRequestEntity.builder()
                .order(order)
                .customer(order.getCustomer())
                .reason(request.getReason())
                .description(request.getDescription())
                .status(ReturnStatus.REQUESTED)
                .refundAmount(totalRefund)
                .build();
        ret = returnRequestRepository.save(ret);

        List<ReturnItemEntity> items = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : requestedQuantities.entrySet()) {
            OrderItemEntity oi = orderItemMap.get(entry.getKey());
            BigDecimal unitPrice = oi.getSnapshotPrice() != null ? oi.getSnapshotPrice() : BigDecimal.ZERO;
            BigDecimal itemRefund = unitPrice.multiply(BigDecimal.valueOf(entry.getValue()));

            ReturnItemEntity item = ReturnItemEntity.builder()
                    .returnRequest(ret)
                    .orderItem(oi)
                    .quantity(entry.getValue())
                    .refundAmount(itemRefund)
                    .detailReason(itemReasons.get(entry.getKey()))
                    .build();
            items.add(returnItemRepository.save(item));
        }
        ret.setItems(items);

        return toResponse(ret);
    }

    @Override
    public ReturnRequestResponse create(ReturnRequestDto request) throws Exception {
        return create(null, request);
    }

    @Override
    @Transactional(readOnly = true)
    public ReturnRequestResponse getById(Long id) throws Exception {
        ReturnRequestEntity ret = returnRequestRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Không tìm thấy yêu cầu trả hàng #" + id));
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
    @Transactional(readOnly = true)
    public List<ReturnRequestResponse> getByOrder(Long orderId) {
        return returnRequestRepository.findByOrderId(orderId).stream().map(this::toResponse).toList();
    }

    @Override
    public ReturnRequestResponse updateStatus(Long id, ReturnStatus status, Long employeeId, String note, Boolean restock) throws Exception {
        ReturnRequestEntity ret = returnRequestRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Không tìm thấy yêu cầu trả hàng #" + id));

        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.EMPLOYEE_NOT_FOUND, "Không tìm thấy nhân viên #" + employeeId));

        ReturnStatus currentStatus = ret.getStatus();
        if (currentStatus == status) {
            return toResponse(ret);
        }

        if (!currentStatus.canTransitionTo(status)) {
            throw new BusinessException(ErrorCode.INVALID_STATUS_TRANSITION,
                    String.format("Không thể chuyển trạng thái yêu cầu trả hàng từ %s sang %s", currentStatus, status));
        }

        boolean doRestock = (restock == null || restock);

        // Cập nhật tồn kho theo chính sách khi hoàn tất nhận hàng trả (COMPLETED)
        if (status == ReturnStatus.COMPLETED) {
            if (doRestock) {
                List<ReturnItemEntity> items = (ret.getItems() != null && !ret.getItems().isEmpty())
                        ? ret.getItems()
                        : returnItemRepository.findByReturnRequestId(ret.getId());

                OrderEntity order = ret.getOrder();
                StoreEntity assignedStore = order != null ? order.getAssignedStore() : null;

                if (assignedStore != null && items != null) {
                    for (ReturnItemEntity item : items) {
                        if (item.getOrderItem() != null && item.getOrderItem().getSku() != null) {
                            ProductSkuEntity sku = item.getOrderItem().getSku();
                            InventoryEntity inv = inventoryRepository.findByStoreIdAndSkuId(assignedStore.getId(), sku.getId())
                                    .orElseGet(() -> inventoryRepository.save(InventoryEntity.builder()
                                            .store(assignedStore)
                                            .sku(sku)
                                            .actualStock(0)
                                            .heldQuantity(0)
                                            .build()));

                            inv.setActualStock(inv.getActualStock() + item.getQuantity());
                            inv.setUpdatedAt(LocalDateTime.now());
                            inventoryRepository.save(inv);
                        }
                    }
                }
            }
        }

        // Ghi lại lịch sử thao tác của nhân viên vào description
        if (note != null && !note.isBlank()) {
            String currentDesc = ret.getDescription() != null ? ret.getDescription() : "";
            String actionDesc = (status == ReturnStatus.COMPLETED && !doRestock)
                    ? "[QLDH-QĐ7: Hàng hỏng/lỗi không nhập kho] "
                    : (status == ReturnStatus.COMPLETED ? "[Nhập lại kho chi nhánh] " : "");
            String noteEntry = "\n[" + status + " bởi " + employee.getFullName() + "]: " + actionDesc + note.trim();
            ret.setDescription(currentDesc + noteEntry);
        } else if (status == ReturnStatus.COMPLETED && !doRestock) {
            String currentDesc = ret.getDescription() != null ? ret.getDescription() : "";
            ret.setDescription(currentDesc + "\n[COMPLETED bởi " + employee.getFullName() + "]: [QLDH-QĐ7] Hàng hỏng/lỗi không nhập kho.");
        }

        ret.setStatus(status);
        ret.setProcessedBy(employee);
        ret.setProcessedAt(LocalDateTime.now());
        return toResponse(returnRequestRepository.save(ret));
    }

    @Override
    public ReturnRequestResponse updateStatus(Long id, ReturnStatus status, Long employeeId) throws Exception {
        return updateStatus(id, status, employeeId, null, true);
    }

    private ReturnRequestResponse toResponse(ReturnRequestEntity r) {
        List<ReturnItemEntity> items = r.getItems() != null ? r.getItems() :
                returnItemRepository.findByReturnRequestId(r.getId());

        String custPhone = null;
        String custEmail = null;
        if (r.getCustomer() != null && r.getCustomer().getAccount() != null) {
            custPhone = r.getCustomer().getAccount().getPhone();
            custEmail = r.getCustomer().getAccount().getEmail();
        } else if (r.getOrder() != null && r.getOrder().getCustomer() != null && r.getOrder().getCustomer().getAccount() != null) {
            custPhone = r.getOrder().getCustomer().getAccount().getPhone();
            custEmail = r.getOrder().getCustomer().getAccount().getEmail();
        }

        Long storeId = (r.getOrder() != null && r.getOrder().getAssignedStore() != null) ? r.getOrder().getAssignedStore().getId() : null;
        String storeName = (r.getOrder() != null && r.getOrder().getAssignedStore() != null) ? r.getOrder().getAssignedStore().getName() : null;

        List<ReturnRequestResponse.ReturnItemResponse> itemResponses = items.stream()
                .map(i -> {
                    OrderItemEntity oi = i.getOrderItem();
                    BigDecimal unitPrice = (oi != null && oi.getSnapshotPrice() != null) ? oi.getSnapshotPrice() : BigDecimal.ZERO;
                    BigDecimal itemRefund = (i.getRefundAmount() != null)
                            ? i.getRefundAmount()
                            : unitPrice.multiply(BigDecimal.valueOf(i.getQuantity()));

                    String imgUrl = null;
                    if (productImageRepository != null && oi != null && oi.getSku() != null && oi.getSku().getProduct() != null) {
                        try {
                            imgUrl = productImageRepository.findFirstByProductIdAndIsPrimaryTrue(oi.getSku().getProduct().getId())
                                    .map(ProductImageEntity::getImageUrl)
                                    .orElse(null);
                        } catch (Exception ignored) {}
                    }

                    return ReturnRequestResponse.ReturnItemResponse.builder()
                            .id(i.getId())
                            .orderItemId(oi != null ? oi.getId() : null)
                            .skuId((oi != null && oi.getSku() != null) ? oi.getSku().getId() : null)
                            .productName(oi != null ? oi.getSnapshotProductName() : "Sản phẩm")
                            .variantName(oi != null ? oi.getSnapshotVariantName() : null)
                            .imageUrl(imgUrl)
                            .unitPrice(unitPrice)
                            .quantity(i.getQuantity())
                            .refundAmount(itemRefund)
                            .detailReason(i.getDetailReason())
                            .build();
                })
                .toList();

        BigDecimal calculatedRefund = r.getRefundAmount();
        if (calculatedRefund == null) {
            calculatedRefund = itemResponses.stream()
                    .map(ReturnRequestResponse.ReturnItemResponse::getRefundAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        return ReturnRequestResponse.builder()
                .id(r.getId())
                .orderId(r.getOrder() != null ? r.getOrder().getId() : null)
                .orderCode(r.getOrder() != null ? r.getOrder().getOrderCode() : null)
                .customerId(r.getCustomer() != null ? r.getCustomer().getId() : (r.getOrder() != null && r.getOrder().getCustomer() != null ? r.getOrder().getCustomer().getId() : null))
                .customerName(r.getCustomer() != null ? r.getCustomer().getFullName() : (r.getOrder() != null && r.getOrder().getCustomer() != null ? r.getOrder().getCustomer().getFullName() : null))
                .customerPhone(custPhone)
                .customerEmail(custEmail)
                .storeId(storeId)
                .storeName(storeName)
                .reason(r.getReason())
                .description(r.getDescription())
                .status(r.getStatus())
                .refundAmount(calculatedRefund)
                .requestedAt(r.getRequestedAt())
                .processedByName(r.getProcessedBy() != null ? r.getProcessedBy().getFullName() : null)
                .processedAt(r.getProcessedAt())
                .items(itemResponses)
                .build();
    }
}

