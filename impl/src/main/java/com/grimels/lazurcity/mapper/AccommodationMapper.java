package com.grimels.lazurcity.mapper;

import com.grimels.lazurcity.entity.AccommodationEntity;
import com.grimels.lazurcity.entity.ClientEntity;
import com.grimels.lazurcity.entity.RoomEntity;
import com.grimels.lazurcityapi.model.Accommodation;
import com.grimels.lazurcityapi.model.projection.AccommodationProjection;
import com.grimels.lazurcityapi.model.request.CreateAccommodationRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AccommodationMapper {

    @Mapping(source = "accommodationEntity.room.id", target = "roomId")
    AccommodationProjection toAccommodationProjection(AccommodationEntity accommodationEntity);

    Accommodation fromAccommodationEntity(AccommodationEntity accommodationEntity);

    AccommodationEntity toAccommodationEntity(Accommodation accommodation);

    default AccommodationEntity toAccommodationEntity(CreateAccommodationRequest createAccommodationRequest,
                                                      ClientEntity clientEntity,
                                                      RoomEntity roomEntity) {
        AccommodationEntity entity = new AccommodationEntity();
        entity.setClient(clientEntity);
        entity.setRoom(roomEntity);
        entity.setStartDate(createAccommodationRequest.getStartDate());
        entity.setEndDate(createAccommodationRequest.getEndDate());
        entity.setPrice(createAccommodationRequest.getPrice());
        entity.setQuantity(createAccommodationRequest.getQuantity());

        return entity;
    }

}
