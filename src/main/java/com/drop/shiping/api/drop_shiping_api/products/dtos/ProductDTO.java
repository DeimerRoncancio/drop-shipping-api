package com.drop.shiping.api.drop_shiping_api.products.dtos;

import java.util.List;

import com.drop.shiping.api.drop_shiping_api.common.validation.IfExists;
import com.drop.shiping.api.drop_shiping_api.common.validation.ImageFormat;
import com.drop.shiping.api.drop_shiping_api.products.validation.ExistingCategories;
import jakarta.validation.Valid;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.drop.shiping.api.drop_shiping_api.images.entities.Image;
import com.drop.shiping.api.drop_shiping_api.products.entities.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record ProductDTO(
    @NotBlank(message = "{NotBlank.validation.text}")
    @IfExists(message = "{IfExists.validation}", entity = "Product", field = "productName")
    String productName,

    @NotBlank(message = "{NotBlank.validation.text}")
    @Size(max = 400, message = "{Size.product.description}")
    String description,

    @NotNull(message = "{NotBlank.validation.text}")
    Long price,

    @JsonIgnoreProperties("id")
    List<Image> productImages,

    @JsonIgnoreProperties({"id", "products"})
    List<ProductCategory> categories,

    @Valid
    List<VariantDTO> variants,

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    List<String> variantsToRemove,

    @ImageFormat(maxSize = 3 * 1024 * 1024)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    List<MultipartFile> images,

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    List<String> imagesToRemove,

    @ExistingCategories(message = "{IfExists.category.name}")
    @NotEmpty(message = "{NotEmpty.validation.list}")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    List<String> categoriesList
) {
}
