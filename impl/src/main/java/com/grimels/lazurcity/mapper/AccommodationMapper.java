package com.grimels.lazurcity.mapper;

import com.grimels.lazurcity.entity.AccommodationEntity;
import com.grimels.lazurcityapi.model.AccommodationDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AccommodationMapper {

    AccommodationDTO toAccommodationDTO(AccommodationEntity accommodationEntity);

    AccommodationEntity toAccommodationEntity(AccommodationDTO accommodationDTO);

}
