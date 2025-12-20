package com.drop.shiping.api.drop_shiping_api.transactions.mappers;

import com.drop.shiping.api.drop_shiping_api.transactions.dtos.OrderResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.drop.shiping.api.drop_shiping_api.transactions.entities.Transaction;
import com.drop.shiping.api.drop_shiping_api.transactions.dtos.NewOrderDTO;
import com.drop.shiping.api.drop_shiping_api.transactions.dtos.UpdateOrderDTO;

@Mapper
public interface TransactionMapper {
    TransactionMapper MAPPER = Mappers.getMapper(TransactionMapper.class);

    @Mapping(target = "id", ignore = true)
    Transaction orderCreateDTOtoOrder(NewOrderDTO dto);

    OrderResponseDTO orderToResponseDTO(Transaction dto);

    @Mapping(target = "id", ignore = true)
    void toUpdateOrder(UpdateOrderDTO dto, @MappingTarget Transaction order);
}
