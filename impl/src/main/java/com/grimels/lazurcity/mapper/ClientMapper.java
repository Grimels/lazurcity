package com.grimels.lazurcity.mapper;

import com.grimels.lazurcity.entity.ClientEntity;
import com.grimels.lazurcityapi.model.ClientDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ClientMapper {

    ClientDTO toClientDTO(ClientEntity clientEntity);

    ClientEntity toClientEntity(ClientDTO clientDTO);

}
