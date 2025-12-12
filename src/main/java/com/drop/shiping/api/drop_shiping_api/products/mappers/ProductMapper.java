package com.drop.shiping.api.drop_shiping_api.products.mappers;

import com.drop.shiping.api.drop_shiping_api.products.dtos.ProductDTO;
import com.drop.shiping.api.drop_shiping_api.products.dtos.ProductResponseDTO;
import com.drop.shiping.api.drop_shiping_api.products.dtos.VariantDTO;
import com.drop.shiping.api.drop_shiping_api.products.entities.Variant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.drop.shiping.api.drop_shiping_api.products.entities.Product;

import java.util.List;

@Mapper
public interface ProductMapper {
    ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "variants", expression = "java(variantsList)")
    Product productDTOtoProduct(ProductDTO dto, List<Variant> variantsList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productImages", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "variants", ignore = true)
    void toUpdateProduct(ProductDTO dto, @MappingTarget Product product);

    @Mapping(target = "categoriesList", ignore = true)
    ProductDTO productToProductDTO(Product product);

    @Mapping(target = "categoriesList", ignore = true)
    @Mapping(target = "variants", expression = "java(variants)")
    ProductResponseDTO productToResponseDTO(Product product, List<VariantDTO> variants);
}
