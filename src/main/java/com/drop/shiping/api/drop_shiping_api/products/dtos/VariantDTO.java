package com.drop.shiping.api.drop_shiping_api.products.dtos;

import com.drop.shiping.api.drop_shiping_api.common.validation.IfExists;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

import java.util.List;

public record VariantDTO(
    String id,

    @NotBlank(message = "{NotBlank.validation.text}")
    @IfExists(field = "name", entity = "Variant")
    String name,

    @NotBlank
    String type,

    @NotBlank(message = "{NotBlank.validation.text}")
    String tag,

    @NotEmpty(message = "{NotEmpty.validation.list}")
    List<String> listValues
) {
}
