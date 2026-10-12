package com.thinh.cosmetic.service.catalog.impl;

import com.thinh.cosmetic.domain.dto.request.catalog.ProductRequest;
import com.thinh.cosmetic.domain.dto.response.catalog.ProductResponse;
import com.thinh.cosmetic.domain.entity.catalog.BrandEntity;
import com.thinh.cosmetic.domain.entity.catalog.CategoryEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductEntity;
import com.thinh.cosmetic.mapper.catalog.ProductMapper;
import com.thinh.cosmetic.repository.catalog.BrandRepository;
import com.thinh.cosmetic.repository.catalog.CategoryRepository;
import com.thinh.cosmetic.repository.catalog.ProductRepository;
import com.thinh.cosmetic.service.catalog.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinh.cosmetic.domain.dto.response.catalog.ProductSkuResponse;
import com.thinh.cosmetic.domain.entity.catalog.ProductAttributeValueEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductImageEntity;
import com.thinh.cosmetic.domain.entity.catalog.ProductSkuEntity;
import com.thinh.cosmetic.domain.entity.inventory.InventoryEntity;
import com.thinh.cosmetic.repository.catalog.ProductAttributeValueRepository;
import com.thinh.cosmetic.repository.catalog.ProductImageRepository;
import com.thinh.cosmetic.repository.catalog.ProductSkuRepository;
import com.thinh.cosmetic.repository.store.InventoryRepository;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductSkuRepository productSkuRepository;
    private final ProductImageRepository productImageRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductAttributeValueRepository productAttributeValueRepository;

    @Override
    public ProductResponse create(ProductRequest request) throws Exception {
        ProductEntity product = productMapper.toEntity(request);

        if (request.getBrandId() != null){
            BrandEntity brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() ->
                            new Exception(
                                    "Brand not found: " + request.getBrandId()
                            ));
            product.setBrand(brand);
        }

        if (request.getCategoryId() != null) {
            CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() ->
                            new Exception(
                                    "Category not found: " + request.getCategoryId()
                            ));
            product.setCategory(category);
        }

        ProductEntity savedProduct = productRepository.save(product);

        return enrich(savedProduct, productMapper.toResponse(savedProduct));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) throws Exception {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                "Product not found: " + id
                        ));
        return enrich(product, productMapper.toResponse(product));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAll() {
        return productRepository.findAll()
                .stream()
                .map(p -> enrich(p, productMapper.toResponse(p)))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> searchAndFilter(
            String keyword,
            Long brandId,
            Long categoryId,
            String skinType,
            String concern,
            String priceRange,
            String sortBy
    ) {
        List<ProductResponse> all = getAll();

        return all.stream()
                .filter(p -> matchesKeyword(p, keyword))
                .filter(p -> brandId == null || (p.getBrand() != null && Objects.equals(p.getBrand().getId(), brandId)))
                .filter(p -> categoryId == null || (p.getCategory() != null && Objects.equals(p.getCategory().getId(), categoryId)))
                .filter(p -> matchesSkinType(p, skinType))
                .filter(p -> matchesConcern(p, concern))
                .filter(p -> matchesPriceRange(p, priceRange))
                .sorted(getComparator(sortBy))
                .toList();
    }

    private boolean matchesKeyword(ProductResponse p, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return true;
        }
        String kwLower = keyword.trim().toLowerCase();
        String kwNormalized = stripAccents(kwLower);

        List<String> searchTargets = new ArrayList<>();
        if (p.getName() != null) searchTargets.add(p.getName());
        if (p.getBrand() != null && p.getBrand().getName() != null) searchTargets.add(p.getBrand().getName());
        if (p.getCategory() != null && p.getCategory().getName() != null) searchTargets.add(p.getCategory().getName());
        if (p.getDescription() != null) searchTargets.add(p.getDescription());
        if (p.getMainIngredients() != null) searchTargets.add(p.getMainIngredients());
        if (p.getUses() != null) searchTargets.add(p.getUses());
        if (p.getOrigin() != null) searchTargets.add(p.getOrigin());
        if (p.getSkus() != null) {
            for (ProductSkuResponse sku : p.getSkus()) {
                if (sku.getSkuCode() != null) searchTargets.add(sku.getSkuCode());
                if (sku.getVariantName() != null) searchTargets.add(sku.getVariantName());
            }
        }

        for (String target : searchTargets) {
            String targetLower = target.toLowerCase();
            if (targetLower.contains(kwLower)) return true;
            if (stripAccents(targetLower).contains(kwNormalized)) return true;
        }
        return false;
    }

    private String stripAccents(String s) {
        if (s == null) return "";
        String n = Normalizer.normalize(s, Normalizer.Form.NFD);
        return n.replaceAll("\\p{M}", "").replace("đ", "d").replace("Đ", "d");
    }

    private boolean matchesSkinType(ProductResponse p, String skinType) {
        if (skinType == null || skinType.isBlank()) return true;
        if (p.getSkinTypes() == null || p.getSkinTypes().isEmpty()) return false;
        String stNorm = stripAccents(skinType.toLowerCase());
        return p.getSkinTypes().stream()
                .anyMatch(s -> stripAccents(s.toLowerCase()).contains(stNorm) || stNorm.contains(stripAccents(s.toLowerCase())));
    }

    private boolean matchesConcern(ProductResponse p, String concern) {
        if (concern == null || concern.isBlank()) return true;
        if (p.getConcerns() == null || p.getConcerns().isEmpty()) return false;
        String cNorm = stripAccents(concern.toLowerCase());
        return p.getConcerns().stream()
                .anyMatch(c -> stripAccents(c.toLowerCase()).contains(cNorm) || cNorm.contains(stripAccents(c.toLowerCase())));
    }

    private boolean matchesPriceRange(ProductResponse p, String priceRange) {
        if (priceRange == null || priceRange.isBlank()) return true;
        BigDecimal price = p.getMinPrice() != null ? p.getMinPrice() : BigDecimal.ZERO;
        return switch (priceRange.toLowerCase()) {
            case "under300", "under500" -> price.compareTo(new BigDecimal("500000")) < 0;
            case "300to600", "500to1000" -> price.compareTo(new BigDecimal("500000")) >= 0 && price.compareTo(new BigDecimal("1000000")) <= 0;
            case "above1000" -> price.compareTo(new BigDecimal("1000000")) > 0;
            default -> true;
        };
    }

    private Comparator<ProductResponse> getComparator(String sortBy) {
        if ("price-asc".equalsIgnoreCase(sortBy)) {
            return Comparator.comparing(p -> p.getMinPrice() != null ? p.getMinPrice() : BigDecimal.ZERO);
        } else if ("price-desc".equalsIgnoreCase(sortBy)) {
            return Comparator.comparing((ProductResponse p) -> p.getMinPrice() != null ? p.getMinPrice() : BigDecimal.ZERO).reversed();
        } else if ("featured".equalsIgnoreCase(sortBy)) {
            return Comparator.comparing((ProductResponse p) -> Boolean.TRUE.equals(p.getIsFeatured())).reversed()
                    .thenComparing(ProductResponse::getId, Comparator.nullsLast(Comparator.reverseOrder()));
        } else {
            return Comparator.comparing(ProductResponse::getId, Comparator.nullsLast(Comparator.reverseOrder()));
        }
    }

    private ProductResponse enrich(ProductEntity product, ProductResponse res) {
        if (res == null || product == null) return res;

        // 1. SKUs
        List<ProductSkuEntity> skuEntities = productSkuRepository.findByProductId(product.getId());
        List<ProductSkuResponse> skus = skuEntities.stream().map(s -> ProductSkuResponse.builder()
                .id(s.getId())
                .productId(product.getId())
                .productName(product.getName())
                .skuCode(s.getSkuCode())
                .variantName(s.getVariantName())
                .price(s.getPrice())
                .listPrice(s.getListPrice())
                .barcode(s.getBarcode())
                .status(s.getStatus())
                .build()).toList();
        res.setSkus(skus);

        // 2. Images
        Optional<ProductImageEntity> primaryImg = productImageRepository.findByProductIdAndIsPrimaryTrue(product.getId());
        List<ProductImageEntity> allImgs = productImageRepository.findByProductIdOrderBySortOrderAsc(product.getId());
        String defaultFallbackImg = "https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?w=600&auto=format&fit=crop&q=80";
        String primaryUrl = primaryImg.map(ProductImageEntity::getImageUrl)
                .orElseGet(() -> !allImgs.isEmpty() ? allImgs.get(0).getImageUrl() : defaultFallbackImg);
        res.setImageUrl(primaryUrl);
        res.setImageUrls(allImgs.stream().map(ProductImageEntity::getImageUrl).toList());

        // 3. Price range
        BigDecimal minPrice = skus.stream().map(ProductSkuResponse::getPrice).filter(Objects::nonNull).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal maxPrice = skus.stream().map(ProductSkuResponse::getPrice).filter(Objects::nonNull).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal listPrice = skus.stream().map(ProductSkuResponse::getListPrice).filter(Objects::nonNull).max(BigDecimal::compareTo).orElse(minPrice);
        res.setMinPrice(minPrice);
        res.setMaxPrice(maxPrice);
        res.setListPrice(listPrice);

        // 4. Total available inventory across all stores
        int totalStock = 0;
        for (ProductSkuEntity sku : skuEntities) {
            List<InventoryEntity> invs = inventoryRepository.findBySkuId(sku.getId());
            for (InventoryEntity inv : invs) {
                totalStock += Math.max(0, inv.getActualStock() - inv.getHeldQuantity());
            }
        }
        res.setTotalAvailableStock(totalStock);
        res.setInStock(totalStock > 0);

        // 5. Attributes (Skin types & Concerns)
        List<ProductAttributeValueEntity> attrValues = productAttributeValueRepository.findByProductId(product.getId());
        List<String> skinTypes = new ArrayList<>();
        List<String> concerns = new ArrayList<>();
        for (ProductAttributeValueEntity pav : attrValues) {
            if (pav.getAttribute() != null) {
                if ("SKIN_TYPE".equalsIgnoreCase(pav.getAttribute().getGroup())) {
                    skinTypes.add(pav.getAttribute().getName());
                } else if ("NEED".equalsIgnoreCase(pav.getAttribute().getGroup())) {
                    concerns.add(pav.getAttribute().getName());
                } else {
                    concerns.add(pav.getAttribute().getName());
                }
            }
        }
        res.setSkinTypes(skinTypes);
        res.setConcerns(concerns);

        return res;
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request) throws Exception {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                "Product not found: " + id
                        ));

        productMapper.updateEntity(request, product);

        if (request.getBrandId() != null){
            BrandEntity brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() ->
                            new Exception(
                                    "Brand not found: " + request.getBrandId()
                            ));
            product.setBrand(brand);
        }

        if (request.getCategoryId() != null) {
            CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() ->
                            new Exception(
                                    "Category not found: " + request.getCategoryId()
                            ));
            product.setCategory(category);
        }

        ProductEntity savedProduct = productRepository.save(product);

        return productMapper.toResponse(savedProduct);
    }

    @Override
    public void delete(Long id) throws Exception {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() ->
                        new Exception(
                                "Product not found: " + id
                        ));
        productRepository.deleteById(id);
    }
}
