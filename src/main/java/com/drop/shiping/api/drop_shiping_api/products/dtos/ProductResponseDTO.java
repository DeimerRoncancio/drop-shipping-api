package com.drop.shiping.api.drop_shiping_api.products.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.drop.shiping.api.drop_shiping_api.images.entities.Image;
import com.drop.shiping.api.drop_shiping_api.products.entities.ProductCategory;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public record ProductResponseDTO(
    String id,
    String productName,
    String description,
    Long price,

    @JsonIgnoreProperties("id")
    List<Image> productImages,

    @JsonIgnoreProperties({"id", "products"})
    List<ProductCategory> categories,

    List<VariantDTO> variants,

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    List<MultipartFile> images,

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    List<String> categoriesList

) {}
