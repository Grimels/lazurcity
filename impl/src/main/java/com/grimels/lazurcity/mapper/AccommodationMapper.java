package com.grimels.lazurcity.mapper;

import com.grimels.lazurcity.entity.AccommodationEntity;
import com.grimels.lazurcity.entity.ClientEntity;
import com.grimels.lazurcity.entity.RoomEntity;
import com.grimels.lazurcityapi.model.Accommodation;
import com.grimels.lazurcityapi.model.request.CreateAccommodationRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AccommodationMapper {

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
        entity.setPrice(createAccommodationRequest.getPrice().doubleValue());
        entity.setQuantity(createAccommodationRequest.getQuantity());
        entity.setComment(createAccommodationRequest.getComment());

        return entity;
    }

}
