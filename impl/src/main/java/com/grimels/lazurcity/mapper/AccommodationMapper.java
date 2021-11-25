package com.grimels.lazurcity.mapper;

import com.grimels.lazurcity.entity.AccommodationEntity;
import com.grimels.lazurcity.entity.ClientEntity;
import com.grimels.lazurcity.entity.RoomEntity;
import com.grimels.lazurcityapi.model.Accommodation;
import com.grimels.lazurcityapi.model.request.CreateAccommodationRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface AccommodationMapper {

    Accommodation fromAccommodationEntity(AccommodationEntity accommodationEntity);

    @Mapping(source = "clientEntity", target = "client")
    @Mapping(source = "roomEntity", target = "room")
    @Mapping(source = "request.startDate", target = "startDate")
    @Mapping(source = "request.endDate", target = "endDate")
    @Mapping(source = "request.price", target = "price")
    @Mapping(source = "request.quantity", target = "quantity")
    @Mapping(source = "request.comment", target = "comment")
    @Mapping(source = "request.isFinal", target = "isFinal")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    AccommodationEntity toAccommodationEntity(CreateAccommodationRequest request,
                                              ClientEntity clientEntity,
                                              RoomEntity roomEntity);

}
